import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

class ApiService {
     constructor() {
          this.api = axios.create({
               baseURL: API_BASE_URL,
               headers: {
                    'Content-Type': 'application/json',
               },
          });

          // Request interceptor
          this.api.interceptors.request.use(
               (config) => {
                    const token = localStorage.getItem('auth');
                    if (token) {
                         config.headers.Authorization = `Basic ${token}`;
                    }
                    return config;
               },
               (error) => {
                    return Promise.reject(error);
               }
          );

          // Response interceptor
          this.api.interceptors.response.use(
               (response) => response,
               (error) => {
                    if (error.response?.status === 401) {
                         localStorage.removeItem('auth');
                         window.location.href = '/login';
                    }
                    return Promise.reject(error);
               }
          );
     }

     setAuthToken(token) {
          if (token) {
               this.api.defaults.headers.Authorization = `Basic ${token}`;
          } else {
               delete this.api.defaults.headers.Authorization;
          }
     }

     clearAuthToken() {
          delete this.api.defaults.headers.Authorization;
     }

     // Generic API methods
     get(url, config = {}) {
          return this.api.get(url, config);
     }

     post(url, data, config = {}) {
          return this.api.post(url, data, config);
     }

     put(url, data, config = {}) {
          return this.api.put(url, data, config);
     }

     delete(url, config = {}) {
          return this.api.delete(url, config);
     }

     // Authentication APIs
     async login(credentials) {
          const token = btoa(`${credentials.email}:${credentials.password}`);
          this.setAuthToken(token);
          return this.get('/user/dashboard');
     }

     async register(userData) {
          return this.post('/api/signup', userData);
     }

     // User APIs
     async getUserBalance() {
          return this.get('/user/balance');
     }

     async updateUser(userId, userData) {
          return this.put(`/user/${userId}`, userData);
     }

     async changePassword(passwordData) {
          return this.post('/user/change-password', passwordData);
     }

     async deleteUser(userId) {
          return this.delete(`/user/${userId}`);
     }

     // Transaction APIs
     async deposit(amount) {
          return this.post('/transactions/deposit', amount);
     }

     async withdraw(amount) {
          return this.post('/transactions/withdraw', amount);
     }

     async transfer(transferData) {
          return this.post('/transactions/transfer', transferData);
     }

     async getTransactionHistory(page = 0, size = 10) {
          return this.get(`/transactions/history?page=${page}&size=${size}`);
     }

     // Public APIs
     async getDashboard() {
          return this.get('/api/dashboard');
     }
}

const api = new ApiService();
export default api;
