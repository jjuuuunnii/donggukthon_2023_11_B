package rednosed.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rednosed.app.dto.common.ResponseDto;
import rednosed.app.security.oauth.info.PrincipalDetails;
import rednosed.app.service.CanvasService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/canvas")
public class CanvasController {

    private final CanvasService canvasService;


    //3-2. 우표 만들기(캔버스 데이터 가져오기)
    @GetMapping("/{canvasClientId}/make-stamp")
    public ResponseDto<?> showCanvasInfo(
            @PathVariable String canvasClientId
    )
    {
        return ResponseDto.ok(canvasService.showCanvasInfo(canvasClientId));
    }

    //3-3. 방장인지 아닌지 판단
    @GetMapping("/{canvasClientId}/check-room-maker")
    public ResponseDto<?> checkUserRoomMaker(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable String canvasClientId
    ) {
        return ResponseDto.ok(canvasService.checkUserRoomMaker(principalDetails.getUser(), canvasClientId));
    }


}
