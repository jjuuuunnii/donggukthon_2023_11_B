package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserCheckRoomMakerDto(

        @NotNull(message = "")
        boolean roomMaker

) {
}
