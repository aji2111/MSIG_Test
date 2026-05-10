package com.msig.project.test.order.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.msig.project.test.order.model.Order;
import com.msig.project.test.order.model.request.CreateOrderRequest;
import com.msig.project.test.order.model.response.OrderResponse;
import com.msig.project.test.order.producer.OrderProducer;
import com.msig.project.test.order.repository.OrderRepository;

@Service
public class OrderService {

    private static final Logger log =
            LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository repository;

    @Autowired
    private OrderProducer producer;

   

    public OrderResponse createOrder(
            CreateOrderRequest request
    ) {

        try {

            log.info(
                    "========== START CREATE PREMIUM ORDER =========="
            );

            Order order = new Order();

            String orderId =
                    "ORD-" +
                    UUID.randomUUID()
                            .toString()
                            .substring(0,8);

            order.setOrderId(orderId);

            order.setPolicyNumber(
                    request.getPolicyNumber()
            );

            order.setCustomerName(
                    request.getCustomerName()
            );

            order.setAmount(
                    request.getAmount()
            );

            order.setStatus(
                    "PENDING_PAYMENT"
            );

            order.setCreatedAt(
                    java.time.LocalDateTime.now()
            );

            repository.save(order);

            log.info(
                    "SUCCESS SAVE ORDER -> ORDER_ID : {}, POLICY_NUMBER : {}, STATUS : {}, AMOUNT : {}, CUSTOMER_NAME : {}",
                    order.getOrderId(),
                    order.getPolicyNumber(),
                    order.getStatus(),
                    order.getAmount(),
                    order.getCustomerName()
            );

            // SEND MESSAGE TO RABBITMQ
            producer.sendMessage(
                    "ORDER CREATED : "
                            + order.getOrderId()
            );

            log.info(
                    "MESSAGE SENT TO RABBITMQ"
            );

            return OrderResponse.builder()
                    .orderId(order.getOrderId())
                    .customerName(order.getCustomerName())
                    .policyNumber(order.getPolicyNumber())
                    .amount(order.getAmount())
                    .status(order.getStatus())
                    .build();

        } catch (Exception e) {

            log.error(
                    "FAILED CREATE PREMIUM ORDER : {}",
                    e.getMessage(),
                    e
            );

            throw new RuntimeException(
                    "Failed create premium order"
            );
        }
    }
    public OrderResponse getOrderByOrderId(
        String orderId
) {

    log.info(
            "GET ORDER BY ORDER ID : {}",
            orderId
    );

    Order order =
            repository
                    .findByOrderId(orderId)
                    .orElseThrow(
                            () ->
                            new RuntimeException(
                                    "Order not found"
                            )
                    );

    return OrderResponse.builder()
            .orderId(order.getOrderId())
            .customerName(order.getCustomerName())
            .policyNumber(order.getPolicyNumber())
            .amount(order.getAmount())
            .status(order.getStatus())
            .build();
}
public void updateStatus(
        String orderId,
        String status
) {

    log.info(
            "UPDATE ORDER STATUS : {} -> {}",
            orderId,
            status
    );

    Order order =
            repository
                    .findByOrderId(orderId)
                    .orElseThrow(
                            () ->
                            new RuntimeException(
                                    "Order not found"
                            )
                    );

    order.setStatus(status);

    repository.save(order);

    log.info(
            "ORDER STATUS UPDATED SUCCESS"
    );
}
}