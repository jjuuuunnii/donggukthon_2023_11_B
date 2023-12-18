package rednosed.app.dto.stomp;

import lombok.Builder;
import lombok.Getter;

@Builder
public record StompErrorDto (
         String errorCode,
         String message
){
}
