package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rednosed.app.domain.rds.User;
import rednosed.app.domain.rds.UserStamp;
import rednosed.app.dto.request.UserNicknameDto;
import rednosed.app.dto.response.StampInfoDto;
import rednosed.app.dto.response.StampListDto;
import rednosed.app.dto.response.UserDuplicatedStatusDto;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.rds.UserRepository;
import rednosed.app.repository.rds.UserStampRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserStampRepository userStampRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    //1-1. 사용자 정보 설정(닉네임 중복 확인)
    @Transactional(readOnly = true)
    public UserDuplicatedStatusDto checkUserNickname(UserNicknameDto userNicknameDto) {
        boolean isUserExist = userRepository.findByNickname(userNicknameDto.nickname()).isPresent();

        return UserDuplicatedStatusDto.builder()
                .existedUser(isUserExist)
                .build();
    }

    //1-2. 사용자 정보 설정(닉네임 설정)
    @Transactional
    public void saveUserNickname(User tmpUser, UserNicknameDto userNicknameDto) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.updateUserNickname(userNicknameDto.nickname());
    }

    public StampListDto showUserStampMyPage(User tmpUser) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<StampInfoDto> stampInfoDtoList = userStampRepository.findByUser(user).stream().map(userStamp
                -> StampInfoDto.builder()
                .stampImg(userStamp.getStamp().getStampImgUrl())
                .stampName(userStamp.getStamp().getStampName())
                .build()).toList();

        return StampListDto.builder()
                .stampList(stampInfoDtoList)
                .build();
    }
}
