package com.reservas.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String DELAY_EXCHANGE = "delay.exchange";
    public static final String DELAY_QUEUE = "delay.queue";
    public static final String FINAL_EXCHANGE = "reserva.expira.exchange";
    public static final String FINAL_QUEUE = "reserva.expira.queue";
    public static final String ROUTING_KEY = "reserva.expira";

    // Exchange que será usada após o TTL (final)
    @Bean
    public DirectExchange finalExchange() {
        return new DirectExchange(FINAL_EXCHANGE);
    }

    @Bean
    public Queue finalQueue() {
        return QueueBuilder.durable(FINAL_QUEUE).build();
    }

    @Bean
    public Binding finalBinding() {
        return BindingBuilder.bind(finalQueue()).to(finalExchange()).with(ROUTING_KEY);
    }

    // Fila delay com TTL + Dead Letter
    @Bean
    public DirectExchange delayExchange() {
        return new DirectExchange(DELAY_EXCHANGE);
    }

    @Bean
    public Queue delayQueue() {
        return QueueBuilder.durable(DELAY_QUEUE)
                .withArgument("x-dead-letter-exchange", FINAL_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding delayBinding() {
        return BindingBuilder.bind(delayQueue()).to(delayExchange()).with(ROUTING_KEY);
    }
}
