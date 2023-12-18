package rednosed.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rednosed.app.domain.nosql.Pixel;
import rednosed.app.domain.rds.Canvas;
import rednosed.app.domain.rds.User;
import rednosed.app.dto.response.CanvasInfoDto;
import rednosed.app.dto.response.UserCheckRoomMakerDto;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.repository.nosql.PixelRepository;
import rednosed.app.repository.rds.CanvasRepository;
import rednosed.app.repository.rds.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CanvasService {

    private final CanvasRepository canvasRepository;
    private final UserRepository userRepository;
    private final PixelRepository pixelRepository;

    //3-2. 우표 만들기(캔버스 데이터 가져오기)
    public CanvasInfoDto showCanvasInfo(String canvasClientId) {
        Pixel pixel = pixelRepository.findByCanvasClientId(canvasClientId)
                .orElseThrow(() -> new CustomException(ErrorCode.PIXEL_NOT_FOUND));

        List<List<String>> colorMatrix = pixel.getColors();
        if (colorMatrix == null || colorMatrix.isEmpty()) {
            throw new CustomException(ErrorCode.COLOR_NOT_FOUND);
        }

        int leftTime = (int) pixel.getLeftTime();

        return CanvasInfoDto.builder()
                .color(colorMatrix)
                .leftTime(leftTime)
                .build();
    }


    public UserCheckRoomMakerDto checkUserRoomMaker(User tmpUser, String canvasClientId) {
        User user = userRepository.findByUserClientId(tmpUser.getUserClientId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Canvas canvas = canvasRepository.findByCanvasClientId(canvasClientId)
                .orElseThrow(() -> new CustomException(ErrorCode.CANVAS_NOT_FOUND));

        if (canvas.getRoomMaker().equals(user)) {
            return UserCheckRoomMakerDto.builder()
                    .roomMaker(true)
                    .build();
        }

        return UserCheckRoomMakerDto.builder()
                .roomMaker(false)
                .build();
    }
}
