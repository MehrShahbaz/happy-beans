# Happy Beans Backend API

Welcome! Our team, **Happy Beans**, has developed a backend API for a food ordering platform. This project focuses on providing a **robust, extensible API** that enables users to order food from restaurants with a **personalized experience**. Our API serves **three distinct user types**: regular members, restaurant owners, and administrators.

---

## API Features

### Personalized Recommendations
Our API allows users to specify **likes and dislikes** using tags, which are then used to **filter and rank dishes**.  

### Secure Authentication
The API utilizes a **role-based access system** with **JWTs (JSON Web Tokens)** for authentication.  

### Integrated Payment System
We have seamlessly integrated the **Stripe API** to handle payments.  
The API manages the **entire payment lifecycle**, from creating a secure checkout session to processing webhook events for **payment success or failure**.

### Comprehensive Documentation
The API endpoints are well-documented using **Spring Rest Docs**, providing **clear and up-to-date information** on request/response formats, path parameters, and error codes.

---

## Tech & Architecture

### Tech Stack
- **Language & Framework:** Kotlin + Spring Boot with Spring Data JPA
- **Build Tool:** Gradle
- **Database:** PostgreSQL on AWS RDS
- **Authentication:** JWTs with **custom interceptors** for role-based access
- **Payment Integration:** Stripe API for handling payments and webhooks
- **Testing:** SpringBootTest, Mockito/MockK for mocking, Postman for manual testing

### System Diagram & Key Components
The application follows a **modular, layered architecture**:

- **Controllers:** Top layer handling HTTP requests for **members, restaurant owners, and admins**
- **Services:** Business logic layer with core functionality like `CartProductService` and `MemberOrderService`
- **Repositories:** Data persistence using **Spring Data JPA**
- **Infrastructure:** Houses key components like `JwtProvider` for **token management**
- **Interceptors:** Custom `HandlerInterceptor` implementations enforce **role-based access control**

The **production infrastructure** is deployed on an **AWS EC2 instance** and monitored.
