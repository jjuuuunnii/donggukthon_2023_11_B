package com.cafevery.security.handler.signin;


import com.cafevery.dto.response.JwtTokenDto;
import com.cafevery.dto.type.ERole;
import com.cafevery.repository.UserRepository;
import com.cafevery.security.info.CustomOAuth2User;
import com.cafevery.utility.CookieUtil;
import com.cafevery.utility.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        JwtTokenDto tokenDto = jwtUtil.generateTokens(customOAuth2User.getId(), ERole.USER);
        userRepository.updateRefreshTokenAndLoginStatus(customOAuth2User.getId(), tokenDto.refreshToken());

        setSuccessWebResponse(response, tokenDto);
    }

    private void setSuccessWebResponse(HttpServletResponse response, JwtTokenDto tokenDto) throws IOException {
        CookieUtil.addCookie(response, "access_token", tokenDto.accessToken());
        CookieUtil.addSecureCookie(response, "refresh_token", tokenDto.refreshToken(), jwtUtil.getRefreshTokenExpirePeriod());

        response.sendRedirect("http://localhost:3000");
    }
}
