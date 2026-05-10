package com.msig.test.project.payment.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.msig.test.project.payment.config.RabbitMQConfig;

@Service
public class PaymentProducer {

  private static final Logger log =
    LoggerFactory.getLogger(
      PaymentProducer.class
    );

  private final RabbitTemplate rabbitTemplate;

  public PaymentProducer(
    RabbitTemplate rabbitTemplate
  ) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void sendMessage(
    String message
  ) {

    log.info(
      "SEND MESSAGE TO RABBITMQ : {}",
      message
    );

    rabbitTemplate.convertAndSend(
      RabbitMQConfig.PAYMENT_QUEUE,
      message
    );
  }
}