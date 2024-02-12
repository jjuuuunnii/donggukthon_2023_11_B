package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SealIdDto(
       @NotNull(message = "씰 아이디는 null이 될 수 없습니다")
       String sealId
) {
}
