package com.msig.project.test.order.model.request;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


import lombok.Data;

@Data
public class CreateOrderRequest {
     @NotBlank(message = "Policy number is required")
    private String policyNumber;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotNull(message = "Amount is required")
    @DecimalMin(
            value = "1",
            message = "Amount must be greater than 0"
    )
    private BigDecimal amount;
}
