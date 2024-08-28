package me.gfumagali.btgpedidos.config.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MongoConfig {
    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Bean
    public MongoClient mongoClient() {
        log.info("Creating mongo client with host: {}, port: {}, username: {}", host, port, username);
        return MongoClients.create("mongodb://" + username + ":" + password + "@" + host + ":" + port);
    }

}
