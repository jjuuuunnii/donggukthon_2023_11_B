package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SealNewInfoDto(
        @NotNull(message = "씰 이름은 null이 될 수 없습니다.")
        String sealName,

        @NotNull(message = "씰 이미지는 null이 될 수 없습니다.")
        String sealImg
) {
}
