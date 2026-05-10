package com.msig.project.test.order.model.response;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderResponse {
    private String orderId;

    private String policyNumber;

    private String customerName;

    private BigDecimal amount;

    private String status;
}
