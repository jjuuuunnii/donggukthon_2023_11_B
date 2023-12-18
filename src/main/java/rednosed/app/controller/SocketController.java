package rednosed.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rednosed.app.dto.request.PixelInfoDto;
import rednosed.app.service.PixelService;

@RestController
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final PixelService pixelService;

    @MessageMapping("/canvas/{canvasId}")
    public void sendCanvasData(
            @DestinationVariable String canvasId,
            @RequestBody PixelInfoDto pixelInfoDto)
    {

    }
}
