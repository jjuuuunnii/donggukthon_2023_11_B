package rednosed.app.security;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import rednosed.app.dto.common.ResponseDto;
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

        if (authException.getCause() instanceof TokenExpiredException || authException.getCause() instanceof SignatureVerificationException) {
            setResponseMap(result, "FAIL", null, ErrorCode.INVALID_TOKEN.getMessage());
            status = HttpStatus.BAD_REQUEST;
        } else if (authException instanceof BadCredentialsException && authException.getCause() == null) {
            setResponseMap(result, "NONE", null, ErrorCode.INVALID_TOKEN.getMessage());
            status = HttpStatus.UNAUTHORIZED;
        } else if (authException.getCause() instanceof UsernameNotFoundException) {
            setResponseMap(result, "FAIL", null, ErrorCode.USER_NOT_FOUND.getMessage());
            status = HttpStatus.BAD_REQUEST;
        } else {
            ErrorCode errorCode = request.getAttribute("exception") == null ?
                    ErrorCode.NOT_FOUND_END_POINT : (ErrorCode) request.getAttribute("exception");
            setResponseMap(result, "FAIL", null, errorCode.getMessage());
            status = HttpStatus.BAD_REQUEST;
        }
        sendResponse(response, result, status);
        log.error("Authentication error: {}", authException.getMessage(), authException);
    }

    private void setResponseMap(Map<String, Object> map, String status, Object result, String message) {
        map.put("status", status);
        map.put("result", result);
        map.put("message", message);
    }

    private void sendResponse(HttpServletResponse response, Map<String, Object> result, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JSONValue.toJSONString(result));
    }
}

