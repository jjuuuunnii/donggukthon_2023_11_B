package rednosed.app.dto.type;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //400
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "로그인에 실패했습니다."),

    //401
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    UNAUTHORIZED_USER(HttpStatus.UNAUTHORIZED, "인증되지 않은 유저입니다"),
    INVALID_PARAMETER_FORMAT(HttpStatus.BAD_REQUEST, "요청에 유효하지 않은 인자 형식입니다."),
    MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "필수 요청 파라미터가 누락 되었습니다."),
    INVALID_PARAMETER_TYPE(HttpStatus.BAD_REQUEST, "파라미터의 타입이 잘못되었습니다."),
    BAD_REQUEST_JSON(HttpStatus.BAD_REQUEST, "잘못된 JSON 형식입니다."),

    //404
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다"),
    STAMP_NOT_FOUND(HttpStatus.NOT_FOUND, "우표를 찾을 수 없습니다"),
    NOT_FOUND_END_POINT(HttpStatus.NOT_FOUND,  "잘못된 엔드 포인트 입니다"),
    CANVAS_NOT_FOUND(HttpStatus.NOT_FOUND, "캔버스를 찾을 수 없습니다"),
    PIXEL_NOT_FOUND(HttpStatus.NOT_FOUND, "픽셀을 찾을 수 없습니다"),
    COLOR_NOT_FOUND(HttpStatus.NOT_FOUND, "색상을 찾을 수 없습니다."),
    EXCEPT_ERROR(HttpStatus.NOT_FOUND, "발견하지 못한 에러입니다"),

    //500
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다."),

    ;
    private HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
