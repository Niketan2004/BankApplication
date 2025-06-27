/**
 * AuthContext.js
 * 
 * This file handles all authentication-related functionality for the banking application.
 * It provides a React Context that manages user authentication state, login/logout processes,
 * and user registration across the entire application.
 * 
 * Key Features:
 * - User authentication state management
 * - Login functionality for both users and admins
 * - User registration
 * - Automatic authentication checking on app start
 * - Token-based authentication with localStorage
 * - Role-based access control (USER vs ADMIN)
 */

import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';
import { toast } from 'react-toastify';

// Create React Context for authentication
// This allows any component in the app to access authentication state
const AuthContext = createContext({});

/**
 * Custom hook to use the AuthContext
 * This hook must be used within an AuthProvider component
 * @returns {Object} Authentication context with user state and methods
 */
export const useAuth = () => {
     const context = useContext(AuthContext);
     if (!context) {
          throw new Error('useAuth must be used within an AuthProvider');
     }
     return context;
};

/**
 * AuthProvider Component
 * Wraps the entire application to provide authentication context
 * Manages user state, authentication status, and loading states
 * 
 * @param {Object} props - Component props
 * @param {React.ReactNode} props.children - Child components
 */
export const AuthProvider = ({ children }) => {
     // State to store current user information (includes role, account details, etc.)
     const [user, setUser] = useState(null);

     // Boolean to track if user is currently authenticated
     const [isAuthenticated, setIsAuthenticated] = useState(false);

     // Loading state for authentication operations
     const [loading, setLoading] = useState(true);

     /**
      * Effect hook to check authentication status when app starts
      * Runs once when the component mounts
      * Checks for existing auth token in localStorage and validates it
      */
     useEffect(() => {
          const checkAuth = async () => {
               // Get stored authentication token from browser storage
               const token = localStorage.getItem('auth');

               if (token) {
                    try {
                         // Set the token for API requests
                         api.setAuthToken(token);

                         // Try to get user dashboard to verify token validity
                         // This endpoint returns user info if token is valid
                         const response = await api.get('/user/dashboard');

                         if (response.status === 200) {
                              // Token is valid, user is authenticated
                              setIsAuthenticated(true);
                              setUser(response.data);
                         }
                    } catch (error) {
                         // Token is invalid or expired, clean up
                         localStorage.removeItem('auth');
                         api.clearAuthToken();
                    }
               }

               // Authentication check complete
               setLoading(false);
          };

          checkAuth();
     }, []);

     /**
      * Fetches current user data from the server
      * Used to refresh user information after login or updates
      * @throws {Error} If fetching user data fails
      */
     const fetchUserData = async () => {
          try {
               const response = await api.get('/user/dashboard');
               setUser(response.data);
          } catch (error) {
               // If user dashboard fails, user might be an admin
               console.error('Failed to fetch user data:', error);
               throw error;
          }
     };

     /**
      * Handles user login authentication
      * Supports both regular users and administrators
      * 
      * @param {string} email - User's email address
      * @param {string} password - User's password
      * @returns {Object} Login result with success status and user data
      */
     const login = async (email, password) => {
          try {
               setLoading(true);

               // Create Base64 encoded credentials for Basic Authentication
               // Format: "email:password" encoded in Base64
               const credentials = btoa(`${email}:${password}`);

               // Set authentication token for API requests
               api.setAuthToken(credentials);

               // Attempt to get user dashboard data
               // This validates credentials and returns user information
               const response = await api.get('/user/dashboard');

               if (response.status === 200) {
                    // Login successful - store credentials and update state
                    localStorage.setItem('auth', credentials);
                    setIsAuthenticated(true);
                    setUser(response.data);

                    // Show success message to user
                    toast.success('Login successful!');

                    // Return success result with user data and admin flag
                    return {
                         success: true,
                         user: response.data,
                         isAdmin: response.data.role === 'ADMIN'
                    };
               }
          } catch (error) {
               // Login failed - clean up and show error
               api.clearAuthToken();

               // Determine appropriate error message based on status code
               const errorMessage = error.response?.status === 401
                    ? 'Invalid email or password'
                    : 'Login failed. Please try again.';

               toast.error(errorMessage);
               return { success: false, error: errorMessage };
          } finally {
               setLoading(false);
          }
     };

     /**
      * Handles new user registration
      * Creates a new user account with provided information
      * 
      * @param {Object} userData - User registration data
      * @param {string} userData.fullName - User's full name
      * @param {string} userData.email - User's email address
      * @param {string} userData.password - User's password
      * @param {number} userData.balance - Initial account balance
      * @param {string} userData.accountType - Type of account (SAVINGS/CURRENT)
      * @returns {Object} Registration result with success status
      */
     const register = async (userData) => {
          try {
               setLoading(true);

               // Send registration request to backend
               const response = await api.post('/api/signup', userData);

               if (response.status === 201) {
                    // Registration successful
                    toast.success('Registration successful! Please login.');
                    return { success: true, data: response.data };
               }
          } catch (error) {
               // Registration failed - extract error message
               const errorMessage = error.response?.data?.message || 'Registration failed. Please try again.';
               toast.error(errorMessage);
               return { success: false, error: errorMessage };
          } finally {
               setLoading(false);
          }
     };

     /**
      * Logs out the current user
      * Clears authentication token and resets user state
      */
     const logout = () => {
          // Remove stored authentication token
          localStorage.removeItem('auth');

          // Clear API authentication
          api.clearAuthToken();

          // Reset authentication state
          setUser(null);
          setIsAuthenticated(false);

          // Show logout confirmation
          toast.info('Logged out successfully');
     };

     // Context value that will be provided to all child components
     const value = {
          user,                // Current user object (null if not authenticated)
          isAuthenticated,     // Boolean indicating if user is logged in
          loading,             // Boolean indicating if auth operation is in progress
          login,               // Function to log in a user
          register,            // Function to register a new user
          logout,              // Function to log out current user
          fetchUserData        // Function to refresh user data
     };

     // Provide the authentication context to all child components
     return (
          <AuthContext.Provider value={value}>
               {children}
          </AuthContext.Provider>
     );
};
