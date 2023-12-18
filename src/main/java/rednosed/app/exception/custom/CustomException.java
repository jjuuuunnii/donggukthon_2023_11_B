package rednosed.app.exception.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import rednosed.app.dto.type.ErrorCode;


@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private ErrorCode errorCode;
}