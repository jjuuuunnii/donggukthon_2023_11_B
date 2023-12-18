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

    @Transactional
    public void makeNewSeal(User tmpUser, SealNewDto sealNewDto) throws IOException {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String sealFullPath = gcsUtil.saveFileImageToGCS(sealNewDto.seal(), Constants.T_SEAL);
        Seal seal = Seal.builder()
                .sealClientId(UUID.randomUUID().toString())
                .sealName(sealNewDto.name())
                .sealImgUrl(sealFullPath)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        sealRepository.save(seal);
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
    public void putSealLike(User user, String sealClientId, LikeDto likeDto) {
        Seal seal = sealRepository.findBySealClientId(sealClientId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEAL_NOT_FOUND));

        Optional<LikeSeal> likeSealOptional = likeSealRepository.findByUserAndSeal(user, seal);

        if (likeDto.like()) { //유저가 좋아요를 취소함
            if (likeSealOptional.isEmpty()) {
                throw new CustomException(ErrorCode.LIKED_ERROR);
            }
            likeSealRepository.delete(likeSealOptional.get());

        } else { //유저가 좋아요를 누름
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
        }
    }

    //2-4. 마이페이지(씰 싱글)
    @Transactional(readOnly = true)
    public SealSingleInfoDto showSealSingle(User user, String sealClientId) {
        Seal seal = sealRepository.findBySealClientId(sealClientId)
                .orElseThrow(() -> new CustomException(ErrorCode.SEAL_NOT_FOUND));

        // 씰 좋아요 데이터를 가져오기 위해 LikeSealRepository 사용
        SealLikeDataTmpDto sealLikeData = likeSealRepository
                .findAllSealLikeDataBySealClientId(sealClientId)
                .orElse(null);

        int likeCount = sealLikeData != null ? Math.toIntExact(sealLikeData.likeCount()) : 0;
        boolean isLiked = sealLikeData != null;

        // 날짜 포맷 지정
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
}
