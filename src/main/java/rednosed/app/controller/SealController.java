package rednosed.app.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rednosed.app.dto.common.ResponseDto;
import rednosed.app.dto.request.LikeDto;
import rednosed.app.dto.request.SealNewDto;
import rednosed.app.security.oauth.info.PrincipalDetails;
import rednosed.app.service.SealService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/seal")
public class SealController {

    private final SealService sealService;

    //2-4. 마이페이지(씰 싱글)
    @GetMapping("/{sealClientId}/details")
    public ResponseDto<?> showSealSingle(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable String sealClientId
    ) {
        return ResponseDto.ok(sealService.showSealSingle(principalDetails.getUser(), sealClientId));
    }

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
        return ResponseDto.ok(sealService.makeNewSeal(principalDetails.getUser(), sealNewDto));
    }

    //5. 씰 게시판
    @GetMapping("/all-list")
    public ResponseDto<?> showSealAllList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseDto.ok(sealService.showSealAllList(principalDetails.getUser().getUserClientId()));
    }

    //5-1. 씰 좋아요 누르기
    @PutMapping("/{sealClientId}/like")
    public ResponseDto<?> putSealLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody LikeDto likeDto,
            @PathVariable String sealClientId
    ) {
        sealService.putSealLike(principalDetails.getUser(), sealClientId, likeDto);
        return ResponseDto.ok(null);
    }
}
