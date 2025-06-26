# SecureBank Frontend

A modern, responsive React frontend for the SecureBank application built with Tailwind CSS.

## 🚀 Features

- **Modern UI/UX** - Clean, professional design with smooth animations
- **Fully Responsive** - Works perfectly on desktop, tablet, and mobile devices
- **Authentication System** - Secure login/register with HTTP Basic Auth
- **Dashboard** - Comprehensive account overview with quick actions
- **Transaction Management** - Deposit, withdraw, transfer with real-time updates
- **Transaction History** - Paginated transaction list with filters and search
- **Profile Management** - Update account information and settings
- **Real-time Notifications** - Toast notifications for all actions
- **Error Handling** - Comprehensive error handling with user-friendly messages

## 🛠️ Technologies Used

- **React 18** - Modern React with hooks and functional components
- **React Router 6** - Client-side routing with protected routes
- **Tailwind CSS** - Utility-first CSS framework for styling
- **Heroicons** - Beautiful SVG icons
- **Axios** - HTTP client for API requests
- **React Toastify** - Elegant notifications
- **Context API** - State management for authentication

## 📁 Project Structure

```
src/
├── components/
│   └── common/
│       ├── Navbar.js          # Navigation component
│       ├── Footer.js          # Footer component
│       └── ProtectedRoute.js  # Route protection
├── contexts/
│   └── AuthContext.js         # Authentication context
├── pages/
│   ├── LandingPage.js         # Home/landing page
│   ├── LoginPage.js           # User login
│   ├── RegisterPage.js        # User registration
│   ├── Dashboard.js           # Main dashboard
│   ├── TransactionHistory.js  # Transaction history
│   └── Profile.js             # User profile
├── services/
│   └── api.js                 # API service layer
├── App.js                     # Main app component
├── index.js                   # App entry point
└── index.css                  # Global styles and Tailwind
```

## 🎨 Design System

### Colors
- **Primary**: Banking blue (#0ea5e9, #0284c7, #0369a1)
- **Success**: Green (#16a34a, #15803d)
- **Warning**: Amber (#d97706, #b45309)
- **Error**: Red (#dc2626, #b91c1c)
- **Gray Scale**: Modern gray palette

### Components
- **Buttons**: Primary, secondary, and danger variants
- **Input Fields**: Consistent styling with icons
- **Cards**: Clean cards with subtle shadows
- **Modals**: Centered modals with backdrop blur

## 🚀 Getting Started

### Prerequisites
- Node.js (16+ recommended)
- npm or yarn
- Spring Boot backend running on localhost:8080

### Installation

1. **Navigate to frontend directory**
   ```bash
   cd src/main/Frontend
   ```

2. **Install dependencies**
   ```bash
   npm install
   ```

3. **Start development server**
   ```bash
   npm start
   ```

4. **Open browser**
   ```
   http://localhost:3000
   ```

### Build for Production

```bash
npm run build
```

This creates an optimized production build in the `build` directory.

## 🔧 Configuration

### API Configuration
The frontend is configured to proxy API requests to the Spring Boot backend:

```json
// package.json
"proxy": "http://localhost:8080"
```

For production, update the API base URL in `src/services/api.js`:

```javascript
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';
```

### Environment Variables
Create a `.env` file in the frontend root:

```env
REACT_APP_API_BASE_URL=http://localhost:8080
REACT_APP_APP_NAME=SecureBank
```

## 📱 Responsive Design

The application is fully responsive with breakpoints:
- **Mobile**: < 768px
- **Tablet**: 768px - 1024px  
- **Desktop**: > 1024px

### Mobile Features
- Touch-friendly interface
- Optimized navigation
- Mobile-specific layouts
- Swipe gestures support

## 🔐 Security Features

### Authentication
- HTTP Basic Authentication
- Token storage in localStorage
- Automatic token refresh
- Protected routes

### Input Validation
- Client-side form validation
- XSS prevention
- Input sanitization
- Error boundary handling

## 🎯 User Experience

### Navigation
- Clean, intuitive navigation
- Breadcrumb support
- Mobile hamburger menu
- Quick action buttons

### Feedback
- Loading states for all actions
- Success/error notifications
- Progress indicators
- Form validation messages

### Accessibility
- Semantic HTML structure
- ARIA labels and roles
- Keyboard navigation
- Screen reader support

## 📊 Performance

### Optimization
- Code splitting with React.lazy
- Image optimization
- Bundle size optimization
- Tree shaking

### Caching
- API response caching
- Static asset caching
- Service worker support

## 🧪 Testing

### Running Tests
```bash
npm test
```

### Testing Strategy
- Unit tests for components
- Integration tests for user flows
- E2E tests with Cypress
- API mocking for tests

## 🚀 Deployment

### Vercel (Recommended)
1. Connect GitHub repository
2. Set environment variables
3. Deploy automatically

### Netlify
1. Build project: `npm run build`
2. Deploy `build` folder
3. Configure redirects for SPA

### Docker
```dockerfile
FROM node:16-alpine
WORKDIR /app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npm run build
EXPOSE 3000
CMD ["npm", "start"]
```

## 🔄 Integration with Backend

### API Endpoints Used
- `POST /api/signup` - User registration
- `GET /user/dashboard` - Authentication check
- `GET /user/balance` - Get account balance
- `PUT /user/{id}` - Update user profile
- `DELETE /user/{id}` - Delete user account
- `POST /transactions/deposit` - Deposit money
- `POST /transactions/withdraw` - Withdraw money
- `POST /transactions/transfer` - Transfer money
- `GET /transactions/history` - Get transaction history

### Authentication Flow
1. User enters email/password
2. Frontend creates Basic Auth header
3. API validates credentials
4. Success: Store token, redirect to dashboard
5. Failure: Show error message

## 🛠️ Development

### Code Style
- ESLint configuration
- Prettier formatting
- Consistent naming conventions
- Component documentation

### Git Workflow
- Feature branches
- Pull request reviews
- Automated testing
- Semantic versioning

## 📈 Future Enhancements

### Planned Features
- [ ] Dark mode support
- [ ] Multi-language support
- [ ] Advanced charts and analytics
- [ ] Real-time notifications with WebSocket
- [ ] Mobile app with React Native
- [ ] Offline support with PWA
- [ ] Advanced security features
- [ ] Biometric authentication

### Performance Improvements
- [ ] Virtual scrolling for large lists
- [ ] Image lazy loading
- [ ] Code splitting optimization
- [ ] Service worker caching

## 🐛 Troubleshooting

### Common Issues

1. **CORS Errors**
   - Ensure backend allows frontend origin
   - Check proxy configuration

2. **Authentication Issues**
   - Verify backend is running
   - Check network requests in DevTools

3. **Build Errors**
   - Clear node_modules and reinstall
   - Check Node.js version compatibility

### Debug Mode
Enable debug logging:
```javascript
// Add to index.js
if (process.env.NODE_ENV === 'development') {
  console.log('Debug mode enabled');
}
```

## 📞 Support

For development support:
- Check browser console for errors
- Review network requests in DevTools
- Verify backend API responses
- Check authentication token validity

---

**Built with ❤️ using React + Tailwind CSS**
