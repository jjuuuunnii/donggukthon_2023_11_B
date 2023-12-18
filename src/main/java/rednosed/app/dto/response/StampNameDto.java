package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record StampNameDto(

        @NotNull(message = "우표이름은 null이 될 수 없습니다.")
        String stampName,

        @NotNull(message = "우표 url은 null이 될 수 없습니다.")
        String stampImg
) {
}
