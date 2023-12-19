//package rednosed.app.intercepter.pre;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.servlet.HandlerInterceptor;
//import rednosed.app.domain.rds.User;
//import rednosed.app.security.oauth.info.PrincipalDetails;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Slf4j
//public class TestUserInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
//            PrincipalDetails testPrincipal = createTestPrincipalDetails();
//            Authentication testAuthentication = new UsernamePasswordAuthenticationToken(
//                    testPrincipal, null, testPrincipal.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(testAuthentication);
//        }
//        return HandlerInterceptor.super.preHandle(request, response, handler);
//    }
//
//    private PrincipalDetails createTestPrincipalDetails(){
//        return new PrincipalDetails(User.builder()
//                .userClientId("testClientId")
//                .nickname("test")
//                .refreshToken("test")
//                .socialId("testId")
//                .canvas(null)
//                .createdAt(LocalDateTime.now())
//                .build());
//    }
//
//}
