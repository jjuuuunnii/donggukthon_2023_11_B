package rednosed.app.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rednosed.app.dto.common.ResponseDto;
import rednosed.app.dto.request.SealNewDto;
import rednosed.app.security.oauth.info.PrincipalDetails;
import rednosed.app.service.SealService;

import java.io.IOException;

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

    //4-2. 씰 만들기(만들기)
    @PostMapping("/new-seal")
    public ResponseDto<?> makeNewSeal(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @ModelAttribute SealNewDto sealNewDto
            ) throws IOException {
        sealService.makeNewSeal(principalDetails.getUser(), sealNewDto);
        return ResponseDto.ok(null);
    }

    //5. 씰 게시판
    @GetMapping("/all-list")
    public ResponseDto<?> showSealAllList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseDto.ok(sealService.showSealAllList(principalDetails.getUser().getUserClientId()));
    }
}
