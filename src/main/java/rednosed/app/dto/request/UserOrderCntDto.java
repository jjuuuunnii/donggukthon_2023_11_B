package rednosed.app.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserOrderCntDto(
        @NotNull(message = "주문 수는 null이 될 수 없습니다.")
        int orderCnt
) {
}
