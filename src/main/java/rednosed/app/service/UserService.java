package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rednosed.app.domain.rds.Seal;
import rednosed.app.domain.rds.User;
import rednosed.app.domain.rds.UserStamp;
import rednosed.app.dto.request.UserNicknameDto;
import rednosed.app.dto.response.*;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.rds.SealRepository;
import rednosed.app.repository.rds.UserRepository;
import rednosed.app.repository.rds.UserStampRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserStampRepository userStampRepository;
    private final SealRepository sealRepository;

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

    //2. 마이페이지(우표)
    public UserMyPageStampInfoDto showUserStampMyPage(User tmpUser) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<StampInfoDto> stampInfoDtoList = userStampRepository.findByUser(user).stream().map(userStamp
                -> StampInfoDto.builder()
                .stampImg(userStamp.getStamp().getStampImgUrl())
                .stampName(userStamp.getStamp().getStampName())
                .likeCnt(0)
                .like(true)
                .build()).toList();

        return UserMyPageStampInfoDto.builder()
                .nickname(user.getNickname())
                .stampList(StampListDto.builder()
                        .stampList(stampInfoDtoList)
                        .build())
                .build();
    }

    //2-1. 마이페이지(씰)
    public SealListDto showUserSealMyPage(User tmpUser) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Seal> sealList = sealRepository.findByUser(user);
        List<SealInfoDto> sealInfoDtoList = sealList.stream().map(seal -> SealInfoDto.builder()
                .sealImg(seal.getSealImgUrl())
                .sealName(seal.getSealName())
                .likeCnt(0)
                .like(true)
                .build()).toList();

        return SealListDto.builder()
                .sealList(sealInfoDtoList)
                .build();
    }
}
