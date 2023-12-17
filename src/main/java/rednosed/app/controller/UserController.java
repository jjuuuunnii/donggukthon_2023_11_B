package rednosed.app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rednosed.app.dto.common.ResponseDto;
import rednosed.app.dto.request.UserNicknameDto;
import rednosed.app.dto.response.UserDuplicatedStatusDto;
import rednosed.app.security.oauth.info.PrincipalDetails;
import rednosed.app.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    //1-1. 사용자 정보 설정(닉네임 중복 확인)
    @PostMapping("/check-nickname")
    public ResponseDto<?> checkUserNickname(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody @Valid UserNicknameDto userNicknameDto
    ) {

        return ResponseDto.ok(userService.checkUserNickname(principalDetails.getUser(), userNicknameDto));
    }


}
