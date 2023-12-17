package rednosed.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJpaRepositories(basePackages = "rednosed.app.repository.rds")
@EnableMongoRepositories(basePackages = "rednosed.app.repository.nosql")
public class RedNosedAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedNosedAppApplication.class, args);
	}

}
