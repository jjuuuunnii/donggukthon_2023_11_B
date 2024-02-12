package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record StampInfoDto(
        @NotNull(message = "우표는 id는 null이 될 수 없습니다.")
        String id,

        @NotNull(message = "우표의 이미지는 null이 될 수 없습니다.")
        String stampImg,

        @NotNull(message = "우표의 이름은 null이 될 수 없습니다.")
        String stampName,

        @NotNull(message = "우표의 좋아요 수는 null이 될 수 없습니다.")
        int likeCnt,

        @NotNull(message = "우표의 좋아요 상태는 null이 될 수 없습니다.")
        boolean like
) {}
