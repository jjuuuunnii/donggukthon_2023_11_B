package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rednosed.app.domain.rds.LikeSeal;
import rednosed.app.domain.rds.Seal;
import rednosed.app.domain.rds.User;
import rednosed.app.dto.response.SealInfoDto;
import rednosed.app.dto.response.SealListDto;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.rds.LikeSealRepository;
import rednosed.app.repository.rds.SealRepository;
import rednosed.app.repository.rds.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SealService {

    private final SealRepository sealRepository;
    private final UserRepository userRepository;
    private final LikeSealRepository likeSealRepository;


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
}
