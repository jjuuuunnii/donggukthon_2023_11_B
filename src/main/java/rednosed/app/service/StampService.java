package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rednosed.app.contrant.Constants;
import rednosed.app.domain.nosql.Pixel;
import rednosed.app.domain.rds.*;
import rednosed.app.dto.request.LikeDto;
import rednosed.app.dto.request.StampNameDto;
import rednosed.app.dto.response.*;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.event.LoadingEvent;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.nosql.PixelRepository;
import rednosed.app.repository.rds.*;
import rednosed.app.util.GCSUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static rednosed.app.contrant.Constants.PIXEL_SIZE;

@Service
@RequiredArgsConstructor
public class StampService {

    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final UserStampRepository userStampRepository;
    private final LikeStampRepository likeStampRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final CanvasRepository canvasRepository;
    private final PixelRepository pixelRepository;
    private final GCSUtil gcsUtil;


    //2-3. 마이페이지 (내가 만든 우표 목록)
    @Transactional(readOnly = true)
    public StampListDto showUserStampList(User tmpUser) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<UserStamp> userStampList = userStampRepository.findByUser(user);
        List<LikeStamp> likeStampList = likeStampRepository.findLikeStampByUser(user);

        Map<Stamp, Long> likeCountMap = likeStampList.stream()
                .collect(Collectors.groupingBy(LikeStamp::getStamp, Collectors.counting()));

        List<StampInfoDto> stampInfoDtoList = userStampList.stream().map(userStamp -> {
            Stamp stamp = userStamp.getStamp();
            long likeCount = likeCountMap.getOrDefault(stamp, 0L);
            boolean isLiked = likeCount > 0;

            return StampInfoDto.builder()
                    .stampImg(stamp.getStampImgUrl())
                    .stampName(stamp.getStampName())
                    .likeCnt((int) likeCount)
                    .like(isLiked)
                    .build();
        }).collect(Collectors.toList());

        return StampListDto.builder()
                .stampList(stampInfoDtoList)
                .build();
    }

    //3-5. 우표 만들기(만들기 버튼을 누르고 파일이 넘어올 떄)
    @Transactional
    public StampIdDto makeNewStamp(User tmpUser, StampNameDto stampNameDto) throws IOException {

        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Pixel pixel = pixelRepository.findByCanvasClientId(user.getCanvas().getCanvasClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.CANVAS_NOT_FOUND));

        String stampClientId = UUID.randomUUID().toString();

        File stampImageFile = createImageFromPixels(pixel.getColors(), stampClientId);
        String stampFullPath = gcsUtil.saveFileImageToGCS(stampImageFile, Constants.T_STAMP);

        Stamp stamp = Stamp.builder()
                .stampClientId(stampClientId)
                .stampName(stampNameDto.stampName())
                .stampImgUrl(stampFullPath)
                .createdAt(LocalDateTime.now())
                .build();
        stampRepository.save(stamp);
        stampRepository.flush();

        UserStamp userStamp = UserStamp.builder()
                .user(user)
                .stamp(stamp)
                .createdAt(LocalDateTime.now())
                .build();
        userStampRepository.save(userStamp);

        eventPublisher.publishEvent(LoadingEvent.builder()
                .stampId(stamp.getStampClientId())
                .build());

        canvasRepository.findByRoomMaker(user).ifPresent(canvas -> {
            for (User canvasUser : canvas.getUserList()) {
                canvasUser.updateCanvas(null);
                userRepository.save(canvasUser);
            }
            userRepository.flush();
            canvasRepository.delete(canvas);
            pixelRepository.deleteAllByCanvasClientId(canvas.getCanvasClientId());
        });

        return StampIdDto.builder()
                .stampId(stamp.getStampClientId())
                .build();
    }
    private File createImageFromPixels(List<List<String>> colors, String stampClientId) throws IOException {
        int width = colors.size() * PIXEL_SIZE;
        int height = colors.get(0).size() * PIXEL_SIZE;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();

        for (int x = 0; x < colors.size(); x++) {
            for (int y = 0; y < colors.get(x).size(); y++) {
                String colorCode = colors.get(x).get(y);
                if (colorCode != null) {
                    Color color = Color.decode(colorCode);
                    graphics.setColor(color);
                    graphics.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
                }
            }
        }

        graphics.dispose();
        File outputFile = new File(stampClientId + ".png");
        ImageIO.write(image, "png", outputFile);

        return outputFile;
    }

    //3-6. 우표 이름, 사진 요청
    @Transactional(readOnly = true)
    public StampNewInfoDto showNewStampInfo(String stampClientId) {
        Stamp stamp = stampRepository.findByStampClientId(stampClientId)
                .orElseThrow(() -> new CustomException(ErrorCode.STAMP_NOT_FOUND));

        return StampNewInfoDto.builder()
                .stampName(stamp.getStampName())
                .stampImg(stamp.getStampImgUrl())
                .build();
    }

    //4. 씰 만들기(필터링: 좋아요 한 우표)
    @Transactional(readOnly = true)
    public StampListDto showLikeStampList(User tmpUser) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<StampLikeDataTmpDto> stampLikeDataTmpDtoList = likeStampRepository.findStampLikeDataByUserClientId(user.getUserClientId());

        List<StampInfoDto> stampInfoDtoList = stampLikeDataTmpDtoList.stream()
                .map(data -> StampInfoDto.builder()
                        .id(data.stampId())
                        .stampImg(data.stampImgUrl())
                        .stampName(data.stampName())
                        .likeCnt((int) data.likeCount())
                        .like(true)
                        .build())
                .toList();

        return StampListDto.builder()
                .stampList(stampInfoDtoList)
                .build();
    }

    //4-1. 씰 만들기(필터링: 전체우표)
    @Transactional(readOnly = true)
    public StampListDto showStampAllList(User user) {
        List<Stamp> stampList = stampRepository.findAll();
        List<StampLikeDataTmpDto> stampLikeDataList = likeStampRepository.findAllStampLikeData();
        List<String> likedStampIds = likeStampRepository.findLikedStampIdsByUserClientId(user.getUserClientId());

        Set<String> likedStampIdsSet = new HashSet<>(likedStampIds);
        Map<String, Long> likeCountMap = stampLikeDataList.stream()
                .collect(Collectors.toMap(StampLikeDataTmpDto::stampId, StampLikeDataTmpDto::likeCount));

        List<StampInfoDto> stampInfoDtoList = stampList.stream()
                .map(stamp -> {
                    String stampId = stamp.getStampClientId();
                    long likeCount = likeCountMap.getOrDefault(stampId, 0L);
                    boolean isLiked = likedStampIdsSet.contains(stampId);

                    return StampInfoDto.builder()
                            .id(stampId)
                            .stampImg(stamp.getStampImgUrl())
                            .stampName(stamp.getStampName())
                            .likeCnt((int) likeCount)
                            .like(isLiked)
                            .build();
                })
                .collect(Collectors.toList());

        return StampListDto.builder()
                .stampList(stampInfoDtoList)
                .build();
    }

    //4-3. 우표 좋아요 누르기
    @Transactional
    public void putStampLike(User tmpUser, String stampClientId, LikeDto likeDto) {
        Stamp stamp = stampRepository.findByStampClientId(stampClientId)
                .orElseThrow(() -> new CustomException(ErrorCode.STAMP_NOT_FOUND));

        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Optional<LikeStamp> likeStampOptional = likeStampRepository.findByUserAndStamp(user, stamp);

        if (likeDto.like()) { //유저가 좋아요를 취소함
            if (likeStampOptional.isPresent()) {
                likeStampRepository.delete(likeStampOptional.get());
                throw new CustomException(ErrorCode.LIKED_ERROR);
            }

            LikeStamp likeStamp = LikeStamp.builder()
                    .user(user)
                    .stamp(stamp)
                    .createdAt(LocalDateTime.now())
                    .build();
            likeStampRepository.save(likeStamp);

        } else { //유저가 좋아요를 누름
            if (likeStampOptional.isEmpty()) {
                throw new CustomException(ErrorCode.LIKED_ERROR);
            }
            likeStampRepository.delete(likeStampOptional.get());
        }
    }

    //2-2. 마이페이지(우표 싱글)
    @Transactional(readOnly = true)
    public StampSingleInfoDto showStampSingle(User user, String stampClientId) {
        Stamp stamp = stampRepository.findByStampClientId(stampClientId)
                .orElseThrow(() -> new CustomException(ErrorCode.STAMP_NOT_FOUND));

        StampLikeDataTmpDto stampLikeData = likeStampRepository
                .findStampLikeDataByUserClientIdAndStampId(user.getUserClientId(), stampClientId)
                .orElse(null);

        int likeCount = stampLikeData != null ? Math.toIntExact(stampLikeData.likeCount()) : 0;
        boolean isLiked = stampLikeData != null;

        List<String> friendList = userStampRepository.findUserNicknamesByStamp(stamp);

        String formattedDate = stamp.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        return StampSingleInfoDto.builder()
                .nickname(stamp.getStampName())
                .likeCnt(likeCount)
                .like(isLiked)
                .date(formattedDate)
                .friendList(friendList)
                .ImgUrl(stamp.getStampImgUrl())
                .build();
    }
}

