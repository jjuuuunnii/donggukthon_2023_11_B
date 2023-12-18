package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rednosed.app.contrant.Constants;
import rednosed.app.domain.rds.LikeStamp;
import rednosed.app.domain.rds.Stamp;
import rednosed.app.domain.rds.User;
import rednosed.app.domain.rds.UserStamp;
import rednosed.app.dto.request.StampNewDto;
import rednosed.app.dto.response.StampInfoDto;
import rednosed.app.dto.response.StampListDto;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.event.LoadingEvent;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.rds.LikeStampRepository;
import rednosed.app.repository.rds.StampRepository;
import rednosed.app.repository.rds.UserRepository;
import rednosed.app.repository.rds.UserStampRepository;
import rednosed.app.security.oauth.info.PrincipalDetails;
import rednosed.app.util.GCSUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StampService {

    private final StampRepository stampRepository;
    private final UserRepository userRepository;
    private final UserStampRepository userStampRepository;
    private final LikeStampRepository likeStampRepository;
    private final ApplicationEventPublisher eventPublisher;
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
    public void makeNewStamp(User tmpUser, StampNewDto stampNewDto) throws IOException {

        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String stampFullPath = gcsUtil.saveFileImageToGCS(stampNewDto.stamp(), Constants.T_STAMP);

        Stamp stamp = Stamp.builder()
                .stampClientId(UUID.randomUUID().toString())
                .stampName(stampNewDto.name())
                .stampImgUrl(stampFullPath)
                .createdAt(LocalDateTime.now())
                .build();

        UserStamp userStamp = UserStamp.builder()
                .user(user)
                .stamp(stamp)
                .createdAt(LocalDateTime.now())
                .build();

        Stamp newStamp = stampRepository.save(stamp);
        userStampRepository.save(userStamp);
        stampRepository.flush();
        userStampRepository.flush();

        //로딩페이지를 위한 이벤트 처리
        eventPublisher.publishEvent(LoadingEvent.builder()
                .stampId(newStamp.getStampClientId())
                .build());
    }
}
