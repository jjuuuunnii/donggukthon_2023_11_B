package rednosed.app.security.handler;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import rednosed.app.domain.rds.User;
import rednosed.app.repository.rds.UserRepository;
import rednosed.app.security.oauth.info.CustomOAuth2user;
import rednosed.app.security.oauth.info.PrincipalDetails;
import rednosed.app.security.util.JwtUtil;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        log.info("로그인 성공 토큰 세팅에 들어갑니다");

        CustomOAuth2user customOAuth2user = (CustomOAuth2user) authentication.getPrincipal();
        String userClientId = customOAuth2user.getUserClientId();
        User user = userRepository.findByUserClientId(userClientId).orElseThrow(() -> new UsernameNotFoundException("유저를 찾지 못했습니다"));
        userRepository.findByUserClientId(userClientId).orElseThrow(() -> new UsernameNotFoundException("유저를 찾지 못했습니다"));
        PrincipalDetails principalDetails = new PrincipalDetails(user);


        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(principalDetails, null,
                        authoritiesMapper.mapAuthorities(principalDetails.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        String accessToken = jwtUtil.createAccessToken(user.getUserClientId());
        String refreshToken = jwtUtil.createRefreshToken();

        jwtUtil.updateRefreshToken(user, refreshToken);

        Cookie accessCookie = new Cookie("accessCookie", accessToken);
        Cookie refreshCookie = new Cookie("refreshCookie", refreshToken);

        accessCookie.setPath("/");
        refreshCookie.setPath("/");

        refreshCookie.setSecure(true);
        refreshCookie.setHttpOnly(true);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        response.sendRedirect("https://localhost:3000/mypage");
    }
}
