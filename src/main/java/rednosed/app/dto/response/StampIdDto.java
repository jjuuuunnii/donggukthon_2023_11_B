package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record StampIdDto (

        @NotNull(message = "스템프 id는 null이 될 수 없습니다.")
        String stampId
){

}
