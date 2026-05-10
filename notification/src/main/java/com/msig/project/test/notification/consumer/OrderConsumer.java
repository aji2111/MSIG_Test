package com.msig.project.test.notification.consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import org.springframework.stereotype.Service;

import com.msig.project.test.notification.service.NotificationService;
import com.msig.project.test.notification.config.RabbitMQConfig;

@Service
public class OrderConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(
                    OrderConsumer.class
            );

    private final NotificationService
            notificationService;

    public OrderConsumer(
            NotificationService notificationService
    ) {
        this.notificationService =
                notificationService;
    }

    @RabbitListener(
            queues = RabbitMQConfig.ORDER_QUEUE
    )
    public void consume(
            String message
    ) {

        log.info(
                "ORDER MESSAGE RECEIVED : {}",
                message
        );

        notificationService.sendOrderNotification(
                message
        );
    }
}