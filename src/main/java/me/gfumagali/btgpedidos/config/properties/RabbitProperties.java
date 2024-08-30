package me.gfumagali.btgpedidos.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.rabbitmq")
@Getter
@Setter
@Valid
public class RabbitProperties {
    @NotBlank
    private String host;

    @Positive
    @Max(65535)
    private Integer port;

    @NotBlank
    private String queue;

    private String username;
    private String password;
}
