package com.msig.project.test.notification.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.msig.project.test.notification.config.RabbitMQConfig;
import com.msig.project.test.notification.service.NotificationService;



@Service
public class PaymentConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(
                    PaymentConsumer.class
            );

    private final NotificationService
            notificationService;

    public PaymentConsumer(
            NotificationService notificationService
    ) {
        this.notificationService =
                notificationService;
    }

    @RabbitListener(
            queues = RabbitMQConfig.PAYMENT_QUEUE
    )
    public void consume(
            String message
    ) {

        log.info(
                "MESSAGE RECEIVED FROM RABBITMQ : {}",
                message
        );

        notificationService.sendPaymentNotification(
                message
        );
    }
}
