/**
 * api.js
 * 
 * This file contains the API service class that handles all HTTP requests to the backend.
 * It provides a centralized way to communicate with the Spring Boot backend server.
 * 
 * Key Features:
 * - Centralized API configuration with base URL
 * - Authentication token management with interceptors
 * - HTTP methods (GET, POST, PUT, DELETE)
 * - User authentication and registration
 * - Banking operations (deposit, withdraw, transfer)
 * - Transaction history management
 * - Admin user management operations
 * - Automatic token handling and error response management
 * 
 * The service uses Axios for HTTP requests and includes proper error handling.
 */

import axios from 'axios';
import { isTokenExpired } from '../utils/jwtUtils';

// API base URL - can be configured via environment variable for different environments
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

/**
 * ApiService Class
 * Handles all API communication with the backend server
 * Provides methods for authentication, banking operations, and admin functions
 */
class ApiService {
     /**
      * Constructor - Sets up the API service with base configuration
      * Initializes Axios instance with interceptors for automatic token handling
      */
     constructor() {
          // Create Axios instance with default configuration
          this.api = axios.create({
               baseURL: API_BASE_URL,
               headers: {
                    'Content-Type': 'application/json',
               },
          });

          // Request interceptor - automatically adds JWT token to requests
          this.api.interceptors.request.use(
               (config) => {
                    // Get stored JWT token from localStorage
                    const token = localStorage.getItem('jwtToken');
                    if (token) {
                         // Check if token is expired before using it
                         if (isTokenExpired(token)) {
                              // Token is expired, remove it and redirect to login
                              localStorage.removeItem('jwtToken');
                              localStorage.removeItem('userInfo');
                              window.location.href = '/login';
                              return Promise.reject(new Error('Token expired'));
                         }
                         // Add Authorization header with Bearer token to request
                         config.headers.Authorization = `Bearer ${token}`;
                    }
                    return config;
               },
               (error) => {
                    return Promise.reject(error);
               }
          );

          // Response interceptor - handles authentication errors globally
          this.api.interceptors.response.use(
               (response) => response,
               (error) => {
                    // If authentication fails (401), redirect to login
                    if (error.response?.status === 401) {
                         localStorage.removeItem('jwtToken');
                         localStorage.removeItem('userInfo');
                         window.location.href = '/login';
                    }
                    return Promise.reject(error);
               }
          );
     }

     /**
      * Sets JWT authentication token for API requests
      * Used for Bearer token authentication
      * 
      * @param {string} token - JWT token string
      */
     setAuthToken(token) {
          if (token) {
               // Set Authorization header with Bearer token for all subsequent requests
               this.api.defaults.headers.Authorization = `Bearer ${token}`;
               // Store token in localStorage for persistence
               localStorage.setItem('jwtToken', token);
          } else {
               // Remove Authorization header if no token provided
               delete this.api.defaults.headers.Authorization;
               localStorage.removeItem('jwtToken');
          }
     }

     /**
      * Clears JWT authentication token from API requests
      * Used during logout or when authentication fails
      */
     clearAuthToken() {
          // Remove Authorization header from default headers
          delete this.api.defaults.headers.Authorization;
          // Remove token from localStorage
          localStorage.removeItem('jwtToken');
          localStorage.removeItem('userInfo');
     }

     /**
      * Checks if the current JWT token is valid and not expired
      * @returns {boolean} True if token is valid, false otherwise
      */
     isTokenValid() {
          const token = localStorage.getItem('jwtToken');
          return token && !isTokenExpired(token);
     }

     /**
      * Gets the current JWT token from localStorage
      * @returns {string|null} The JWT token or null if not found
      */
     getToken() {
          return localStorage.getItem('jwtToken');
     }

     // ============================================================================
     // GENERIC HTTP METHODS
     // These provide basic HTTP functionality for all API operations
     // ============================================================================

     /**
      * Generic GET request method
      * @param {string} url - API endpoint URL (relative to base URL)
      * @param {Object} config - Additional Axios configuration
      * @returns {Promise} Axios response promise
      */
     get(url, config = {}) {
          return this.api.get(url, config);
     }

     /**
      * Generic POST request method
      * @param {string} url - API endpoint URL (relative to base URL)
      * @param {Object} data - Request body data
      * @param {Object} config - Additional Axios configuration
      * @returns {Promise} Axios response promise
      */
     post(url, data, config = {}) {
          return this.api.post(url, data, config);
     }

     /**
      * Generic PUT request method (for updates)
      * @param {string} url - API endpoint URL (relative to base URL)
      * @param {Object} data - Request body data
      * @param {Object} config - Additional Axios configuration
      * @returns {Promise} Axios response promise
      */
     put(url, data, config = {}) {
          return this.api.put(url, data, config);
     }

     /**
      * Generic DELETE request method
      * @param {string} url - API endpoint URL (relative to base URL)
      * @param {Object} config - Additional Axios configuration
      * @returns {Promise} Axios response promise
      */
     delete(url, config = {}) {
          return this.api.delete(url, config);
     }

     // ============================================================================
     // AUTHENTICATION METHODS
     // Handle user login and registration
     // ============================================================================

     /**
      * Authenticates user with email and password using JWT
      * @param {Object} credentials - Login credentials
      * @param {string} credentials.email - User's email address
      * @param {string} credentials.password - User's password
      * @returns {Promise} API response with JWT token
      */
     async login(credentials) {
          // Send authentication request to get JWT token
          const response = await this.post('/authenticate', {
               username: credentials.email,
               password: credentials.password
          });

          if (response.data) {
               // Store the JWT token
               this.setAuthToken(response.data);
               return response;
          }

          throw new Error('Authentication failed');
     }

     /**
      * Registers a new user account
      * @param {Object} userData - New user registration data
      * @param {string} userData.fullName - User's full name
      * @param {string} userData.email - User's email address
      * @param {string} userData.password - User's password
      * @param {number} userData.balance - Initial account balance
      * @param {string} userData.accountType - Account type (SAVINGS/CURRENT)
      * @returns {Promise} API response with registration result
      */
     async register(userData) {
          return this.post('/api/signup', userData);
     }

     // ============================================================================
     // USER ACCOUNT METHODS
     // Handle user profile and account operations
     // ============================================================================

     /**
      * Gets current user's account balance
      * @returns {Promise} API response with balance information
      */
     async getUserBalance() {
          return this.get('/user/balance');
     }

     /**
      * Updates user profile information
      * @param {string} userId - UUID of the user to update
      * @param {Object} userData - Updated user information
      * @param {string} userData.fullName - Updated full name
      * @param {string} userData.email - Updated email address
      * @returns {Promise} API response with updated user details
      */
     async updateUser(userId, userData) {
          return this.put(`/user/${userId}`, userData);
     }

     /**
      * Changes user's password
      * @param {Object} passwordData - Password change data
      * @param {string} passwordData.currentPassword - Current password
      * @param {string} passwordData.newPassword - New password
      * @returns {Promise} API response confirming password change
      */
     async changePassword(passwordData) {
          return this.post('/user/change-password', passwordData);
     }

     /**
      * Deletes user account
      * @param {string} userId - UUID of the user to delete
      * @returns {Promise} API response confirming deletion
      */
     async deleteUser(userId) {
          return this.delete(`/user/${userId}`);
     }

     // ============================================================================
     // TRANSACTION METHODS
     // Handle banking operations like deposit, withdraw, transfer
     // ============================================================================

     /**
      * Deposits money into user's account
      * @param {number} amount - Amount to deposit (must be positive)
      * @returns {Promise} API response with transaction details
      */
     async deposit(amount) {
          return this.post('/transactions/deposit', amount);
     }

     /**
      * Withdraws money from user's account
      * @param {number} amount - Amount to withdraw (must not exceed balance)
      * @returns {Promise} API response with transaction details
      */
     async withdraw(amount) {
          return this.post('/transactions/withdraw', amount);
     }

     /**
      * Transfers money between accounts
      * @param {Object} transferData - Transfer details
      * @param {string} transferData.toAccountNumber - Recipient account number
      * @param {number} transferData.amount - Amount to transfer
      * @param {string} transferData.description - Transfer description/note
      * @returns {Promise} API response with transaction details
      */
     async transfer(transferData) {
          return this.post('/transactions/transfer', transferData);
     }

     /**
      * Retrieves user's transaction history with pagination
      * @param {number} page - Page number (0-based indexing)
      * @param {number} size - Number of transactions per page
      * @returns {Promise} API response with paginated transaction list
      */
     async getTransactionHistory(page = 0, size = 10) {
          return this.get(`/transactions/history?page=${page}&size=${size}`);
     }

     // ============================================================================
     // PUBLIC API METHODS
     // Endpoints that don't require authentication
     // ============================================================================

     /**
      * Gets dashboard data for authenticated user
      * This endpoint is used for both authentication validation and data fetching
      * @returns {Promise} API response with user dashboard information
      */
     async getDashboard() {
          return this.get('/api/dashboard');
     }

     // ============================================================================
     // ADMIN-ONLY METHODS
     // These methods are restricted to users with ADMIN role
     // ============================================================================

     /**
      * Gets all users in the system (Admin only)
      * Returns paginated list of users, excluding admin accounts
      * 
      * @param {number} page - Page number (0-based indexing)
      * @param {number} size - Number of users per page
      * @returns {Promise} API response with paginated user list
      */
     async getAllUsers(page = 0, size = 10) {
          return this.get(`/admin/users?page=${page}&size=${size}`);
     }

     /**
      * Creates a new user account (Admin only)
      * @param {Object} userData - New user information
      * @param {string} userData.fullName - User's full name
      * @param {string} userData.email - User's email address (must be unique)
      * @param {string} userData.password - User's password (min 6 characters)
      * @param {number} userData.balance - Initial account balance
      * @param {string} userData.accountType - Account type (SAVINGS/CURRENT)
      * @param {string} userData.role - User role (USER/ADMIN)
      * @returns {Promise} API response with created user details
      */
     async createUser(userData) {
          return this.post('/admin/users', userData);
     }

     /**
      * Deletes a user account (Admin only)
      * Permanently removes user and all associated data
      * 
      * @param {string} userId - UUID of the user to delete
      * @returns {Promise} API response confirming deletion
      */
     async deleteUserAdmin(userId) {
          return this.delete(`/admin/users/${userId}`);
     }
}

// Create and export a single instance of the API service
// This ensures all components use the same instance with shared configuration
const api = new ApiService();
export default api;
