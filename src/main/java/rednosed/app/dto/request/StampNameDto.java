package rednosed.app.dto.request;

import jakarta.validation.constraints.NotNull;

import java.io.File;

public record StampNameDto(

        @NotNull(message = "우표의 이름은 null이 될 수 없습니다.")
        String stampName
) {}
