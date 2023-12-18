package rednosed.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rednosed.app.dto.common.ResponseDto;
import rednosed.app.dto.request.PixelInfoDto;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.service.PixelService;

@RestController
@RequiredArgsConstructor
public class StompController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PixelService pixelService;

    //3-2. 우표만들기(캔버스: 소켓)
    @MessageMapping("/canvas/{canvasId}")
    public void sendCanvasData(
            @DestinationVariable String canvasId,
            @RequestBody PixelInfoDto pixelInfoDto)
    {
        try {
            pixelService.saveCanvasData(canvasId, pixelInfoDto);
            simpMessagingTemplate.convertAndSend("/subscribe/canvas/" + canvasId, ResponseDto.ok(pixelInfoDto));
        } catch (CustomException e) {
            simpMessagingTemplate.convertAndSend("/subscribe/canvas/", ResponseDto.fail(e));
        }
    }

}
