package com.msig.test.project.payment.model.response;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
     private String transactionId;

    private String orderId;

    private BigDecimal amount;

    private String status;
}
