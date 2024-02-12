package rednosed.app.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.CorsFilter;
import rednosed.app.security.CustomAuthenticationEntryPoint;
import rednosed.app.security.filter.JwtFilter;
import rednosed.app.security.handler.OAuth2LoginFailureHandler;
import rednosed.app.security.handler.OAuth2LoginSuccessHandler;
import rednosed.app.security.oauth.service.OAuth2UserService;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final OAuth2UserService oAuth2UserService;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtFilter jwtFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
                .addFilter(corsFilter);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(config -> config
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/oauth2/authorization/kakao", "/login/oauth2/code/kakao/**").permitAll()
                        .requestMatchers("/login/oauth2/code/kakao").permitAll()
                );


        http
                .oauth2Login(config -> config
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(oAuth2UserService))
                );


        http
                .exceptionHandling(config -> config.authenticationEntryPoint(customAuthenticationEntryPoint));

        http
                .addFilterBefore(jwtFilter, LogoutFilter.class);



        return http.build();
    }
}
