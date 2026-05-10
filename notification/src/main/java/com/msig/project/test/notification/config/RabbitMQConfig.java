package com.msig.project.test.notification.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_QUEUE =
            "order.queue";

    public static final String PAYMENT_QUEUE =
            "payment.queue";

    @Bean
    public Queue orderQueue() {

        return new Queue(
                ORDER_QUEUE
        );
    }

    @Bean
    public Queue paymentQueue() {

        return new Queue(
                PAYMENT_QUEUE
        );
    }
}