package rednosed.app.dto.request;

import jakarta.validation.constraints.NotNull;

import java.io.File;

public record StampNewDto(

        @NotNull(message = "우표의 이름은 null이 될 수 없습니다.")
        String name,

        @NotNull(message = "우표의 파일 데이터는 null이 될 수 없습니다.")
        File stamp
) {}
