package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rednosed.app.domain.rds.LikeStamp;
import rednosed.app.domain.rds.Stamp;
import rednosed.app.domain.rds.User;
import rednosed.app.domain.rds.UserStamp;
import rednosed.app.dto.response.StampInfoDto;
import rednosed.app.dto.response.StampListDto;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.rds.LikeStampRepository;
import rednosed.app.repository.rds.StampRepository;
import rednosed.app.repository.rds.UserRepository;
import rednosed.app.repository.rds.UserStampRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StampService {

    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final UserStampRepository userStampRepository;
    private final LikeStampRepository likeStampRepository;
    private final ApplicationEventPublisher eventPublisher;


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
}
