package rednosed.app.security;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import rednosed.app.dto.common.ResponseDto;
import rednosed.app.dto.type.ErrorCode;


import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        ResponseDto responseDto;

        // TokenExpiredException 처리
        // SignatureVerificationException 처리
        if (authException.getCause() instanceof TokenExpiredException || authException.getCause() instanceof SignatureVerificationException) {
            responseDto = new ResponseDto(HttpStatus.BAD_REQUEST,"FALSE", null, ErrorCode.INVALID_TOKEN.getMessage());
        }

        // UsernameNotFoundException 처리
        else if (authException.getCause() instanceof UsernameNotFoundException) {
            responseDto = new ResponseDto(HttpStatus.BAD_REQUEST,"FALSE", null, ErrorCode.USER_NOT_FOUND.getMessage());
        }
        // 기타 예외 처리
        else {
            ErrorCode errorCode = request.getAttribute("exception") == null ?
                    ErrorCode.NOT_FOUND_END_POINT : (ErrorCode) request.getAttribute("exception");
            responseDto = new ResponseDto(HttpStatus.BAD_REQUEST, "FALSE", null, errorCode.getMessage());
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONValue.toJSONString(responseDto));
        log.error("error = {}", (Object) authException.getStackTrace());
    }
}
