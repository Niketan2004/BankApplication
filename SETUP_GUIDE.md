# Bank Management System - Complete Setup Guide

## 🏦 Project Overview

This is a comprehensive **Bank Management System** built with **Spring Boot** (backend) and **React with Tailwind CSS** (frontend). It provides secure banking operations including user authentication, account management, and transaction processing.

## 📋 Features

### Backend Features
- **User Authentication & Authorization** (Spring Security + JWT)
- **Account Management** (Create, View, Update accounts)
- **Transaction Processing** (Deposit, Withdraw, Transfer)
- **Transaction History** with pagination
- **Role-based Access Control**
- **Global Exception Handling**
- **RESTful API Design**

### Frontend Features
- **Modern React UI** with Tailwind CSS
- **Responsive Design** for all devices
- **Authentication Flow** (Login/Register)
- **Dashboard** with account overview
- **Transaction Management** (Deposit, Withdraw, Transfer)
- **Transaction History** with filtering
- **Profile Management**
- **Toast Notifications**
- **Protected Routes**

## 🛠️ Technology Stack

### Backend
- **Java 17+**
- **Spring Boot 3.x**
- **Spring Security**
- **Spring Data JPA**
- **H2 Database** (for development)
- **Maven**

### Frontend
- **React 18**
- **React Router DOM**
- **Tailwind CSS**
- **Axios** (HTTP client)
- **React Toastify** (notifications)
- **Heroicons** (icons)

## 📁 Project Structure

```
BankApplication/
├── src/main/java/com/BankProject/BankApplication/
│   ├── Controller/          # REST Controllers
│   ├── Entity/             # JPA Entities
│   ├── Service/            # Business Logic
│   ├── Repository/         # Data Access Layer
│   ├── Auth/              # Security Configuration
│   ├── Exceptions/        # Exception Handling
│   ├── Utils/             # Utility Classes
│   └── Enum/              # Enumerations
├── src/main/Frontend/      # React Frontend
│   ├── public/            # Static files
│   ├── src/
│   │   ├── components/    # Reusable components
│   │   ├── pages/         # Page components
│   │   ├── contexts/      # React contexts
│   │   └── services/      # API services
│   └── package.json
├── src/main/resources/
│   └── application.properties
└── pom.xml
```

## 🚀 Quick Start

### Prerequisites
- **Java 17+** installed
- **Node.js 16+** and **npm** installed
- **Maven** installed

### Backend Setup

1. **Clone and Navigate to Project**
   ```bash
   cd BankApplication
   ```

2. **Install Backend Dependencies**
   ```bash
   mvn clean install
   ```

3. **Start Backend Server**
   ```bash
   mvn spring-boot:run
   ```
   
   The backend will start on `http://localhost:8080`

### Frontend Setup

1. **Navigate to Frontend Directory**
   ```bash
   cd src/main/Frontend
   ```

2. **Install Frontend Dependencies**
   ```bash
   npm install
   ```

3. **Start Frontend Development Server**
   ```bash
   npm start
   ```
   
   The frontend will start on `http://localhost:3000`

## 🔧 VS Code Tasks

For VS Code users, use the integrated tasks:

1. **Ctrl+Shift+P** → Type "Tasks: Run Task"
2. Select **"Start Frontend Development Server"**

## 🚨 Troubleshooting

### Frontend Won't Start (Dev Server Error)
If you encounter `Invalid options object. Dev Server has been initialized using an options object that does not match the API schema`, follow these steps:

1. **Create `.env` file** in the Frontend directory:
   ```bash
   cd src/main/Frontend
   echo "DANGEROUSLY_DISABLE_HOST_CHECK=true" > .env
   ```

2. **Clear npm cache**:
   ```bash
   npm cache clean --force
   ```

3. **Restart the development server**:
   ```bash
   npm start
   ```

### Port Already in Use
If ports 3000 or 8080 are already in use:

**For Frontend (Port 3000):**
```bash
# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F
```

**For Backend (Port 8080):**
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Backend Database Issues
If you encounter H2 database connection issues:

1. Check `application.properties` configuration
2. Ensure no other applications are using the database
3. Clear target directory: `mvn clean`

## 📊 Database Schema

The application uses H2 in-memory database with the following entities:

### User Entity
- `userId` (Primary Key)
- `username` (Unique)
- `email` (Unique)
- `fullName`
- `password` (Encrypted)
- `role` (ADMIN/USER)

### Account Entity
- `accountNumber` (Primary Key)
- `accountType` (SAVINGS/CURRENT)
- `balance`
- `userId` (Foreign Key)

### Transaction Entity
- `transactionId` (Primary Key)
- `amount`
- `type` (DEPOSIT/WITHDRAW/TRANSFER)
- `time`
- `accountNumber` (Foreign Key)

## 🔐 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### User Management
- `GET /api/users/profile` - Get user profile
- `PUT /api/users/profile` - Update user profile

### Account Operations
- `POST /api/accounts/deposit` - Deposit money
- `POST /api/accounts/withdraw` - Withdraw money
- `POST /api/accounts/transfer` - Transfer money

### Transactions
- `GET /api/transactions/history` - Get transaction history

## 🎨 Frontend Design System

### Colors
- **Primary**: Banking Blue (`#0284c7`)
- **Success**: Green (`#10b981`)
- **Error**: Red (`#ef4444`)
- **Warning**: Yellow (`#f59e0b`)

### Components
- **Cards**: Rounded corners with subtle shadows
- **Buttons**: Primary, secondary, and danger variants
- **Forms**: Clean input fields with focus states
- **Modals**: Centered overlays for transactions

## 🔒 Security Features

- **JWT Token-based Authentication**
- **Password Encryption** (BCrypt)
- **Role-based Authorization**
- **CORS Configuration**
- **Input Validation**
- **SQL Injection Prevention**

## 🧪 Testing

### Backend Testing
```bash
mvn test
```

### Frontend Testing
```bash
cd src/main/Frontend
npm test
```

## 📦 Building for Production

### Backend
```bash
mvn clean package
java -jar target/BankApplication-1.0.jar
```

### Frontend
```bash
cd src/main/Frontend
npm run build
```

## 🌐 Environment Configuration

### Backend (application.properties)
```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:bankdb
spring.jpa.hibernate.ddl-auto=create-drop
jwt.secret=your-secret-key
jwt.expiration=86400000
```

### Frontend (package.json)
```json
{
  "proxy": "http://localhost:8080"
}
```

## 📱 Usage Guide

### For New Users
1. Open `http://localhost:3000`
2. Click **"Get Started"** or **"Sign Up"**
3. Fill registration form with required details
4. Login with your credentials
5. Access dashboard to manage your account

### Banking Operations
1. **Deposit**: Add money to your account
2. **Withdraw**: Remove money from your account
3. **Transfer**: Send money to another account
4. **History**: View all your transactions

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👥 Team

- **Backend Development**: Spring Boot with Java
- **Frontend Development**: React with Tailwind CSS
- **Database Design**: JPA with H2
- **Security**: Spring Security with JWT

## 📞 Support

For questions or support:
- Create an issue in the repository
- Contact the development team
- Check the comprehensive documentation

---

**Happy Banking! 🏦✨**
