# 🏦 Bank Management System (Spring Boot)

A **full-stack Bank Management System** built using **Spring Boot** that provides secure account creation, login, fund transactions, and account management functionalities. This project follows industry-level design patterns and integrates Spring Security for authentication and authorization.

---

## 🚀 Features

- 🔐 User Signup & Login with Spring Security
- 🧾 Account creation with custom account numbers (starting from 1462000000)
- 📧 Balance check using email, without loading full user object
- 💸 Deposit, Withdraw, and Transfer transactions
- 📜 Transaction history with **pagination support**
- 📑 Role-based access control (Admin/User)
- 🛡️ JWT-based authentication *(WIP)*
- 🔄 RESTful API design following best practices
- 🧪 Integrated exception handling and validation

---

## 🛠️ Tech Stack

| Layer        | Technology            |
|-------------|------------------------|
| Backend      | Java 17, Spring Boot 3 |
| Security     | Spring Security 6     |
| Persistence  | Spring Data JPA + Hibernate |
| Database     | MySQL                 |
| Build Tool   | Maven                 |
| IDE          | IntelliJ IDEA         |

---

## 🧩 Project Structure

```
BankApplication/
├── Entity/
│   ├── User.java
│   ├── Account.java
│   └── Transactions.java
├── Repository/
│   ├── UserRepository.java
│   ├── AccountRepository.java
│   └── TransactionRepository.java
├── Service/
│   ├── UserService.java
│   └── AccountService.java
├── Controller/
│   ├── AuthController.java
│   ├── UserController.java
│   └── TransactionController.java
├── Security/
│   ├── WebSecurityConfig.java
│   └── CustomUserDetails.java
└── BankApplication.java
```

---

## ⚙️ Setup Instructions

1. **Clone the project:**
   ```bash
   git clone https://github.com/your-username/BankApplication.git
   cd BankApplication
   ```

2. **Configure the database:**
   Add your credentials in:
   - `application-dev.properties`
   - `application-prod.properties`

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

---

## 📌 Active Profiles

This project supports multiple environments using Spring Profiles:

```properties
# application.properties
spring.application.name=BankApplication
spring.profiles.active=dev
```

- `application-dev.properties` → Local DB, debug logging
- `application-prod.properties` → Production DB, secure settings

---

## 🔍 API Highlights

| Endpoint              | Method | Access   | Description                |
|-----------------------|--------|----------|----------------------------|
| `/api/signup`         | POST   | Public   | Register a new user        |
| `/api/login`          | POST   | Public   | Authenticate user          |
| `/user/dashboard`     | GET    | User     | View user dashboard        |
| `/user/balance`       | GET    | User     | Check account balance by email |
| `/user/transactions`  | GET    | User     | Paginated transaction list |
| `/admin/delete/{id}`  | DELETE | Admin    | Delete user securely       |

---

## 🧠 Challenges Faced & How I Solved Them

### ❗ Issues

- **Security Flaw:**
  - Any logged-in user could delete other users via ID.
  - **Solution:** Secured endpoint using `@PreAuthorize` and matched user ID with authentication.

- **Recursive Serialization Error:**
  - Caused due to bidirectional JPA mappings.
  - **Solution:** Used `@JsonIgnore` in entity mappings.

- **Password Matching Failed:**
  - Due to storing plain password and trying to validate against BCrypt hash.
  - **Solution:** Used `BCryptPasswordEncoder.encode()` consistently on registration.

- **Hard-coded Account Numbers:**
  - Replaced with sequence generator using `@SequenceGenerator`.

- **Performance Bottleneck in Transaction Fetch:**
  - Fixed with `Pageable` support in `TransactionRepository`.

---

## ✅ Improvements Made

- ✅ Custom account number generation using JPA Sequence
- ✅ Secured delete logic with user ownership validation
- ✅ Created profile-based configuration: dev and prod
- ✅ Refactored balance check using only email (lightweight queries)
- ✅ Transaction APIs are now pageable for scalability

---

## 📌 To-Do

- [ ] Complete JWT-based login & token refresh
- [ ] Add frontend using React or Thymeleaf
- [ ] Add audit logging for key user actions
- [ ] Enable email notifications on transactions
- [ ] Dockerize app and configure CI/CD

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you'd like to improve or add.

---



> “Building secure, scalable banking systems — one microservice at a time.”
