package rednosed.app.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONValue;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
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
        ErrorCode errorCode = request.getAttribute("exception") == null ?
                ErrorCode.NOT_FOUND_END_POINT : (ErrorCode) request.getAttribute("exception");
        setResponseMap(result, errorCode.getMessage());

        HttpStatus status = errorCode.getHttpStatus();
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

