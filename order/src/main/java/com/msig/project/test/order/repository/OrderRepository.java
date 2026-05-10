package com.msig.project.test.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msig.project.test.order.model.Order;

public interface  OrderRepository extends JpaRepository<Order, Long>  {
      Optional<Order> findByOrderId(String orderId);
      
}
