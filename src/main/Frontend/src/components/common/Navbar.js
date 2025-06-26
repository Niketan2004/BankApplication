import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import {
     BanknotesIcon,
     UserCircleIcon,
     ArrowRightOnRectangleIcon,
     ChartBarIcon,
     UserIcon
} from '@heroicons/react/24/outline';

const Navbar = () => {
     const { isAuthenticated, user, logout } = useAuth();
     const navigate = useNavigate();

     const handleLogout = () => {
          logout();
          navigate('/');
     };

     return (
          <nav className="bg-white shadow-lg border-b border-gray-200">
               <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                    <div className="flex justify-between h-16">
                         {/* Logo and Brand */}
                         <div className="flex items-center">
                              <Link to="/" className="flex items-center space-x-2">
                                   <div className="bg-banking-600 p-2 rounded-lg">
                                        <BanknotesIcon className="h-6 w-6 text-white" />
                                   </div>
                                   <span className="font-bold text-xl text-gray-900">SecureBank</span>
                              </Link>
                         </div>

                         {/* Navigation Links */}
                         <div className="flex items-center space-x-4">
                              {isAuthenticated ? (
                                   <>
                                        <Link
                                             to="/dashboard"
                                             className="flex items-center space-x-1 text-gray-600 hover:text-banking-600 px-3 py-2 rounded-md text-sm font-medium transition-colors"
                                        >
                                             <ChartBarIcon className="h-4 w-4" />
                                             <span>Dashboard</span>
                                        </Link>

                                        <Link
                                             to="/transactions"
                                             className="flex items-center space-x-1 text-gray-600 hover:text-banking-600 px-3 py-2 rounded-md text-sm font-medium transition-colors"
                                        >
                                             <BanknotesIcon className="h-4 w-4" />
                                             <span>Transactions</span>
                                        </Link>

                                        <Link
                                             to="/profile"
                                             className="flex items-center space-x-1 text-gray-600 hover:text-banking-600 px-3 py-2 rounded-md text-sm font-medium transition-colors"
                                        >
                                             <UserIcon className="h-4 w-4" />
                                             <span>Profile</span>
                                        </Link>

                                        {/* User Menu */}
                                        <div className="flex items-center space-x-3 ml-4 pl-4 border-l border-gray-200">
                                             <div className="flex items-center space-x-2">
                                                  <UserCircleIcon className="h-8 w-8 text-gray-400" />
                                                  <div className="hidden sm:block">
                                                       <p className="text-sm font-medium text-gray-900">
                                                            {user?.fullName || 'User'}
                                                       </p>
                                                       <p className="text-xs text-gray-500">
                                                            Account: {user?.accountNumber}
                                                       </p>
                                                  </div>
                                             </div>

                                             <button
                                                  onClick={handleLogout}
                                                  className="flex items-center space-x-1 text-gray-600 hover:text-red-600 px-3 py-2 rounded-md text-sm font-medium transition-colors"
                                             >
                                                  <ArrowRightOnRectangleIcon className="h-4 w-4" />
                                                  <span>Logout</span>
                                             </button>
                                        </div>
                                   </>
                              ) : (
                                   <div className="flex items-center space-x-4">
                                        <Link
                                             to="/login"
                                             className="text-gray-600 hover:text-banking-600 px-3 py-2 rounded-md text-sm font-medium transition-colors"
                                        >
                                             Login
                                        </Link>
                                        <Link
                                             to="/register"
                                             className="btn-primary text-sm"
                                        >
                                             Get Started
                                        </Link>
                                   </div>
                              )}
                         </div>
                    </div>
               </div>
          </nav>
     );
};

export default Navbar;
