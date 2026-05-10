package com.msig.test.project.payment.model.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class OrderResponse {
     private String orderId;

    private String policyNumber;

    private String customerName;

    private BigDecimal amount;

    private String status;
}
