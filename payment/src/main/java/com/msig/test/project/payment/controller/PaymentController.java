package com.msig.test.project.payment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.msig.test.project.payment.model.request.CallbackRequest;
import com.msig.test.project.payment.model.request.CreatePaymentRequest;
import com.msig.test.project.payment.model.response.PaymentResponse;
import com.msig.test.project.payment.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
@Tag(
  name = "Payment API",
  description = "Payment Management API"
)
public class PaymentController {

  @Autowired
  private PaymentService service;

  @Operation(
    summary = "Create Payment"
  )
  @PostMapping
  public PaymentResponse createPayment(
    @Valid @RequestBody CreatePaymentRequest request
  ) {

    return service.createPayment(
      request
    );
  }
  @Operation(
    summary =
    "Payment Gateway Callback"
  )
  @PostMapping("/callback")
  public String callback(
    @Valid @RequestBody CallbackRequest request) {
    return service.callback(request);
  }
}