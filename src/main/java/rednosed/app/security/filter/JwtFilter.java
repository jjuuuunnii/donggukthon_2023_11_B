package rednosed.app.security.filter;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import rednosed.app.contrant.Constants;
import rednosed.app.domain.rds.User;
import rednosed.app.repository.rds.UserRepository;
import rednosed.app.security.oauth.info.PrincipalDetails;
import rednosed.app.security.util.JwtUtil;


import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final AntPathMatcher antPathMatcher;
    private final GrantedAuthoritiesMapper authoritiesMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (isPathExcluded(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        processTokenAuthentication(request, response);
        filterChain.doFilter(request, response);
    }

    private boolean isPathExcluded(String path) {
        return Constants.NO_NEED_AUTH_URLS.stream()
                .anyMatch(url -> antPathMatcher.match(url, path));
    }

    private void processTokenAuthentication(HttpServletRequest request, HttpServletResponse response) {
        try {
            String refreshToken = jwtUtil.extractRefreshToken(request)
                    .filter(jwtUtil::isTokenValid)
                    .orElse(null);

            if (refreshToken != null) {
                reIssueAccessToken(refreshToken, response);
            } else {
                authenticateWithAccessToken(request);
            }
        } catch (SignatureVerificationException | TokenExpiredException e) {
            log.error("토큰 검증 실패: " + e.getMessage());
            throw new BadCredentialsException("토큰 인증 실패", e);
        }
    }


    private void reIssueAccessToken(String refreshToken, HttpServletResponse response) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String newAccessToken = jwtUtil.createAccessToken(user.getSocialId());
                    jwtUtil.sendAccessToken(response, newAccessToken);
                    saveAuthentication(user);
                });
    }

    private void authenticateWithAccessToken(HttpServletRequest request) {
        jwtUtil.extractAccessToken(request)
                .ifPresent(accessToken -> {
                    try {
                        String userClientId = jwtUtil.extractUserClientId(accessToken);
                        userRepository.findByUserClientId(userClientId).ifPresent(this::saveAuthentication);
                    } catch (TokenExpiredException | SignatureVerificationException e) {
                        log.error("인증 실패: " + e.getMessage());
                        throw new BadCredentialsException("토큰 검증 실패", e);
                    }
                });
    }


    private void saveAuthentication(User user) {
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails, null, authoritiesMapper.mapAuthorities(principalDetails.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
