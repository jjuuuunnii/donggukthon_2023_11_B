package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserCheckRoomMakerDto(

        @NotNull(message = "룸메이커는 null이 될 수 없습니다.")
        boolean roomMaker

) {}
