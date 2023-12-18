package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CanvasIdDto(

        @NotNull(message = "캔버스 id는 null이 될 수 없습니다")
        String canvasId
) {
}
