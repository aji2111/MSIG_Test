package com.msig.project.test.order.producer;

import com.msig.project.test.order.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {
       private static final Logger log =
            LoggerFactory.getLogger(
                    OrderProducer.class
            );

    private final RabbitTemplate rabbitTemplate;

    public OrderProducer(
            RabbitTemplate rabbitTemplate
    ) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(String message) {

        log.info(
                "SEND MESSAGE TO RABBITMQ : {}",
                message
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_QUEUE,
                message
        );
    }
}
