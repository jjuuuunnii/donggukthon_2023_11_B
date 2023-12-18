package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rednosed.app.contrant.Constants;
import rednosed.app.domain.rds.LikeSeal;
import rednosed.app.domain.rds.Seal;
import rednosed.app.domain.rds.User;
import rednosed.app.dto.request.SealNewDto;
import rednosed.app.dto.response.SealInfoDto;
import rednosed.app.dto.response.SealLikeDataTmpDto;
import rednosed.app.dto.response.SealListDto;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.rds.LikeSealRepository;
import rednosed.app.repository.rds.SealRepository;
import rednosed.app.repository.rds.UserRepository;
import rednosed.app.util.GCSUtil;

import java.io.IOException;
import java.time.LocalDateTime;
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
}
