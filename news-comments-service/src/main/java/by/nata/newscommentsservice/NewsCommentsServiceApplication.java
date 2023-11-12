package by.nata.newscommentsservice;

import by.nata.newscommentsservice.cache.config.CacheProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(CacheProperties.class)
public class NewsCommentsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsCommentsServiceApplication.class, args);
    }

}
