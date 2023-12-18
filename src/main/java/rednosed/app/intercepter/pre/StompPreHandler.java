package rednosed.app.intercepter.pre;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import rednosed.app.domain.rds.User;
import rednosed.app.dto.type.ErrorCode;
import rednosed.app.exception.custom.CustomException;
import rednosed.app.security.oauth.info.PrincipalDetails;
import rednosed.app.security.util.JwtUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class StompPreHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    public StompPreHandler(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        try {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

            /** TODO 테스트 데이터 지우기 **/
            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
                PrincipalDetails testPrincipal = createTestPrincipalDetails();
                Authentication testAuthentication = new UsernamePasswordAuthenticationToken(
                        testPrincipal, null, testPrincipal.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(testAuthentication);
                return message;
            }

            if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");

                if (authorizationHeaders == null || authorizationHeaders.isEmpty()) {
                    log.error("Authorization header is missing");
                    throw new CustomException(ErrorCode.INVALID_TOKEN);
                }

                String token = authorizationHeaders.get(0).replace("Bearer ", "");

                if (!jwtUtil.isTokenValid(token)) {
                    log.error("Invalid JWT token");
                    throw new CustomException(ErrorCode.UNAUTHORIZED_USER);
                }
            }
        } catch (CustomException e) {
            StompHeaderAccessor errorAccessor = StompHeaderAccessor.create(StompCommand.ERROR);

            Map<String, Object> result = new HashMap<>();
            result.put("status", "FAILED");
            result.put("result", null);
            result.put("message", e.getErrorCode().getMessage());
            String jsonError = convertToJson(result);

            return new ErrorMessage(
                    new Throwable(jsonError),
                    errorAccessor.getMessageHeaders()
            );
        }
        return message;
    }

    private String convertToJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Error in JSON conversion", e);
            return "{}"; // 오류 발생시 기본 빈 JSON 반환
        }
    }

    /**
     * TODO 테스트 데이터
     * @return
     */
    /* 테스트를 위한 데이터 */
    private PrincipalDetails createTestPrincipalDetails() {
        return new PrincipalDetails(User.builder()
                .userClientId("testClientId")
                .nickname("test")
                .refreshToken("test")
                .socialId("testId")
                .canvas(null)
                .createdAt(LocalDateTime.now())
                .build());
    }
}
