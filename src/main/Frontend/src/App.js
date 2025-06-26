import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

import { AuthProvider } from './contexts/AuthContext';
import ProtectedRoute from './components/common/ProtectedRoute';
import Navbar from './components/common/Navbar';
import Footer from './components/common/Footer';

import LandingPage from './pages/LandingPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import Dashboard from './pages/Dashboard';
import TransactionHistory from './pages/TransactionHistory';
import Profile from './pages/Profile';

function App() {
     return (
          <AuthProvider>
               <Router>
                    <div className="min-h-screen bg-gray-50 flex flex-col">
                         <Navbar />
                         <main className="flex-grow">
                              <Routes>
                                   <Route path="/" element={<LandingPage />} />
                                   <Route path="/login" element={<LoginPage />} />
                                   <Route path="/register" element={<RegisterPage />} />
                                   <Route
                                        path="/dashboard"
                                        element={
                                             <ProtectedRoute>
                                                  <Dashboard />
                                             </ProtectedRoute>
                                        }
                                   />
                                   <Route
                                        path="/transactions"
                                        element={
                                             <ProtectedRoute>
                                                  <TransactionHistory />
                                             </ProtectedRoute>
                                        }
                                   />
                                   <Route
                                        path="/profile"
                                        element={
                                             <ProtectedRoute>
                                                  <Profile />
                                             </ProtectedRoute>
                                        }
                                   />
                                   <Route path="*" element={<Navigate to="/" replace />} />
                              </Routes>
                         </main>
                         <Footer />
                         <ToastContainer
                              position="top-right"
                              autoClose={5000}
                              hideProgressBar={false}
                              newestOnTop={false}
                              closeOnClick
                              rtl={false}
                              pauseOnFocusLoss
                              draggable
                              pauseOnHover
                              theme="light"
                         />
                    </div>
               </Router>
          </AuthProvider>
     );
}

export default App;
