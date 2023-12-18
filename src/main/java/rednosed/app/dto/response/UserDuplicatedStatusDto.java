package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserDuplicatedStatusDto(
        @NotNull(message = "닉네임 중복 상태값은 필수입니다")
        boolean existedUser
)
{}
