package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import javax.annotation.Nullable;

@Builder
public record UserMyPageStampInfoDto(
        @NotNull(message = "유저의 닉네임은 null이 될 수 없습니다")
        String nickname,

        @Nullable
        StampListDto stampList
) {
}
