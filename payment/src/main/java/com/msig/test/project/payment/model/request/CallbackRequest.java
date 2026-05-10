package com.msig.test.project.payment.model.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CallbackRequest {
      @NotBlank(
            message =
                    "Transaction ID is required"
    )
    private String transactionId;

    @NotBlank(
            message = "Status is required"
    )
    private String status;

}
