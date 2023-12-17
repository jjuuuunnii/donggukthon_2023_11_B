package rednosed.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserNicknameDto(

        @NotNull(message = "nickname은 null이 될 수 없습니다.")
        String nickname
){}
