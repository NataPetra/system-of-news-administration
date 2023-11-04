package by.nata.newscommentsservice;

import by.nata.newscommentsservice.cache.config.CacheProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CacheProperties.class)
public class NewsCommentsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsCommentsServiceApplication.class, args);
    }

}
