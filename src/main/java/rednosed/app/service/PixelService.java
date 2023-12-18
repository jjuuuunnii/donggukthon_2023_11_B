package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rednosed.app.domain.nosql.Pixel;
import rednosed.app.dto.request.PixelInfoDto;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.nosql.PixelRepository;

@Service
@RequiredArgsConstructor
public class PixelService {

    private final PixelRepository pixelRepository;

    public void saveCanvasData(String canvasClientId, PixelInfoDto pixelInfoDto) {
        Pixel pixel = pixelRepository.findByCanvasClientId(canvasClientId)
                .orElseThrow(() -> new CustomException(ErrorCode.PIXEL_NOT_FOUND));
        pixel.updateColor(pixelInfoDto.xCoordinate(), pixelInfoDto.yCoordinate(), pixelInfoDto.color());
    }
}
