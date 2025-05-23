# 🗂️ Task Manager Microservices App

This is a **Task Manager** application developed using **Spring Boot** and a **microservices architecture**. It provides features such as user registration, login with OTP verification, task creation and management, and administrative controls. The system is secured with **JWT authentication**, monitored via **Spring Actuator**, and documented using **Swagger**.

---

## 🧩 Microservices Overview

| Service          | Port | Description                                                                 |
|------------------|------|-----------------------------------------------------------------------------|
| `registryservice`| 8761 | Eureka Discovery Server for service registration and monitoring             |
| `gatewayservice` | 8080 | API Gateway to route requests, validate JWT tokens                          |
| `userservice`    | 8081 | Handles user registration, OTP login, authentication, and notifications     |
| `taskservice`    | 8082 | Manages task-related operations like create, retrieve, update, delete.|
|||Supports task filtering, sorting, and pagination.                                           |

---

## 🧱 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Cloud Gateway
- Spring Data JPA
- Eureka Discovery
- MySQL
- JWT
- Swagger
- Prometheus + Actuator (for metrics)
- SMTP (Gmail) for email

---

## ⚙️ Configuration

### 📡 `registryservice`
```properties
spring.application.name=registryservice
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```
### 🌐 `gatewayservice`
```properties
spring.application.name=gatewayservice
server.port=8080
jwt.secret=<JWT_SECRET>
spring.datasource.url=jdbc:mysql://localhost:3306/passresets
spring.datasource.username=root
spring.datasource.password=admin
spring.jpa.hibernate.ddl-auto=update
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
management.endpoints.web.exposure.include=health,info,metrics,prometheus
```
### 👤 `userservice`
```properties
spring.application.name=userservice
server.port=8081
jwt.secret=<JWT_SECRET>
jwt.expiration=3600000
spring.datasource.url=jdbc:mysql://localhost:3306/userstaskdb
spring.datasource.username=root
spring.datasource.password=admin
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<YOUR_EMAIL>
spring.mail.password=<EMAIL_PASSWORD>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```
### 📝 `taskservice`
```properties
spring.application.name=taskservice
server.port=8082
spring.datasource.url=jdbc:mysql://localhost:3306/taskman
spring.datasource.username=root
spring.datasource.password=admin
eureka.client.service-url.defaultZone=http://localhost:8761/eureka
```
---

## ▶️ How to Run
```properties
1. Start the Eureka Registry (registryservice)

2. Start the API Gateway (gatewayservice)

3. Start the User Service (userservice)

4. Start the Task Service (taskservice)
```
---

## 🧪 API Usage
```properties
Base URL: http://localhost:8080
```
### 👤 `Auth & User`
```properties
Register

POST /api/auth/register

{
  "username": "test",
  "password": "password",
  "email": "test@example.com"
}

Register Admin

POST /api/auth/register/admin

Verify OTP

POST /api/auth/verify-otp

{
  "username": "test",
  "mfaOtp": "123456"
}

Login

POST /api/auth/login

{
  "username": "test",
  "password": "password"
}
```
### `👤 User Endpoints (Requires Bearer Token)`
```properties
Get Info

GET /api/user/info

Reset Password

POST /api/user/resetpwd

{
  "password": "oldPassword",
  "new_password": "newPassword123",
  "confirm_password": "newPassword123"
}

Change Email

POST /api/user/changemail

{
  "email": "new@example.com",
  "password": "password"
}
```
### 📝 `Task Endpoints`
```properties
Add Task

POST /api/user/newtask

{
  "title": "Buy groceries",
  "description": "Milk, eggs, bread, and fruits",
  "priority": "Low",
  "dueDate": "2025-06-15T18:00:00"
}

Get Task List

GET /api/user/tasklist

Get Task by ID

GET /api/user/task/{id}

Get Tasks by Priority

GET /api/user/tasks/priority/{priority}

Get Tasks by Status

GET /api/user/tasks/status/{status}

Get Tasks by Title

GET /api/user/tasks/title/{title}

Get Tasks by Due Date

GET /api/user/tasks/due-date?dueDate=2025-06-01

Count Tasks

GET /api/user/task-count

Delete Task

DELETE /api/user/delete-task/{id}

Mark Task as Complete/Pending/In Progress

PUT /api/user/mark-as-complete/{id}

PUT /api/user/mark-as-pending/{id}

PUT /api/user/mark-as-in-progress/{id}

Update Task

PUT /api/user/update-task/{id}

{
  "title": "Updated Title",
  "description": "Updated description",
  "priority": "High",
  "dueDate": "2025-06-20T12:00:00"
}
```
## 🔄 Task Pagination & Sorting

### 📄 `Get Paginated Tasks (User-specific)`

GET http://localhost:8080/api/user/tasklist/page?page=0&size=10&sortBy=createdDate&sortDir=asc

### 📄 `Get Paginated Tasks (All - Admin)`

GET http://localhost:8080/api/admin/tasklist/page?page=0&size=10&sortBy=createdDate&sortDir=asc


### 🛡️ `Admin Endpoints`
```properties
Delete User

DELETE /api/admin/delete/{id}

Delete Non-Verified Users

DELETE /api/admin/delete/non-verified

Admin Task Management

GET    /api/admin/tasklist

GET    /api/admin/task/{id}

GET    /api/admin/task-count

DELETE /api/admin/delete-task/{id}
```
### 📊 `Monitoring & Actuator`
```properties
http://localhost:8080/actuator/health

http://localhost:8080/actuator/info

http://localhost:8080/actuator/metrics

http://localhost:8080/actuator/prometheus
```
### 📒 `Swagger Docs`
```properties
Enable Swagger in gatewayservice, userservice, and taskservice to visualize and test endpoints.

Gateway (aggregate): http://localhost:8080/swagger-ui/index.html

User Service: http://localhost:8081/swagger-ui/index.html

Task Service: http://localhost:8082/swagger-ui/index.html
```
---

The userservice is scheduled to automatically delete all non-verified users or revert to its original email if it is not verified. Also sends reminder email for nearly due to date tasks at 8:00 am every day, it considers tasks who expire next day and aren't marked as complete.

It uses rate limiting for operations like login, resetpwd and changemail.

As well as implements logging using Spring Boot Logging to track API requests.