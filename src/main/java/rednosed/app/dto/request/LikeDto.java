package rednosed.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record LikeDto(

        @NotNull(message = "좋아요 값은 null이 될 수 없습니다.")
        boolean like
) {
}
