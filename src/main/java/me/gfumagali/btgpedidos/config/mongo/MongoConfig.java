package me.gfumagali.btgpedidos.config.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.gfumagali.btgpedidos.config.properties.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MongoConfig {
    private final MongoProperties mongoProperties;

    @Bean
    public MongoClient mongoClient() {
        log.info("Creating mongo client with host: {}, port: {}, username: {}", mongoProperties.getHost(), mongoProperties.getPort(), mongoProperties.getUsername());
        return MongoClients.create("mongodb://" + mongoProperties.getUsername() + ":" + mongoProperties.getPassword() + "@" + mongoProperties.getHost() + ":" + mongoProperties.getPort());
    }

}
