package com.msig.project.test.order.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.msig.project.test.order.model.request.CreateOrderRequest;
import com.msig.project.test.order.model.response.OrderResponse;
import com.msig.project.test.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
@Tag(
  name = "Order API",
  description = "Order Management API"
)
public class OrderController {
  @Autowired
  OrderService service;

  @Operation(summary = "Create Order")
  @PostMapping
  public OrderResponse createOrder(
    @Valid @RequestBody CreateOrderRequest request
  ) {

    return service.createOrder(request);
  }

  @Operation(summary = "Get Order By ID")
  @GetMapping("/{orderId}")
  public OrderResponse getOrder(
    @PathVariable String orderId
  ) {

    return service.getOrderByOrderId(
      orderId
    );
  }
  @Operation(summary = "Update Order Status")
  @PutMapping("/{orderId}/status")
  public String updateStatus(
    @PathVariable String orderId,
    @RequestParam String status
  ) {

    service.updateStatus(
      orderId,
      status
    );

    return "ORDER UPDATED";
  }
}