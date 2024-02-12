package rednosed.app.dto.response;

import lombok.Builder;

import javax.annotation.Nullable;
import java.util.List;

@Builder
public record StampListDto(

        @Nullable
        List<StampInfoDto> stampList

){}
