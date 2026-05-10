# Payment System Microservices

## Overview

This project is a simple payment system built using a microservices architecture.

The system consists of:

* Order Service
* Payment Service
* Notification Service
* RabbitMQ
* PostgreSQL

This project was created as a backend technical test simulation for an insurance company.

---

# Architecture

```text
Order Service
    |
    | Create Order
    v
Payment Service
    |
    | Payment Callback
    v
RabbitMQ
    |
    v
Notification Service
    |
    v
Send Email Notification
```

---

# Tech Stack

| Technology  | Description             |
| ----------- | ----------------------- |
| Java 21     | Backend Language        |
| Spring Boot | Microservices Framework |
| PostgreSQL  | Database                |
| RabbitMQ    | Message Broker          |
| Docker      | Containerization        |
| Maven       | Dependency Management   |
| Mail SMTP   | Email Notification      |

---

# Features

## Order Service

* Create insurance order
* Get order by order ID
* Update order status
* Publish order event to RabbitMQ

---

## Payment Service

* Create payment transaction
* Validate order
* Prevent duplicate payment
* Handle payment callback
* Prevent duplicate callback
* Prevent double charge
* Update order status automatically
* Publish payment success event to RabbitMQ

---

## Notification Service

* Consume order queue
* Consume payment queue
* Send email notification asynchronously

---

# Microservices

| Service              | Port  |
| -------------------- | ----- |
| Order Service        | 8080  |
| Payment Service      | 8081  |
| Notification Service | 8082  |
| PostgreSQL           | 5432  |
| RabbitMQ             | 5672  |
| RabbitMQ Dashboard   | 15672 |

---

# Database

Database Name:

```text
payment_system
```

---

# Database Tables

## orders

```sql
CREATE TABLE orders (

    id BIGSERIAL PRIMARY KEY,

    order_id VARCHAR(100) UNIQUE NOT NULL,

    policy_number VARCHAR(100) NOT NULL,

    customer_name VARCHAR(255) NOT NULL,

    amount NUMERIC(15,2) NOT NULL,

    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);
```

---

## payments

```sql
CREATE TABLE payments (

    id BIGSERIAL PRIMARY KEY,

    transaction_id VARCHAR(100) UNIQUE NOT NULL,

    order_id VARCHAR(100) NOT NULL,

    amount NUMERIC(15,2) NOT NULL,

    status VARCHAR(50) NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);
```

---

# Order Status

| Status          | Description         |
| --------------- | ------------------- |
| PENDING_PAYMENT | Waiting for payment |
| PAID            | Payment success     |
| FAILED          | Payment failed      |

---

# Payment Status

| Status  | Description      |
| ------- | ---------------- |
| PENDING | Waiting callback |
| SUCCESS | Payment success  |
| FAILED  | Payment failed   |

---

# RabbitMQ Queues

| Queue         | Description           |
| ------------- | --------------------- |
| order.queue   | Order created event   |
| payment.queue | Payment success event |

---

# Duplicate Callback Handling

This project implements idempotency handling to prevent duplicate payment processing.

Example:

```java
if ("SUCCESS".equals(payment.getStatus())) {

    log.warn(
        "DUPLICATE CALLBACK DETECTED : {}",
        payment.getTransactionId()
    );

    return "CALLBACK ALREADY PROCESSED";
}
```

This prevents:

* Double charge
* Duplicate transaction update
* Duplicate notification

---

# Docker Setup

## docker-compose.yml

```yaml
services:

  postgres:
    image: postgres:16

  rabbitmq:
    image: rabbitmq:3-management

  order-service:
    build: ./order

  payment-service:
    build: ./payment

  notification-service:
    build: ./notification
```

---

# Run Project

## 1. Build Project

Run this command inside each microservice:

```bash
mvn clean package -DskipTests
```

---

## 2. Run Docker Compose

```bash
docker compose up --build
```

---

# RabbitMQ Dashboard

```text
http://localhost:15672
```

## Login

```text
username : admin
password : admin
```

---

# API Endpoints Documentation & Swagger

This project uses Swagger / OpenAPI for interactive API documentation and testing.

Swagger provides:

* Interactive API testing
* Request/Response documentation
* API contract visibility
* Faster backend integration
* Easier microservice debugging

---

# Swagger Documentation

Swagger UI is available for API testing and documentation.

## Swagger UI Access

| Service              | URL                                                                            |
| -------------------- | ------------------------------------------------------------------------------ |
| Order Service        | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |
| Payment Service      | [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html) |


---

## Swagger Dependency

Add this dependency to each microservice:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.9</version>
</dependency>
```

---

## OpenAPI Configuration Example

```java
package com.msig.project.test.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("Payment System API")
                                .version("1.0")
                                .description(
                                        "Microservices Payment System API Documentation"
                                )
                );
    }
}
```

---

# API Documentation

# Order Service

## Create Order

### Request

```http
POST /orders
```

### Body

```json
{
  "policyNumber": "POL-001",
  "customerName": "John Doe",
  "amount": 500000
}
```

### Response

```json
{
  "orderId": "ORD-123456",
  "policyNumber": "POL-001",
  "customerName": "John Doe",
  "amount": 500000,
  "status": "PENDING_PAYMENT"
}
```

---

## Get Order

### Request

```http
GET /orders/{orderId}
```

---

# Payment Service

## Create Payment

### Request

```http
POST /payments
```

### Body

```json
{
  "orderId": "ORD-123456",
  "amount": 500000
}
```

### Response

```json
{
  "transactionId": "TX-123456",
  "orderId": "ORD-123456",
  "amount": 500000,
  "status": "PENDING"
}
```

---

## Payment Callback

### Request

```http
POST /payments/callback
```

### Body

```json
{
  "transactionId": "TX-123456",
  "status": "SUCCESS"
}
```

### Response

```json
{
  "message": "Payment success"
}
```

---

# Notification Service

## Notification Flow

* Consume `order.queue`
* Consume `payment.queue`
* Send email notification

---

# Email Notification

This project uses Gmail SMTP for email notification.

Example configuration:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
```

---

# Important Notes

## Local Development

Use:

```text
localhost
```

Example:

```text
http://localhost:8080/orders
```

---

## Docker Environment

Use service name:

```text
http://order-service:8080/orders
```

---

# Example End-to-End Flow

## 1. Create Order

```text
Order status = PENDING_PAYMENT
```

---

## 2. Create Payment

```text
Payment status = PENDING
```

---

## 3. Payment Callback SUCCESS

```text
Payment status = SUCCESS
Order status = PAID
```

---

## 4. RabbitMQ Event Published

```text
PAYMENT SUCCESS : ORD-123456
```

---

## 5. Notification Service Consume Event

```text
EMAIL SUCCESSFULLY SENT
```

---

# Future Improvements

* JWT Authentication
* API Gateway
* Retry Mechanism
* Dead Letter Queue
* Distributed Tracing
* Centralized Logging
* Kubernetes Deployment
* CI/CD Pipeline
* Unit Testing
* Integration Testing

---

# Author

Wahyu Aji Saputro

Software Engineer | Java Backend Developer | Flutter Developer
