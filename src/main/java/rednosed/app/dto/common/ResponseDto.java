package rednosed.app.dto.common;

import net.minidev.json.annotate.JsonIgnore;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;


public record ResponseDto<T>(@JsonIgnore HttpStatus httpStatus,
                             @NotNull String successStatus,
                             @Nullable T data,
                             @Nullable String message) {

    public static <T> ResponseDto<T> ok(@Nullable final T data) {
        return new ResponseDto<>(HttpStatus.OK,"SUCCESS", data, null);
    }

    public static ResponseDto<Object> fail(final NoHandlerFoundException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST,"FALSE", null, ErrorCode.NOT_FOUND_END_POINT.getMessage());
    }

    public static ResponseDto<Object> fail(final MissingServletRequestParameterException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, "FALSE", null, ErrorCode.MISSING_REQUEST_PARAMETER.getMessage());
    }

    public static ResponseDto<Object> fail(final MethodArgumentTypeMismatchException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST, "FALSE", null, ErrorCode.INVALID_PARAMETER_TYPE.getMessage());
    }


    public static ResponseDto<Object> fail(final MethodArgumentNotValidException e) {
        return new ResponseDto<>(HttpStatus.BAD_REQUEST,"FALSE", null, ErrorCode.INVALID_PARAMETER_FORMAT.getMessage());
    }

    public static ResponseDto<Object> fail(final CustomException e) {
        return new ResponseDto<>(e.getErrorCode().getHttpStatus(), "FALSE", null, e.getErrorCode().getMessage());
    }
}