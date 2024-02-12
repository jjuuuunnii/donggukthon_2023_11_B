package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rednosed.app.contrant.Constants;
import rednosed.app.domain.rds.*;
import rednosed.app.dto.request.LikeDto;
import rednosed.app.dto.request.SealNewDto;
import rednosed.app.dto.response.*;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.rds.LikeSealRepository;
import rednosed.app.repository.rds.SealRepository;
import rednosed.app.repository.rds.UserRepository;
import rednosed.app.util.GCSUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SealService {

    private final SealRepository sealRepository;
    private final UserRepository userRepository;
    private final LikeSealRepository likeSealRepository;
    private final GCSUtil gcsUtil;


    //2-5. 마이페이지(내가 만든 씰 목록)
    @Transactional(readOnly = true)
    public SealListDto showUserSealList(User tmpUser) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Seal> sealList = sealRepository.findByUser(user);
        List<LikeSeal> likeSealList = likeSealRepository.findByUser(user);

        Map<Seal, Long> likeSealCountMap = likeSealList.stream()
                .collect(Collectors.groupingBy(LikeSeal::getSeal, Collectors.counting()));

        List<SealInfoDto> sealInfoDtoList = sealList.stream().map(seal -> {
            long likeCount = likeSealCountMap.getOrDefault(seal, 0L);
            boolean isLiked = likeCount > 0;

            return SealInfoDto.builder()
                    .sealImg(seal.getSealImgUrl())
                    .sealName(seal.getSealName())
                    .likeCnt((int) likeCount)
                    .like(isLiked)
                    .build();
        }).collect(Collectors.toList());

        return SealListDto.builder()
                .sealList(sealInfoDtoList)
                .build();
    }

    //4-2. 씰 만들기(만들기)
    @Transactional
    public SealIdDto makeNewSeal(User tmpUser, SealNewDto sealNewDto) throws IOException {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String sealClientId = UUID.randomUUID().toString();
        File sealFile = convertBase64ToImage(sealNewDto.sealImg(), sealClientId);
        String sealFullPath = gcsUtil.saveFileImageToGCS(sealFile, Constants.T_SEAL);
        Seal seal = Seal.builder()
                .sealClientId(sealClientId)
                .sealName(sealNewDto.sealName())
                .sealImgUrl(sealFullPath)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        sealRepository.save(seal);

        return SealIdDto.builder()
                .sealId(seal.getSealClientId())
                .build();
    }

    private File convertBase64ToImage(String base64String, String sealClientId) throws IOException {
        // Base64 문자열에서 데이터 부분만 추출 (헤더 제거)
        String base64Image = base64String.split(",")[1];

        // Base64 문자열을 바이트 배열로 디코딩
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        // 바이트 배열을 BufferedImage로 변환
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

        File outputFile = new File(sealClientId);
        ImageIO.write(image, "png", outputFile);

        return outputFile;
    }

    //5. 씰 게시판
    @Transactional(readOnly = true)
    public SealListDto showSealAllList(String userClientId) {
        List<Seal> sealList = sealRepository.findAll();
        List<SealLikeDataTmpDto> sealLikeDataList = likeSealRepository.findAllSealLikeData();
        List<String> likedSealIds = likeSealRepository.findLikedSealIdsByUserClientId(userClientId);

        Set<String> likedSealIdsSet = new HashSet<>(likedSealIds);
        Map<String, Long> likeCountMap = sealLikeDataList.stream()
                .collect(Collectors.toMap(SealLikeDataTmpDto::sealId, SealLikeDataTmpDto::likeCount));

        List<SealInfoDto> sealInfoDtoList = sealList.stream()
                .map(seal -> {
                    String sealId = seal.getSealClientId();
                    long likeCount = likeCountMap.getOrDefault(sealId, 0L);
                    boolean isLiked = likedSealIdsSet.contains(sealId);

                    return SealInfoDto.builder()
                            .id(sealId)
                            .sealImg(seal.getSealImgUrl())
                            .sealName(seal.getSealName())
                            .likeCnt((int) likeCount)
                            .like(isLiked)
                            .build();
                })
                .collect(Collectors.toList());

        return SealListDto.builder()
                .sealList(sealInfoDtoList)
                .build();
    }

    //5-1. 씰 좋아요 누르기
    @Transactional
    public void putSealLike(User tmpUser, String sealClientId, LikeDto likeDto) {

        Seal seal = sealRepository.findBySealClientId(sealClientId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEAL_NOT_FOUND));
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Optional<LikeSeal> likeSealOptional = likeSealRepository.findByUserAndSeal(user, seal);

        if (likeDto.like()) { //유저가 좋아요를 누름
            if (likeSealOptional.isPresent()) {
                likeSealRepository.delete(likeSealOptional.get());
                throw new CustomException(ErrorCode.LIKED_ERROR);
            }

            LikeSeal likeSeal = LikeSeal.builder()
                    .user(user)
                    .seal(seal)
                    .createdAt(LocalDateTime.now())
                    .build();
            likeSealRepository.save(likeSeal);

        } else { //유저가 좋아요를 누름
            if (likeSealOptional.isEmpty()) {
                throw new CustomException(ErrorCode.LIKED_ERROR);
            }
            likeSealRepository.delete(likeSealOptional.get());
        }
    }

    //2-4. 마이페이지(씰 싱글)
    @Transactional(readOnly = true)
    public SealSingleInfoDto showSealSingle(String sealClientId) {
        Seal seal = sealRepository.findBySealClientId(sealClientId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEAL_NOT_FOUND));

        SealLikeDataTmpDto sealLikeData = likeSealRepository
                .findAllSealLikeDataBySealClientId(sealClientId)
                .orElse(null);

        int likeCount = sealLikeData != null ? Math.toIntExact(sealLikeData.likeCount()) : 0;
        boolean isLiked = sealLikeData != null;

        String formattedDate = seal.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        return SealSingleInfoDto.builder()
                .nickname(seal.getSealName())
                .likeCnt(likeCount)
                .like(isLiked)
                .date(formattedDate)
                .maker(seal.getUser().getNickname())
                .ImgUrl(seal.getSealImgUrl())
                .build();
    }

    //4-4. 씰 이름, 사진 요청
    public SealNewInfoDto showNewSealInfo(String sealClientId) {
        Seal seal = sealRepository.findBySealClientId(sealClientId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEAL_NOT_FOUND));

        return SealNewInfoDto.builder()
                .sealName(seal.getSealName())
                .sealImg(seal.getSealImgUrl())
                .build();
    }
}
