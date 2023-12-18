package rednosed.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record PixelInfoDto(

        @NotNull(message = "x좌표는 null이 될 수 없습니다.")
        int xCoordinate,

        @NotNull(message = "y좌표는 null이 될 수 없습니다.")
        int yCoordinate,

        @NotNull(message = "color 값은 null이 될 수 없습니다.")
        String color
) {
}
