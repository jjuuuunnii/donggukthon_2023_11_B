package rednosed.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rednosed.app.dto.common.ResponseDto;
import rednosed.app.security.oauth.info.PrincipalDetails;
import rednosed.app.service.StampService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stamp")
public class StampController {

    private final StampService stampService;


    //2-3. 마이페이지 (내가 만든 우표 목록)
    @GetMapping("/stamp-list")
    public ResponseDto<?> showUserStampList(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        return ResponseDto.ok(stampService.showUserStampList(principalDetails.getUser()));
    }
}
