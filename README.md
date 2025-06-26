# 🏦 Complete Bank Management System - Spring Boot Application

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Technologies & Concepts Used](#technologies--concepts-used)
3. [Architecture & Design Patterns](#architecture--design-patterns)
4. [Database Design](#database-design)
5. [Security Implementation](#security-implementation)
6. [API Documentation](#api-documentation)
7. [Project Structure](#project-structure)
8. [Setup Instructions](#setup-instructions)
9. [Business Logic Explained](#business-logic-explained)
10. [Error Handling](#error-handling)


---

## 🎯 Project Overview

This is a **enterprise-grade Bank Management System** built using **Spring Boot 3.5.0** with **Java 21**. The application provides comprehensive banking functionalities including user registration, authentication, account management, and various transaction operations. It follows **industry best practices** and implements **secure banking operations** with proper validation and error handling.

### 🚀 Key Features
- 🔐 **Secure Authentication** - HTTP Basic Auth with BCrypt password encoding
- 👥 **Role-Based Access Control** - USER and ADMIN roles with different permissions
- 🏦 **Account Management** - Automatic account creation with sequential account numbers
- 💰 **Transaction Operations** - Deposit, Withdraw, Transfer with proper validations
- 📊 **Transaction History** - Paginated transaction records
- 🛡️ **Security** - Input validation, access control, and secure endpoints
- 📈 **Scalable Architecture** - Layered architecture with proper separation of concerns

---

## 🛠️ Technologies & Concepts Used

### **Core Technologies**
```xml
<!-- Spring Boot Framework -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.0</version>
</dependency>

<!-- Key Dependencies -->
- Spring Boot Starter Web          (REST APIs)
- Spring Boot Starter Data JPA     (Database Operations)
- Spring Boot Starter Security     (Authentication & Authorization)
- Spring Boot Starter Validation   (Input Validation)
- Spring Boot Starter Actuator     (Application Monitoring)
- MySQL Connector                   (Database Driver)
- Lombok                           (Boilerplate Code Reduction)
```

### **Java Concepts Implemented**
1. **Object-Oriented Programming** - Encapsulation, Inheritance, Abstraction
2. **Java 21 Features** - Record classes, Pattern matching, Modern syntax
3. **Annotations** - Extensive use of Spring and JPA annotations
4. **Exception Handling** - Custom exceptions and global exception handler
5. **Enums** - Type-safe constants for roles, account types, transaction types
6. **Optional Class** - Null-safe programming
7. **Stream API** - Functional programming for data processing
8. **Bean Validation** - JSR-303 validation annotations

### **Spring Boot Concepts**
1. **Dependency Injection** - Constructor and field injection
2. **Auto Configuration** - Spring Boot's automatic configuration
3. **Spring Security** - Authentication, authorization, password encoding
4. **Spring Data JPA** - Repository pattern, custom queries
5. **REST Controllers** - RESTful API design
6. **Exception Handling** - `@ControllerAdvice` for global exception handling
7. **Configuration Properties** - Environment-specific configurations
8. **Bean Lifecycle** - `@PostConstruct`, `@PreDestroy`

---

## 🏗️ Architecture & Design Patterns

### **Layered Architecture**
```
┌─── Presentation Layer (Controllers) ───┐
│  ├── HomeController                    │
│  ├── UserController                    │
│  └── TransactionController             │
├─── Service Layer (Business Logic) ─────┤
│  ├── UserService                       │
│  ├── AccountService                    │
│  ├── TransactionService                │
│  └── CustomUserDetailsService          │
├─── Repository Layer (Data Access) ─────┤
│  ├── UserRepository                    │
│  ├── AccountRepository                 │
│  └── TransactionRepository             │
└─── Entity Layer (Domain Models) ───────┘
   ├── User
   ├── Account
   └── Transactions
```

### **Design Patterns Used**
1. **Repository Pattern** - Data access abstraction
2. **DTO Pattern** - Data transfer objects for API responses
3. **Builder Pattern** - Spring Security User builder
4. **Template Method Pattern** - JPA repository inheritance
5. **Strategy Pattern** - Different authentication strategies
6. **Facade Pattern** - Service layer facades for complex operations

---

## 🗄️ Database Design

### **Entity Relationship Diagram**
```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│      User       │ 1───1 │     Account     │ 1───* │  Transactions   │
├─────────────────┤       ├─────────────────┤       ├─────────────────┤
│ userId (PK)     │       │ accountNumber   │       │ transactionId   │
│ fullName        │       │ balance         │       │ amount          │
│ email (UNIQUE)  │       │ accountType     │       │ type            │
│ password        │       │ user_id (FK)    │       │ time            │
│ role            │       │                 │       │ account_id (FK) │
│ account_id (FK) │       │                 │       │                 │
└─────────────────┘       └─────────────────┘       └─────────────────┘
```

### **Database Concepts**
1. **Primary Keys** - UUID for User/Transaction, Sequential Long for Account
2. **Foreign Keys** - Proper referential integrity
3. **Unique Constraints** - Email uniqueness
4. **Cascade Operations** - CascadeType.ALL for related entities
5. **Fetch Strategies** - LAZY loading for performance
6. **Sequence Generation** - Custom account number generation starting from 1462000000

---

## 🔐 Security Implementation

### **Authentication & Authorization**
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
            .csrf(csrf -> csrf.disable())
            .httpBasic(basic -> {})  // Enable HTTP Basic Auth
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/signup", "/api/dashboard").permitAll()
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .authenticationProvider(daoAuthenticationProvider())
            .build();
    }
}
```

### **Password Security**
- **BCrypt Hashing** - Industry-standard password hashing
- **Salt Generation** - Automatic salt generation for each password
- **Strength** - Default strength of 10 rounds

### **Access Control**
- **Method-Level Security** - `@PreAuthorize` annotations
- **Role-Based Access** - USER and ADMIN roles
- **Resource Protection** - Users can only access their own resources

---

## 📚 API Documentation

### **Authentication Endpoints**

#### 1. User Registration
```http
POST /api/signup
Content-Type: application/json

{
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "password": "securePassword123",
    "role": "USER",
    "balance": 1000.0,
    "accountType": "SAVINGS"
}
```

**Response (201 Created):**
```json
{
    "userId": "123e4567-e89b-12d3-a456-426614174000",
    "fullName": "John Doe",
    "email": "john.doe@example.com",
    "role": "USER",
    "accountNumber": 1462000001,
    "balance": 1000.0,
    "accountType": "SAVINGS"
}
```

#### 2. Login (HTTP Basic Auth)
```http
GET /user/dashboard
Authorization: Basic base64(email:password)
```

### **User Management Endpoints**

#### 3. Get User Dashboard
```http
GET /user/dashboard
Authorization: Basic base64(email:password)
```
**Response:** `"This is User dashboard"`

#### 4. Check Account Balance
```http
GET /user/balance
Authorization: Basic base64(email:password)
```
**Response:**
```json
{
    "accountNumber": 1462000001,
    "balance": 1000.0,
    "accountType": "SAVINGS"
}
```

#### 5. Update User Profile
```http
PUT /user/{userId}
Authorization: Basic base64(email:password)
Content-Type: application/json

{
    "fullName": "John Smith",
    "email": "john.smith@example.com"
}
```

#### 6. Delete User Account
```http
DELETE /user/{userId}
Authorization: Basic base64(email:password)
```
**Response:** `"User Deleted"`

### **Transaction Endpoints**

#### 7. Deposit Money
```http
POST /transactions/deposit
Authorization: Basic base64(email:password)
Content-Type: application/json

1500.0
```
**Response:**
```json
{
    "transactionId": "trans-123e4567-e89b-12d3",
    "amount": 1500.0,
    "type": "DEPOSIT",
    "time": "2025-06-26T10:30:00",
    "accountNumber": 1462000001
}
```

#### 8. Withdraw Money
```http
POST /transactions/withdraw
Authorization: Basic base64(email:password)
Content-Type: application/json

500.0
```

#### 9. Transfer Money
```http
POST /transactions/transfer
Authorization: Basic base64(email:password)
Content-Type: application/json

{
    "senderAccountNumber": 1462000001,
    "recieverAccountNumber": 1462000002,
    "amount": 300.0
}
```

#### 10. Transaction History (Paginated)
```http
GET /transactions/history?page=0&size=10
Authorization: Basic base64(email:password)
```
**Response:**
```json
{
    "content": [
        {
            "transactionId": "trans-123",
            "amount": 1500.0,
            "type": "DEPOSIT",
            "time": "2025-06-26T10:30:00",
            "accountNumber": 1462000001
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10
    },
    "totalElements": 25,
    "totalPages": 3
}
```

### **Public Endpoints**

#### 11. General Dashboard
```http
GET /api/dashboard
```
**Response:** `"This is dashboard ! That is open to all"`

---

## 📁 Project Structure

```
BankApplication/
├── src/main/java/com/BankProject/BankApplication/
│   ├── BankApplication.java                    # Main Application Class
│   ├── Auth/
│   │   └── SecurityConfig.java                 # Security Configuration
│   ├── Controller/                             # REST Controllers
│   │   ├── HomeController.java                 # Authentication endpoints
│   │   ├── UserController.java                 # User management endpoints
│   │   └── TransactionController.java          # Transaction endpoints
│   ├── Entity/                                 # JPA Entities
│   │   ├── User.java                          # User entity
│   │   ├── Account.java                       # Account entity
│   │   └── Transactions.java                 # Transaction entity
│   ├── Enum/                                  # Enumerations
│   │   ├── Role.java                          # USER, ADMIN
│   │   ├── AccountType.java                   # CURRENT, SAVINGS
│   │   └── TransactionTypes.java              # CREDIT, DEBIT, etc.
│   ├── Exceptions/                            # Custom Exceptions
│   │   ├── GlobalExceptionHandler.java        # Global exception handler
│   │   ├── InsufficientAmountException.java   # Custom exceptions
│   │   ├── UserAlreadyExistsException.java
│   │   └── UserNotFoundException.java
│   ├── Repository/                            # Data Access Layer
│   │   ├── UserRepository.java                # User data operations
│   │   ├── AccountRepository.java             # Account data operations
│   │   └── TransactionRepository.java         # Transaction data operations
│   ├── Service/                               # Business Logic Layer
│   │   ├── UserService.java                   # User business logic
│   │   ├── AccountService.java                # Account business logic
│   │   ├── TransactionService.java            # Transaction business logic
│   │   └── CustomUserDetailsService.java     # Spring Security integration
│   └── Utils/                                 # Utility Classes
│       ├── CustomUserInfo.java               # User response DTO
│       ├── ErrorResponse.java                 # Error response DTO
│       ├── TransactionResponseDTO.java        # Transaction response DTO
│       ├── TransferSlip.java                  # Transfer request DTO
│       └── UserAccountTemplate.java          # Registration request DTO
├── src/main/resources/
│   ├── application.properties                 # Main configuration
│   ├── static/                               # Static resources
│   └── templates/                            # Template files
└── pom.xml                                   # Maven configuration
```

---

## ⚙️ Setup Instructions

### **Prerequisites**
- Java 21 or higher
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA/Eclipse/VS Code)
- Postman (for API testing)

### **Database Setup**
1. **Install MySQL** and create database:
```sql
CREATE DATABASE bank_management_system_springboot;
```

2. **Update application.properties:**
```properties
spring.application.name=BankApplication
spring.datasource.url=jdbc:mysql://localhost:3306/bank_management_system_springboot
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
logging.level.org.springframework.security=DEBUG
```

### **Run Application**
```bash
# Clone repository
git clone <repository-url>
cd BankApplication

# Run with Maven
mvn spring-boot:run

# Or run JAR file
mvn clean package
java -jar target/BankApplication-0.0.1-SNAPSHOT.jar
```

### **Test Setup**
1. Application starts on `http://localhost:8080`
2. Database tables are auto-created via Hibernate
3. Use Postman to test API endpoints
4. Create a user via `/api/signup`
5. Use email/password for Basic Auth in subsequent requests

---

## 🧠 Business Logic Explained

### **User Registration Process**
1. **Input Validation** - Email format, required fields
2. **Duplicate Check** - Email uniqueness validation
3. **Password Encoding** - BCrypt hashing
4. **Account Creation** - Automatic account generation
5. **Sequential Account Numbers** - Starting from 1462000000
6. **Role Assignment** - Default USER role
7. **Response Generation** - Return user info without sensitive data

### **Authentication Flow**
1. **HTTP Basic Auth** - Email as username, plain password
2. **User Lookup** - Find user by email (case-insensitive)
3. **Password Verification** - BCrypt comparison
4. **Authority Assignment** - Role-based authorities
5. **Security Context** - Store authenticated user

### **Transaction Processing**
1. **Authentication Check** - Verify user is logged in
2. **Account Validation** - Verify account exists and belongs to user
3. **Amount Validation** - Check positive amounts, sufficient balance
4. **Balance Updates** - Atomic balance operations
5. **Transaction Recording** - Create transaction record
6. **Response Generation** - Return transaction details

### **Transfer Logic**
1. **Account Validation** - Both sender and receiver accounts exist
2. **Ownership Verification** - Sender must own the source account
3. **Balance Check** - Sufficient funds in sender account
4. **Atomic Transaction** - Debit sender, credit receiver
5. **Transaction Records** - Create records for both accounts
6. **Success Response** - Return transfer confirmation

---

## ⚠️ Error Handling

### **Common Error Responses**
```json
// 400 Bad Request - Validation Error
{
    "error": "VALIDATION_ERROR",
    "message": "Email is required",
    "timestamp": "2025-06-26T10:30:00"
}

// 401 Unauthorized - Authentication Failed
{
    "error": "AUTHENTICATION_FAILED",
    "message": "Invalid email or password"
}

// 403 Forbidden - Access Denied
{
    "error": "ACCESS_DENIED",
    "message": "You don't have permission to access this resource"
}

// 404 Not Found - Resource Not Found
{
    "error": "USER_NOT_FOUND",
    "message": "User with email john@example.com not found"
}

// 500 Internal Server Error
{
    "error": "INTERNAL_SERVER_ERROR",
    "message": "An unexpected error occurred"
}
```

---
*Last Updated: June 26, 2025*
