package rednosed.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import rednosed.app.dto.common.ResponseDto;
import rednosed.app.dto.request.LikeDto;
import rednosed.app.dto.request.StampNameDto;
import rednosed.app.security.oauth.info.PrincipalDetails;
import rednosed.app.service.StampService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stamp")
public class StampController {

    private final StampService stampService;

    //2-2. 마이페이지(우표 싱글)
    @GetMapping("/{stampId}/details")
    public ResponseDto<?> showStampSingle(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable String stampId
    ) {
        return ResponseDto.ok(stampService.showStampSingle(principalDetails.getUser(),stampId));
    }

    //2-3. 마이페이지 (내가 만든 우표 목록)
    @GetMapping("/stamp-list")
    public ResponseDto<?> showUserStampList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseDto.ok(stampService.showUserStampList(principalDetails.getUser()));
    }

    //3-5. 우표 만들기(만들기 버튼을 누르고 파일이 넘어올 떄)
    @PostMapping("/new-stamp")
    public ResponseDto<?> makeNewStamp(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody StampNameDto stampNameDto
    ) throws IOException {
        return ResponseDto.ok(stampService.makeNewStamp(principalDetails.getUser(), stampNameDto));
    }

    //3-6. 우표 이름, 사진 요청
    @GetMapping("/{stampClientId}/stamp-info")
    public ResponseDto<?> showNewStampInfo(
            @PathVariable String stampClientId
    ) {
        return ResponseDto.ok(stampService.showNewStampInfo(stampClientId));
    }

    //4. 씰 만들기(필터링: 좋아요 한 우표)
    @GetMapping("/like-list")
    public ResponseDto<?> showLikeStampList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseDto.ok(stampService.showLikeStampList(principalDetails.getUser()));
    }

    //4-1. 씰 만들기(필터링: 전체우표)
    @GetMapping("/all-list")
    public ResponseDto<?> showStampAllList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseDto.ok(stampService.showStampAllList(principalDetails.getUser()));
    }

    //4-3. 우표 좋아요 누르기
    @PutMapping("/{stampClientId}/like")
    public ResponseDto<?> putStampLike(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody LikeDto likeDto,
            @PathVariable String stampClientId
            ) {
        stampService.putStampLike(principalDetails.getUser(),stampClientId, likeDto);
        return ResponseDto.ok(null);
    }
}
