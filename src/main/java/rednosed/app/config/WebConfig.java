package rednosed.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import rednosed.app.contrant.Constants;
import rednosed.app.converter.MultipartFileToFileConverter;
import rednosed.app.intercepter.pre.TestUserInterceptor;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {


    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GrantedAuthoritiesMapper grantedAuthoritiesMapper(){
        return new NullAuthoritiesMapper();
    }

    @Bean
    public Storage storage() {
        return StorageOptions.getDefaultInstance().getService();
    }
    @Bean
    public AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new MultipartFileToFileConverter());
    }


    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(new TestUserInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(Constants.NO_NEED_AUTH_URLS);
    }
}
