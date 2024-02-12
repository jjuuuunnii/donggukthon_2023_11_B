package rednosed.app.dataLoader;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import rednosed.app.domain.rds.User;
import rednosed.app.repository.rds.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
public class InitialDataConfig {

    @Bean
    @Transactional
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            Optional<User> test1ClientId = userRepository.findByUserClientId("test1ClientId");
            Optional<User> test2ClientId = userRepository.findByUserClientId("test2ClientId");

            if (test2ClientId.isPresent() && test1ClientId.isPresent()) {
                return;
            }

            User test1User = User.builder()
                    .userClientId("test1ClientId")
                    .nickname("test1")
                    .refreshToken("test1")
                    .socialId("test1")
                    .canvas(null)
                    .role("GUEST")
                    .createdAt(LocalDateTime.now())
                    .build();

            User test2User = User.builder()
                    .userClientId("test2ClientId")
                    .nickname("test2")
                    .refreshToken("test2")
                    .socialId("test2")
                    .canvas(null)
                    .role("USER")
                    .createdAt(LocalDateTime.now())
                    .build();

            userRepository.save(test1User);
            userRepository.save(test2User);
        };
    }
}