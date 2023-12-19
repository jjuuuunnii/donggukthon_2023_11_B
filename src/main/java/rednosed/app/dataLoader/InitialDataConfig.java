//package rednosed.app.dataLoader;
//
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.annotation.Transactional;
//import rednosed.app.domain.rds.User;
//import rednosed.app.repository.rds.UserRepository;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Configuration
//public class InitialDataConfig {
//
//    @Bean
//    @Transactional
//    public CommandLineRunner initData(UserRepository userRepository) {
//        return args -> {
//            User testUser = User.builder()
//                    .userClientId("testClientId")
//                    .nickname("test")
//                    .refreshToken("test")
//                    .socialId("testId")
//                    .canvas(null)
//                    .createdAt(LocalDateTime.now())
//                    .build();
//
//            userRepository.save(testUser);
//        };
//    }
//}