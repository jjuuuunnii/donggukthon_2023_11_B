package rednosed.app.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rednosed.app.dto.common.ResponseDto;
import rednosed.app.security.oauth.info.PrincipalDetails;
import rednosed.app.service.SealService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seal")
public class SealController {

    private final SealService sealService;

    //2-5. 마이페이지(내가 만든 씰 목록)
    @GetMapping("/seal-list")
    public ResponseDto<?> showUserSealList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseDto.ok(sealService.showUserSealList(principalDetails.getUser()));
    }

}
