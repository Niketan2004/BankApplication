# 🔗 React + Spring Boot Integration Guide

## How React App Connects to Spring Boot Backend

Your React app connects to Spring Boot through multiple layers. Here's the complete setup:

## 🛠️ Connection Architecture

```
React App (Port 3000) → Proxy → Spring Boot (Port 8080) → Database
```

## 1. 📡 Proxy Configuration

**File: `package.json`**
```json
{
  "proxy": "http://localhost:8080"
}
```

**What it does:**
- Automatically forwards API requests from React (`localhost:3000`) to Spring Boot (`localhost:8080`)
- Eliminates CORS issues during development
- All API calls starting with `/api`, `/user`, `/transactions` are proxied to backend

## 2. 🔧 API Service Layer

**File: `src/services/api.js`**

### Base Configuration:
```javascript
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});
```

### Authentication Interceptor:
```javascript
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('auth');
  if (token) {
    config.headers.Authorization = `Basic ${token}`;
  }
  return config;
});
```

### Error Handling:
```javascript
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('auth');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);
```

## 3. 🌐 API Endpoints Mapping

| React Method | Endpoint | Spring Boot Controller |
|-------------|----------|----------------------|
| `api.login()` | `GET /user/dashboard` | `@GetMapping("/user/dashboard")` |
| `api.register()` | `POST /api/signup` | `@PostMapping("/api/signup")` |
| `api.deposit()` | `POST /transactions/deposit` | `@PostMapping("/transactions/deposit")` |
| `api.withdraw()` | `POST /transactions/withdraw` | `@PostMapping("/transactions/withdraw")` |
| `api.transfer()` | `POST /transactions/transfer` | `@PostMapping("/transactions/transfer")` |
| `api.getTransactionHistory()` | `GET /transactions/history` | `@GetMapping("/transactions/history")` |

## 4. 🔐 Authentication Flow

### Frontend (React):
```javascript
// Login
const credentials = { email, password };
const token = btoa(\`\${email}:\${password}\`); // Base64 encode
localStorage.setItem('auth', token);

// Use in API calls
headers: { Authorization: \`Basic \${token}\` }
```

### Backend (Spring Boot):
```java
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        return http
            .httpBasic(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/signup").permitAll()
                .anyRequest().authenticated()
            )
            .build();
    }
}
```

## 5. 📊 Data Flow Example

### Frontend Component:
```javascript
// Dashboard.js
const [user, setUser] = useState(null);

useEffect(() => {
  const fetchUserData = async () => {
    try {
      const response = await api.get('/user/dashboard');
      setUser(response.data);
    } catch (error) {
      toast.error('Failed to load user data');
    }
  };
  
  fetchUserData();
}, []);
```

### Backend Controller:
```java
@RestController
public class UserController {
    
    @GetMapping("/user/dashboard")
    public ResponseEntity<User> getDashboard(Authentication auth) {
        String email = auth.getName();
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }
}
```

## 6. 🔄 Context Integration

**File: `src/contexts/AuthContext.js`**
```javascript
const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  const fetchUserData = async () => {
    try {
      const response = await api.get('/user/dashboard');
      setUser(response.data);
    } catch (error) {
      console.error('Failed to fetch user data:', error);
      logout();
    } finally {
      setLoading(false);
    }
  };

  // ... rest of context logic
};
```

## 7. 🚀 Environment Configuration

### Development (.env):
```properties
REACT_APP_API_BASE_URL=http://localhost:8080
DANGEROUSLY_DISABLE_HOST_CHECK=true
GENERATE_SOURCEMAP=false
```

### Production:
```properties
REACT_APP_API_BASE_URL=https://your-backend-domain.com
```

## 8. 🛡️ CORS Configuration (Spring Boot)

**File: `SecurityConfig.java`**
```java
@Configuration
public class SecurityConfig {
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
```

## 9. 🧪 Testing the Connection

### Check if Backend is Running:
```bash
curl http://localhost:8080/actuator/health
```

### Check Frontend Proxy:
1. Open browser dev tools
2. Go to Network tab
3. Make API call from React
4. Verify request goes to `localhost:8080`

## 10. 🔧 Common Connection Issues & Solutions

### Issue 1: CORS Errors
**Solution:** Add proxy in `package.json` and CORS config in Spring Boot

### Issue 2: Authentication Failed
**Solution:** Check Base64 encoding and Authorization header format

### Issue 3: 404 Errors
**Solution:** Verify endpoint mappings match between React and Spring Boot

### Issue 4: Connection Refused
**Solution:** Ensure both servers are running on correct ports

## 11. 📱 Usage in Components

```javascript
// In any React component
import { useAuth } from '../contexts/AuthContext';
import api from '../services/api';

const MyComponent = () => {
  const { user } = useAuth();
  
  const handleDeposit = async (amount) => {
    try {
      await api.deposit(amount);
      toast.success('Deposit successful!');
    } catch (error) {
      toast.error('Deposit failed');
    }
  };
  
  return (
    <div>
      <h1>Welcome, {user?.fullName}</h1>
      <button onClick={() => handleDeposit(100)}>
        Deposit $100
      </button>
    </div>
  );
};
```

## 📋 Quick Checklist

- ✅ Proxy configured in `package.json`
- ✅ API service with Axios setup
- ✅ Authentication interceptors
- ✅ Error handling
- ✅ Environment variables
- ✅ CORS configuration in backend
- ✅ Consistent endpoint mappings
- ✅ Both servers running on correct ports

Your React app is already fully configured to work with Spring Boot! The connection is seamless and handles authentication, error management, and data flow automatically.
