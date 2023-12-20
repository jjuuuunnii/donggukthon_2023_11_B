package rednosed.app.security;

import com.auth0.jwt.exceptions.TokenExpiredException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import rednosed.app.dto.type.ErrorCode;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        Map<String, Object> result = new HashMap<>();
        HttpStatus status;
        log.info("ERROR CAUSE = {} ", authException.getMessage());
        if (authException.getCause() instanceof InsufficientAuthenticationException) {
            log.error("FilterException throw MalformedJwtException Exception");
            setResponseMap(result,ErrorCode.TOKEN_MALFORMED_ERROR.getMessage());
            status = ErrorCode.TOKEN_MALFORMED_ERROR.getHttpStatus();
        } else if (authException.getCause() instanceof IllegalArgumentException) {
            log.error("FilterException throw IllegalArgumentException Exception");
            setResponseMap(result,ErrorCode.TOKEN_TYPE_ERROR.getMessage());
            status = ErrorCode.TOKEN_TYPE_ERROR.getHttpStatus();

        } else if (authException.getCause() instanceof TokenExpiredException){
            log.error("FilterException throw ExpiredJwtException Exception");
            setResponseMap(result,ErrorCode.EXPIRED_TOKEN_ERROR.getMessage());
            status = ErrorCode.EXPIRED_TOKEN_ERROR.getHttpStatus();

        } else if (authException.getCause() instanceof UnsupportedJwtException) {
            log.error("FilterException throw UnsupportedJwtException Exception");
            setResponseMap(result,ErrorCode.TOKEN_UNSUPPORTED_ERROR.getMessage());
            status = ErrorCode.TOKEN_UNSUPPORTED_ERROR.getHttpStatus();

        } else if (authException.getCause() instanceof JwtException) {
            log.error("FilterException throw JwtException Exception");
            setResponseMap(result,ErrorCode.TOKEN_UNKNOWN_ERROR.getMessage());
            status = ErrorCode.TOKEN_UNKNOWN_ERROR.getHttpStatus();

        } else {
            log.error("FilterException throw Exception Exception");
            setResponseMap(result,ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
            status = ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus();
        }

        sendResponse(response, result, status);
        log.error("Authentication error: {}", authException.getMessage(), authException);
    }

    private void setResponseMap(Map<String, Object> map, String message) {
        map.put("status", "NONE");
        map.put("result", null);
        map.put("message", message);
    }

    private void sendResponse(HttpServletResponse response, Map<String, Object> result, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONValue.toJSONString(result));
    }
}

