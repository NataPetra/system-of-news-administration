package by.nata.newscommentsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@EntityScan(basePackages = "by.nata.newscommentsservice.database.model")
@SpringBootApplication
public class NewsCommentsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsCommentsServiceApplication.class, args);
	}

}
