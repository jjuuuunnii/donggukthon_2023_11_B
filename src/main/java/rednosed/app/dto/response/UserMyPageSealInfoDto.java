package rednosed.app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import javax.annotation.Nullable;

@Builder
public record UserMyPageSealInfoDto (

        @NotNull(message = "유저의 닉네임 null이 될 수 없습니다.")
        String nickname,

        @NotNull(message = "주문 수는 null이 될 수 없습니다.")
        int orderCnt,

        @Nullable
        SealListDto sealList
){}
