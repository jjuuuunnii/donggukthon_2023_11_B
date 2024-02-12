package com.cafevery.security.config;


import com.cafevery.contrant.Constants;
import com.cafevery.security.filter.GlobalLoggerFilter;
import com.cafevery.security.filter.JwtAuthenticationFilter;
import com.cafevery.security.filter.JwtExceptionFilter;
import com.cafevery.security.handler.jwt.JwtAccessDeniedHandler;
import com.cafevery.security.handler.jwt.JwtAuthEntryPoint;
import com.cafevery.security.handler.signin.DefaultFailureHandler;
import com.cafevery.security.handler.signin.DefaultSuccessHandler;
import com.cafevery.security.handler.signin.OAuth2SuccessHandler;
import com.cafevery.security.handler.singout.CustomSignOutResultHandler;
import com.cafevery.security.service.CustomOAuth2UserService;
import com.cafevery.security.service.CustomUserDetailService;
import com.cafevery.utility.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomSignOutResultHandler customSignOutProcessHandler;
    private final CustomSignOutResultHandler customSignOutResultHandler;
    private final DefaultSuccessHandler defaultSuccessHandler;
    private final DefaultFailureHandler defaultFailureHandler;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    private final CustomUserDetailService customUserDetailService;
    private final JwtUtil jwtUtil;

    @Bean
    protected SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(registry ->
                        registry
                                .requestMatchers(Constants.NO_NEED_AUTH_URLS.toArray(String[]::new)).permitAll()
                                .anyRequest().authenticated()
                )
//                .logout(configurer ->
//                        configurer
//                                .logoutUrl("/auth/sign-out")
//                                .addLogoutHandler(customSignOutProcessHandler)
//                                .logoutSuccessHandler(customSignOutResultHandler)
//                )

                .exceptionHandling(configurer ->
                        configurer
                                .authenticationEntryPoint(jwtAuthEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .oauth2Login(config -> config
                        .successHandler(oAuth2SuccessHandler)
                        .failureHandler(defaultFailureHandler)
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)
                        )
                )

                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtUtil, customUserDetailService),
                        LogoutFilter.class)
                .addFilterBefore(
                        new JwtExceptionFilter(),
                        JwtAuthenticationFilter.class)
                .addFilterBefore(
                        new GlobalLoggerFilter(),
                        JwtExceptionFilter.class)

                .getOrBuild();
    }
}
