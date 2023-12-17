package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rednosed.app.domain.rds.User;
import rednosed.app.dto.request.UserNicknameDto;
import rednosed.app.dto.response.UserDuplicatedStatusDto;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.rds.UserRepository;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    //1-1. 사용자 정보 설정(닉네임 중복 확인)
    @Transactional(readOnly = true)
    public UserDuplicatedStatusDto checkUserNickname(User tmpUser, UserNicknameDto userNicknameDto) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        boolean isUserExist = userRepository.findByNickname(userNicknameDto.nickname()).isPresent();

        return UserDuplicatedStatusDto.builder()
                .existedUser(isUserExist)
                .build();
    }
}
