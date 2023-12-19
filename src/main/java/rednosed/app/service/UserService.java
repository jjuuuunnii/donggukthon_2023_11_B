package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rednosed.app.domain.nosql.Pixel;
import rednosed.app.domain.rds.Canvas;
import rednosed.app.domain.rds.Seal;
import rednosed.app.domain.rds.User;
import rednosed.app.dto.request.UserNicknameDto;
import rednosed.app.dto.request.UserOrderCntDto;
import rednosed.app.dto.response.*;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.nosql.PixelRepository;
import rednosed.app.repository.rds.CanvasRepository;
import rednosed.app.repository.rds.SealRepository;
import rednosed.app.repository.rds.UserRepository;
import rednosed.app.repository.rds.UserStampRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserStampRepository userStampRepository;
    private final SealRepository sealRepository;
    private final CanvasRepository canvasRepository;
    private final PixelRepository pixelRepository;


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

    //2. 마이페이지(내가 만든 우표)
    @Transactional(readOnly = true)
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

    //2-1. 마이페이지(내가 만든 씰)
    @Transactional(readOnly = true)
    public UserMyPageSealInfoDto showUserSealMyPage(User tmpUser) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<Seal> sealList = sealRepository.findByUser(user);
        List<SealInfoDto> sealInfoDtoList = sealList.stream().map(seal -> SealInfoDto.builder()
                .sealImg(seal.getSealImgUrl())
                .sealName(seal.getSealName())
                .likeCnt(0)
                .like(true)
                .build()).toList();

        return UserMyPageSealInfoDto.builder()
                .nickname(user.getNickname())
                .orderCnt(user.getSealOrderCount())
                .sealList(SealListDto.builder()
                        .sealList(sealInfoDtoList)
                        .build())
                .build();
    }

    //3-1. 우표 만들기 전 공유하는 페이지(캔버스 id 돌려주기)
    public CanvasIdDto startUserCanvas(String canvasClientId) {

        List<List<String>> colorMatrix = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 13; j++) {
                List<String> colorRow = new ArrayList<>(Collections.nCopies(13, "#FFFFFF"));
                colorMatrix.add(colorRow);
            }
        }
        Pixel pixel = Pixel.builder()
                .colors(colorMatrix)
                .canvasClientId(canvasClientId)
                .createdAt(LocalDateTime.now())
                .build();
        pixelRepository.save(pixel);

        return CanvasIdDto.builder()
                .canvasId(canvasClientId)
                .build();
    }

    //3. 우표 만들기 전 공유하는 페이지(캔버스 id 돌려주기)
    @Transactional
    public String makeNewCanvas(User tmpUser) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Canvas canvas = Canvas.builder()
                .canvasClientId(UUID.randomUUID().toString())
                .roomMaker(user)
                .createdAt(LocalDateTime.now())
                .build();
        user.updateCanvas(canvas);
        canvasRepository.save(canvas);
        canvasRepository.flush();

        return canvas.getCanvasClientId();
    }

    //6. 주문해
    @Transactional
    public void updateUserOrderCount(User tmpUser, UserOrderCntDto userOrderCntDto) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        int orderCount = userOrderCntDto.orderCnt();
        user.updateOrderCount(++orderCount);
    }
}
