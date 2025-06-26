import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../services/api';
import { toast } from 'react-toastify';

const AuthContext = createContext({});

export const useAuth = () => {
     const context = useContext(AuthContext);
     if (!context) {
          throw new Error('useAuth must be used within an AuthProvider');
     }
     return context;
};

export const AuthProvider = ({ children }) => {
     const [user, setUser] = useState(null);
     const [isAuthenticated, setIsAuthenticated] = useState(false);
     const [loading, setLoading] = useState(true);

     // Check if user is logged in on app start
     useEffect(() => {
          const checkAuth = async () => {
               const token = localStorage.getItem('auth');
               if (token) {
                    try {
                         api.setAuthToken(token);
                         const response = await api.get('/user/dashboard');
                         if (response.status === 200) {
                              setIsAuthenticated(true);
                              await fetchUserData();
                         }
                    } catch (error) {
                         localStorage.removeItem('auth');
                         api.clearAuthToken();
                    }
               }
               setLoading(false);
          };

          checkAuth();
     }, []);

     const fetchUserData = async () => {
          try {
               const response = await api.get('/user/dashboard');
               setUser(response.data);
          } catch (error) {
               console.error('Failed to fetch user data:', error);
          }
     };

     const login = async (email, password) => {
          try {
               setLoading(true);
               const credentials = btoa(`${email}:${password}`);
               api.setAuthToken(credentials);

               const response = await api.get('/user/dashboard');

               if (response.status === 200) {
                    localStorage.setItem('auth', credentials);
                    setIsAuthenticated(true);
                    await fetchUserData();
                    toast.success('Login successful!');
                    return { success: true };
               }
          } catch (error) {
               api.clearAuthToken();
               const errorMessage = error.response?.status === 401
                    ? 'Invalid email or password'
                    : 'Login failed. Please try again.';
               toast.error(errorMessage);
               return { success: false, error: errorMessage };
          } finally {
               setLoading(false);
          }
     };

     const register = async (userData) => {
          try {
               setLoading(true);
               const response = await api.post('/api/signup', userData);

               if (response.status === 201) {
                    toast.success('Registration successful! Please login.');
                    return { success: true, data: response.data };
               }
          } catch (error) {
               const errorMessage = error.response?.data?.message || 'Registration failed. Please try again.';
               toast.error(errorMessage);
               return { success: false, error: errorMessage };
          } finally {
               setLoading(false);
          }
     };

     const logout = () => {
          localStorage.removeItem('auth');
          api.clearAuthToken();
          setUser(null);
          setIsAuthenticated(false);
          toast.info('Logged out successfully');
     };

     const value = {
          user,
          isAuthenticated,
          loading,
          login,
          register,
          logout,
          fetchUserData
     };

     return (
          <AuthContext.Provider value={value}>
               {children}
          </AuthContext.Provider>
     );
};
