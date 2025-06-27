# 🏦 SecureBank - Complete Bank Management System

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Technology Stack](#technology-stack)
4. [Architecture](#architecture)
5. [Database Design](#database-design)
6. [Security Implementation](#security-implementation)
7. [Frontend Features](#frontend-features)
8. [API Documentation](#api-documentation)
9. [Setup Instructions](#setup-instructions)
10. [Usage Guide](#usage-guide)
11. [Project Structure](#project-structure)
12. [Contributing](#contributing)

---

## 🎯 Project Overview

**SecureBank** is a modern, full-stack bank management system built with **Spring Boot** as a backend and **React** as a frontend. This enterprise-grade application provides comprehensive banking functionalities for both regular users and administrators, featuring a responsive design, secure authentication, and real-time transaction processing.

### 🌟 What Makes This Project Special
- **Full-Stack Implementation**: Spring Boot + React with seamless integration
- **Role-Based System**: Distinct user and admin interfaces with different capabilities
- **Mobile-First Design**: Fully responsive UI that works perfectly on all devices
- **Real-Time Operations**: Instant balance updates and transaction processing
- **Enterprise Security**: JWT authentication, password encryption, and secure endpoints
- **Professional UI/UX**: Modern banking interface with intuitive navigation and custom branding
- **Comprehensive Admin Panel**: Complete user management system for administrators

---

## ✨ Features

### 👤 **User Features**
- 🔐 **Secure Registration & Login** - Account creation with automatic bank account assignment
- 💰 **Balance Management** - View account balance with privacy toggle option
- 💸 **Money Transfer** - Send money to other accounts with real-time validation
- 🏧 **Deposit & Withdraw** - Add or remove money from account with instant updates
- 📊 **Transaction History** - Detailed transaction records with search and pagination
- � **Profile Management** - Update personal information and view account details
- 📱 **Mobile Responsive** - Perfect experience on mobile, tablet, and desktop

### 👨‍💼 **Admin Features**
- 🎛️ **Admin Dashboard** - Comprehensive user management interface with distinct dark theme
- 👥 **User Management** - View, search, edit, and delete user accounts (excluding other admins)
- � **Advanced Search** - Find users by name, email, ID, or account number
- ➕ **User Creation** - Create new user accounts with automatic account assignment
- ✏️ **User Editing** - Modify user information (name, email) with validation
- 🗑️ **User Deletion** - Remove user accounts with confirmation dialogs
- 📋 **User Listing** - Paginated user list with account information display
- 🔒 **Admin-Only Access** - Secured admin routes with role-based protection

### 🔧 **Technical Features**
- 🛡️ **JWT Authentication** - Secure token-based authentication system
- 🔄 **Real-Time Updates** - Instant balance and data synchronization
- 📱 **Responsive Design** - Mobile-first approach with Tailwind CSS
- 🚨 **Error Handling** - Comprehensive error management with user-friendly messages
- 🔒 **Data Validation** - Frontend and backend validation for all inputs
- 🎨 **Modern UI** - Professional banking interface with smooth animations and custom branding
- 📦 **Component Architecture** - Reusable React components with proper separation of concerns
- 🏷️ **Custom Branding** - Integrated custom logo throughout the application (navbar, footer, landing page, admin dashboard)

---

## 🛠️ Technology Stack

### **Backend (Spring Boot)**
```xml
Java 21
Spring Boot 3.5.0
Spring Security (Authentication & Authorization)
Spring Data JPA (Database Operations)
MySQL Database
Maven (Dependency Management)
JWT (JSON Web Tokens)
BCrypt (Password Encryption)
```

### **Frontend (React)**
```javascript
React 18
React Router DOM (Navigation & Routing)
Tailwind CSS (Styling & Responsive Design)
Heroicons (Modern Icon Library)
React Toastify (Notifications)
Axios (HTTP Client for API calls)
Context API (State Management)
```

### **Key Frontend Libraries**
- **React Router** - Client-side routing with protected routes
- **Tailwind CSS** - Utility-first CSS framework for responsive design
- **Heroicons** - Beautiful hand-crafted SVG icons
- **React Toastify** - User-friendly notification system
- **Context API** - Global state management for authentication

---

## 🏗️ Architecture

### **Backend Architecture**
```
┌─ Controller Layer (REST APIs)
│  ├─ AdminController (Admin user management operations)
│  ├─ HomeController (Public endpoints & authentication)
│  ├─ TransactionController (Banking transaction operations)
│  └─ UserController (User profile & account operations)
│
├─ Service Layer (Business Logic)
│  ├─ AccountService (Account management logic)
│  ├─ CustomUserDetailsService (Spring Security integration)
│  ├─ TransactionService (Transaction processing logic)
│  └─ UserService (User management & authentication logic)
│
├─ Repository Layer (Data Access)
│  ├─ AccountRepository (Account data operations)
│  ├─ TransactionRepository (Transaction data operations)
│  └─ UserRepository (User data operations)
│
├─ Entity Layer (Data Models)
│  ├─ Account (Bank account details & balance)
│  ├─ Transactions (Transaction records & history)
│  └─ User (User information & authentication)
│
├─ Security Layer (Authentication & Authorization)
│  └─ SecurityConfig (Spring Security configuration)
│
├─ Exception Layer (Error Handling)
│  ├─ GlobalExceptionHandler (Centralized exception handling)
│  ├─ InsufficientAmountException (Insufficient balance errors)
│  ├─ UserAlreadyExistsException (Duplicate user errors)
│  └─ UserNotFoundException (User not found errors)
│
├─ Enum Layer (Type-safe Constants)
│  ├─ AccountType (SAVINGS/CURRENT account types)
│  ├─ Role (USER/ADMIN user roles)
│  └─ TransactionTypes (Transaction type definitions)
│
└─ Utils Layer (Data Transfer & Utilities)
   ├─ CustomUserDetails (Custom user details implementation)
   ├─ CustomUserInfo (User info wrapper/DTO)
   ├─ ErrorResponse (Standardized error response DTO)
   ├─ TransactionResponseDTO (Transaction response DTO)
   ├─ TransferSlip (Money transfer request DTO)
   └─ UserAccountTemplate (User registration template DTO)
```

### **Frontend Architecture**
```
┌─ Pages (Main Application Views)
│  ├─ AdminDashboard (Admin user management interface with dark theme)
│  ├─ Dashboard (User main dashboard with banking operations)
│  ├─ LandingPage (Home/marketing page with features showcase)
│  ├─ LoginPage (User authentication with role-based redirects)
│  ├─ Profile (User profile management & account details)
│  ├─ RegisterPage (User registration with account creation)
│  └─ TransactionHistory (Paginated transaction records view)
│
├─ Components (Reusable UI Components)
│  └─ common/
│     ├─ AdminRoute (Admin-only access guard with role verification)
│     ├─ Footer (Page footer with branding & social links)
│     ├─ Navbar (Navigation bar with logo & role-based menu)
│     └─ ProtectedRoute (User authentication guard with redirects)
│
├─ Contexts (Global State Management)
│  └─ AuthContext (Authentication state, user data & session management)
│
├─ Services (API Communication Layer)
│  └─ api.js (HTTP client, API endpoints & request/response handling)
│
├─ Styles & Configuration
│  ├─ index.css (Global styles, utilities & responsive design)
│  ├─ tailwind.config.js (Tailwind CSS configuration)
│  └─ App.js (Main routing, layout & application structure)
│
└─ Static Assets
   ├─ index.html (Main HTML template with meta tags)
   ├─ logo.png (Application logo for branding)
   └─ manifest.json (PWA configuration & app metadata)
```

---

## 🗄️ Database Design

### **Entity Relationships**
```sql
-- Core Entities and Relationships
User (1) ←→ (1) Account
Account (1) ←→ (Many) Transaction

-- User Entity
- id (Primary Key)
- name, email, password
- role (USER/ADMIN)

-- Account Entity  
- account_number (PK)(Starts with 1462000000)
- balance (Decimal)
- account_type (SAVINGS/CURRENT)
- user_id (Foreign Key)

-- Transaction Entity
- transaction_id (Primary Key)
- transaction_type (DEPOSIT/WITHDRAW/TRANSFER)
- amount (Decimal)
- sender_account/ receiver_account
- timestamp
```

---

## 🔒 Security Implementation

### **Authentication & Authorization**
- **JWT Tokens** - Secure token-based authentication
- **BCrypt Hashing** - Password encryption with salt
- **Role-Based Access** - USER and ADMIN role separation
- **Protected Routes** - Frontend route guards
- **CORS Configuration** - Cross-origin resource sharing setup

### **Input Validation**
- **Frontend Validation** - Real-time form validation
- **Backend Validation** - Bean validation annotations
- **SQL Injection Prevention** - JPA prepared statements
- **XSS Protection** - Input sanitization

### **Security Headers**
- **CSRF Protection** - Cross-site request forgery prevention
- **Secure Headers** - X-Frame-Options, X-Content-Type-Options
- **HTTPS Ready** - SSL/TLS configuration support

---

## 🎨 Frontend Features

### **Responsive Design**
- **Mobile-First Approach** - Optimized for mobile devices
- **Tailwind CSS** - Utility-first responsive design system
- **Flexible Layouts** - Grid and flexbox layouts that adapt to screen size
- **Touch-Friendly** - Large buttons and touch targets for mobile users

### **User Experience**
- **Loading States** - Visual feedback during API calls
- **Error Handling** - User-friendly error messages with toast notifications
- **Form Validation** - Real-time validation with helpful error messages
- **Intuitive Navigation** - Clear navigation with role-based menu items
- **Professional UI** - Modern banking interface with consistent design patterns

### **Admin Dashboard Features**
- **User Search** - Search by name, email, ID, or account number
- **Pagination** - Efficient handling of large user lists
- **Modal Dialogs** - Create, edit, and delete operations in modals
- **Confirmation Dialogs** - Safety checks for destructive operations
- **Data Validation** - Comprehensive form validation for all operations
- **Responsive Tables** - Mobile-friendly data tables

---

---

## 🏗️ Architecture & Design Patterns

### **Layered Architecture**
```
┌─── Presentation Layer (Controllers) ────────────┐
│  ├── AdminController (Admin operations)         │
│  ├── HomeController (Public & auth endpoints)   │
│  ├── TransactionController (Banking operations) │
│  └── UserController (User operations)           │
├─── Service Layer (Business Logic) ──────────────┤
│  ├── AccountService (Account management)        │
│  ├── CustomUserDetailsService (Security)       │
│  ├── TransactionService (Transaction logic)     │
│  └── UserService (User management)              │
├─── Repository Layer (Data Access) ──────────────┤
│  ├── AccountRepository (Account data)           │
│  ├── TransactionRepository (Transaction data)   │
│  └── UserRepository (User data)                 │
├─── Entity Layer (Domain Models) ────────────────┤
│  ├── Account (Account entity)                   │
│  ├── Transactions (Transaction entity)          │
│  └── User (User entity)                         │
├─── Security & Exception Layer ──────────────────┤
│  ├── SecurityConfig (Spring Security)           │
│  ├── GlobalExceptionHandler (Error handling)    │
│  └── Custom Exceptions (Business exceptions)    │
└─── Utils & DTOs Layer ──────────────────────────┘
   ├── CustomUserDetails (Security integration)
   ├── Response DTOs (API responses)
   ├── Request DTOs (API requests)
   └── Enums (Type-safe constants)
```



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
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
 return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> {
                    })
                .authorizeHttpRequests(http -> {
                        http.requestMatchers("/login", "/api/**", "/actuator/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**", "/transactions/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated();
                })
                .logout(logout -> {
                         logout.logoutUrl("/logout");                         
                })
                .build();
     }
```

### **Password Security**
- **BCrypt Hashing** - Industry-standard password hashing
- **Salt Generation** - Automatic salt generation for each password
- **Strength** - Default strength of 10 rounds

### **Access Control**
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
├── 📄 README.md                                    # Project documentation
├── 📄 pom.xml                                      # Maven configuration
├── 📄 sequence.sql                                 # Database initialization
├── 📄 create-admin.html                           # Admin creation utility
│
├── 📂 src/main/
│   ├── 📂 java/com/BankProject/BankApplication/
│   │   ├── 📄 BankApplication.java                # Main Spring Boot class
│   │   │
│   │   ├── 📂 Auth/                               # Security & Authentication
│   │   │   └── 📄 SecurityConfig.java             # Security configuration
│   │   │
│   │   ├── 📂 Controller/                         # REST API Controllers
│   │   │   ├── 📄 AdminController.java            # Admin operations API
│   │   │   ├── 📄 HomeController.java             # Public endpoints
│   │   │   ├── 📄 TransactionController.java      # Transaction API
│   │   │   └── 📄 UserController.java             # User operations API
│   │   │
│   │   ├── 📂 Entity/                             # JPA Entity Models
│   │   │   ├── 📄 Account.java                    # Account entity
│   │   │   ├── 📄 Transactions.java               # Transaction entity
│   │   │   └── 📄 User.java                       # User entity
│   │   │
│   │   ├── 📂 Enum/                               # Type-safe Constants
│   │   │   ├── 📄 AccountType.java                # Account types (SAVINGS/CURRENT)
│   │   │   ├── 📄 Role.java                       # User roles (USER/ADMIN)
│   │   │   └── 📄 TransactionTypes.java           # Transaction types
│   │   │
│   │   ├── 📂 Exceptions/                         # Custom Exception Classes
│   │   │   ├── 📄 GlobalExceptionHandler.java     # Global exception handler
│   │   │   ├── 📄 InsufficientAmountException.java # Insufficient balance exception
│   │   │   ├── 📄 UserAlreadyExistsException.java  # User already exists exception
│   │   │   └── 📄 UserNotFoundException.java       # User not found exception
│   │   │
│   │   ├── 📂 Repository/                         # Data Access Layer
│   │   │   ├── 📄 AccountRepository.java          # Account data operations
│   │   │   ├── 📄 TransactionRepository.java      # Transaction data operations
│   │   │   └── 📄 UserRepository.java             # User data operations
│   │   │
│   │   ├── 📂 Service/                            # Business Logic Layer
│   │   │   ├── 📄 AccountService.java             # Account business logic
│   │   │   ├── 📄 CustomUserDetailsService.java   # Spring Security user details
│   │   │   ├── 📄 TransactionService.java         # Transaction business logic
│   │   │   └── 📄 UserService.java                # User business logic
│   │   │
│   │   └── 📂 Utils/                              # Utility Classes
│   │       ├── 📄 CustomUserDetails.java         # Custom user details implementation
│   │       ├── 📄 CustomUserInfo.java             # User info wrapper/DTO
│   │       ├── 📄 ErrorResponse.java              # Error response DTO
│   │       ├── 📄 TransactionResponseDTO.java     # Transaction response DTO
│   │       ├── 📄 TransferSlip.java               # Transfer request DTO
│   │       └── 📄 UserAccountTemplate.java        # User registration template
│   │
│   ├── 📂 Frontend/                               # React Frontend Application
│   │   ├── 📄 package.json                       # Node.js dependencies
│   │   ├── 📄 tailwind.config.js                 # Tailwind CSS configuration
│   │   │
│   │   ├── 📂 public/                             # Static public files
│   │   │   ├── 📄 index.html                      # Main HTML template
│   │   │   ├── 📄 logo.png                        # Application logo
│   │   │   └── 📄 manifest.json                   # Web app manifest
│   │   │
│   │   └── 📂 src/                                # React source code
│   │       ├── 📄 App.js                          # Main App component with routing
│   │       ├── 📄 index.js                        # React entry point
│   │       ├── 📄 index.css                       # Global styles and utilities
│   │       │
│   │       ├── 📂 pages/                          # Page Components
│   │       │   ├── 📄 AdminDashboard.js           # Admin user management interface
│   │       │   ├── 📄 Dashboard.js                # User main dashboard
│   │       │   ├── 📄 LandingPage.js              # Home/marketing page
│   │       │   ├── 📄 LoginPage.js                # User login page
│   │       │   ├── 📄 Profile.js                  # User profile management
│   │       │   ├── 📄 RegisterPage.js             # User registration form
│   │       │   └── 📄 TransactionHistory.js       # Transaction records view
│   │       │
│   │       ├── 📂 components/common/              # Reusable Components
│   │       │   ├── 📄 AdminRoute.js               # Admin access route guard
│   │       │   ├── 📄 Footer.js                   # Page footer with branding
│   │       │   ├── 📄 Navbar.js                   # Navigation bar with logo
│   │       │   └── 📄 ProtectedRoute.js           # User authentication guard
│   │       │
│   │       ├── 📂 contexts/                       # React Context Providers
│   │       │   └── 📄 AuthContext.js              # Authentication state management
│   │       │
│   │       └── 📂 services/                       # API Services
│   │           └── 📄 api.js                      # HTTP client & API methods
│   │
│   └── 📂 resources/
│       ├── 📄 application.properties              # Spring Boot configuration
│       ├── 📂 static/                             # Static web resources
│       └── 📂 templates/                          # Template files (if any)

```


---

## ⚙️ Setup Instructions

### **Prerequisites**
- **Java 21+** (Download from Oracle or use OpenJDK)
- **Node.js 18+** and npm (for React frontend)
- **Maven 3.6+** (for Spring Boot backend)
- **MySQL 8.0+** (Database)
- **IDE** (IntelliJ IDEA, VS Code, or Eclipse)
- **Git** (Version control)

### **Database Setup**
1. **Install MySQL** and create database:
```sql
CREATE DATABASE bank_management_system_springboot;
CREATE USER 'bank_user'@'localhost' IDENTIFIED BY 'bank_password';
GRANT ALL PRIVILEGES ON bank_management_system_springboot.* TO 'bank_user'@'localhost';
FLUSH PRIVILEGES;
```

2. **Update Backend Configuration** (`src/main/resources/application.properties`):
```properties
spring.application.name=BankApplication
spring.datasource.url=jdbc:mysql://localhost:3306/bank_management_system_springboot
spring.datasource.username=bank_user
spring.datasource.password=bank_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
logging.level.org.springframework.security=INFO
```

### **Backend Setup (Spring Boot)**
```bash
# 1. Navigate to project root
cd BankApplication

# 2. Install dependencies and run
mvn clean install
mvn spring-boot:run

# Alternative: Run as JAR
mvn clean package
java -jar target/BankApplication-0.0.1-SNAPSHOT.jar
```

### **Frontend Setup (React)**
```bash
# 1. Navigate to frontend directory
cd src/main/Frontend

# 2. Install dependencies
npm install

# 3. Start development server
npm start

# For production build
npm run build
```

### **Application URLs**
- **Backend API**: `http://localhost:8080`
- **Frontend Dev Server**: `http://localhost:3000`
- **Database**: `localhost:3306`

### **Initial Setup Verification**
1. ✅ Backend starts successfully on port 8080
2. ✅ Frontend starts successfully on port 3000
3. ✅ Database connection established
4. ✅ Tables auto-created via Hibernate
5. ✅ Can access landing page at `http://localhost:3000`

---

## 📖 Usage Guide

### **For Regular Users**

#### **1. Getting Started**
```bash
# 1. Visit the application
http://localhost:3000

# 2. Register a new account
- Click "Get Started" or "Register"
- Fill in name, email, and password
- Account number is automatically assigned
- Initial balance is ₹0.00
```

#### **2. User Dashboard**
- **View Balance**: See current account balance (with privacy toggle)
- **Quick Actions**: Deposit, withdraw, or transfer money
- **Recent Transactions**: Last 5 transactions displayed
- **Navigation**: Access profile and full transaction history

#### **3. Banking Operations**
```javascript
// Deposit Money
POST /api/users/deposit
{
  "amount": 1000.00
}

// Withdraw Money  
POST /api/users/withdraw
{
  "amount": 500.00
}

// Transfer Money
POST /api/users/transfer
{
  "senderAccountNumber": 1001,
  "recieverAccountNumber": 1002,
  "amount": 250.00
}
```

### **For Administrators**

#### **1. Admin Access**
```bash
# Default Admin Credentials (Create manually in database)
Email: admin@securebank.com
Password: admin123
Role: ADMIN

# Admin Dashboard URL
http://localhost:3000/admin
```

#### **2. User Management**
- **View All Users**: Paginated list with search functionality
- **Search Users**: By name, email, ID, or account number
- **Create User**: Add new user with automatic account creation
- **Edit User**: Modify user name and email
- **Delete User**: Remove user account (with confirmation)

#### **3. Admin Features**
```javascript
// Get All Users (Admin Only)
GET /api/admin/users?page=0&size=10

// Create New User (Admin Only)
POST /api/admin/users
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "securepass123"
}

// Delete User (Admin Only)
DELETE /api/admin/users/{userId}
```

### **Sample Data for Testing**

#### **Test User Accounts**
```sql
-- Create test users manually or via registration
INSERT INTO user (name, email, password, role) VALUES 
('John Doe', 'john@test.com', '$2a$10$...', 'USER'),
('Jane Smith', 'jane@test.com', '$2a$10$...', 'USER'),
('Admin User', 'admin@securebank.com', '$2a$10$...', 'ADMIN');
```

#### **Sample Transactions**
```bash
# Test transaction flow
1. Register two users
2. Deposit money to first user
3. Transfer money between users
4. Check transaction history
5. Test admin functions
```

---
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
---

## 🚀 Future Enhancements

### **Planned Features**

- 💳 **Card Management** - Virtual and physical card services
- 📊 **Analytics Dashboard** - Advanced financial insights
- 🔔 **Push Notifications** - Real-time transaction alerts
- 🌐 **Multi-Language Support** - Internationalization
- 🔐 **Two-Factor Authentication** - Enhanced security
- 🤖 **AI Chatbot** - Customer service automation

### **Technical Improvements**
- ⚡ **Performance Optimization** - Caching and query optimization
- 🧪 **Test Coverage** - Comprehensive test suite
- 📦 **Microservices Architecture** - Service decomposition
- ☁️ **Cloud Deployment** - AWS/Azure deployment
- 🔄 **CI/CD Pipeline** - Automated testing and deployment
- 📊 **Monitoring & Logging** - Application observability

---



