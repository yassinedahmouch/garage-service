package com.renault.garage.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MessagingProperties.class)
@RequiredArgsConstructor
@Slf4j
public class MessagingConfig {

    private final MessagingProperties messagingProperties;

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.setAutoStartup(true);
        return admin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        rabbitTemplate.setMandatory(true);
        return rabbitTemplate;
    }

    @Bean
    public TopicExchange vehicleExchange() {
        return ExchangeBuilder.topicExchange(messagingProperties.getExchange()).durable(true).build();
    }

    @Bean
    public Queue vehicleQueue() {
        return QueueBuilder.durable(messagingProperties.getVehicleQueueName()).build();
    }

    @Bean
    public Binding vehicleBinding(Queue vehicleQueue, TopicExchange vehicleExchange) {
        return BindingBuilder.bind(vehicleQueue).to(vehicleExchange).with(messagingProperties.getVehicleRoutingKey());
    }
}
