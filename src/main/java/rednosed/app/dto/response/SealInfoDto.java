package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record SealInfoDto(

        @NotNull(message = "씰의 id는 null이 될 수 없습니다.")
        String id,

        @NotNull(message = "씰의 이미지는 null이 될 수 없습니다.")
        String sealImg,

        @NotNull(message = "씰의 이름은 null이 될 수 없습니다.")
        String sealName,

        @NotNull(message = "씰의 좋아요 수는 null이 될 수 없습니다.")
        int likeCnt,

        @NotNull(message = "씰의 좋아요 상태는 null이 될 수 없습니다.")
        boolean like
) {
}
