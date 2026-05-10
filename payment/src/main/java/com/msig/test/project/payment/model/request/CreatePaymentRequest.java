package com.msig.test.project.payment.model.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import lombok.Data;

@Data
public class CreatePaymentRequest {
    @NotBlank(
            message = "Order ID is required"
    )
    private String orderId;

    @NotNull(
            message = "Amount is required"
    )
    @DecimalMin(
            value = "1",
            message =
                    "Amount must be greater than 0"
    )
    private BigDecimal amount;

}
