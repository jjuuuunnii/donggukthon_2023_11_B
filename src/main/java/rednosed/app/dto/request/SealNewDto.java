package rednosed.app.dto.request;

import jakarta.validation.constraints.NotNull;

import java.io.File;

public record SealNewDto(

        @NotNull(message = "씰의 이름은 null이 될 수 없습니다.")
        String name,

        @NotNull(message = "씰의 파일은 null이 될 수 없습니다.")
        File seal
) {
}
