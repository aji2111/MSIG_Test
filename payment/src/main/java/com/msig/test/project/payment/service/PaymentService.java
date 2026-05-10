package com.msig.test.project.payment.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.msig.test.project.payment.model.Payment;
import com.msig.test.project.payment.model.request.CallbackRequest;
import com.msig.test.project.payment.model.request.CreatePaymentRequest;
import com.msig.test.project.payment.model.response.OrderResponse;
import com.msig.test.project.payment.model.response.PaymentResponse;
import com.msig.test.project.payment.producer.PaymentProducer;
import com.msig.test.project.payment.repository.PaymentRepository;

@Service
public class PaymentService {

  private static final Logger log =
    LoggerFactory.getLogger(
      PaymentService.class
    );

  private static final String ORDER_SERVICE_URL =
    "http://localhost:8080/orders/";
  @Autowired
  private PaymentRepository repository;

  @Autowired
  private PaymentProducer producer;

  @Autowired
  private RestTemplate restTemplate;

  public PaymentResponse createPayment(
    CreatePaymentRequest request
  ) {

    try {

      log.info(
        "========== START CREATE PAYMENT =========="
      );

      String orderUrl =
        ORDER_SERVICE_URL +
        request.getOrderId();

      OrderResponse order =
        restTemplate.getForObject(
          orderUrl,
          OrderResponse.class
        );

      if (order == null) {

        log.error(
          "ORDER NOT FOUND : {}",
          request.getOrderId()
        );

        throw new RuntimeException(
          "Order not found"
        );
      }

      // PREVENT DUPLICATE PAYMENT
      if ("PAID".equals(order.getStatus())) {

        log.warn(
          "ORDER ALREADY PAID : {}",
          order.getOrderId()
        );

        throw new RuntimeException(
          "Order already paid"
        );
      }

      Payment payment = new Payment();

      String transactionId =
        "TX-" +
        UUID.randomUUID()
        .toString()
        .substring(0, 8);

      payment.setTransactionId(
        transactionId
      );

      payment.setOrderId(
        request.getOrderId()
      );

      payment.setAmount(
        request.getAmount()
      );

      payment.setStatus(
        "PENDING"
      );

      repository.save(payment);

      log.info(
        "PAYMENT CREATED SUCCESS -> TRANSACTION_ID : {}",
        payment.getTransactionId()
      );

      return PaymentResponse.builder()
        .transactionId(
          payment.getTransactionId()
        )
        .orderId(
          payment.getOrderId()
        )
        .amount(
          payment.getAmount()
        )
        .status(
          payment.getStatus()
        )
        .build();

    } catch (RuntimeException e) {

      log.error(
        "FAILED CREATE PAYMENT : {}",
        e.getMessage(),
        e
      );

      throw new RuntimeException(
        "Failed create payment"
      );
    }
  }

  // DUPLICATE CALLBACK HANDLING

  public String callback(
    CallbackRequest request
  ) {

    try {

      log.info(
        "========== START PAYMENT CALLBACK =========="
      );

      Payment payment =
        repository
        .findByTransactionId(
          request.getTransactionId()
        )
        .orElseThrow(
          () -> new RuntimeException(
            "Payment not found"
          )
        );

      // IDEMPOTENCY
      // PREVENT DUPLICATE CALLBACK

      if ("SUCCESS".equals(
          payment.getStatus()
        )) {

        log.warn(
          "DUPLICATE CALLBACK DETECTED : {}",
          payment.getTransactionId()
        );

        return "CALLBACK ALREADY PROCESSED";
      }

      payment.setStatus(
        request.getStatus()
      );

      repository.save(payment);

      log.info(
        "PAYMENT STATUS UPDATED : {}",
        payment.getStatus()
      );

      // UPDATE ORDER STATUS

      String updateOrderUrl =
        ORDER_SERVICE_URL +
        payment.getOrderId() +
        "/status?status=PAID";

      restTemplate.put(
        updateOrderUrl,
        null
      );

      log.info(
        "ORDER STATUS UPDATED TO PAID : {}",
        payment.getOrderId()
      );

      // SEND EVENT TO RABBITMQ

      producer.sendMessage(
        "PAYMENT SUCCESS : " +
        payment.getOrderId()
      );

      log.info(
        "MESSAGE SENT TO RABBITMQ"
      );

      return "SUCCESS";

    } catch (RestClientException e) {

      log.error(
        "FAILED PAYMENT CALLBACK : {}",
        e.getMessage(),
        e
      );

      throw new RuntimeException(
        "Failed payment callback"
      );
    }
  }
}