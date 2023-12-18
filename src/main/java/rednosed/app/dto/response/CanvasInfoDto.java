package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record CanvasInfoDto (

        @NotNull
        List<List<String>> color,

        @NotNull(message = "남은 시간은 null이 될 수 없습니다.")
        int leftTime
){
}
