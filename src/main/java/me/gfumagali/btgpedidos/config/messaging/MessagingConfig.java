package me.gfumagali.btgpedidos.config.messaging;

import me.gfumagali.btgpedidos.model.dto.OrderDTO;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {
    @Value("${application.listener.queue.name}")
    private String queueName;

    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }

    @Bean
    public DefaultClassMapper classMapper() {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setDefaultType(OrderDTO.class);
        return classMapper;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper());
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}