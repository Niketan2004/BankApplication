/**
 * Footer Component
 * 
 * This component renders the footer section of the application with:
 * - Brand information and description
 * - Social media links (Facebook, Twitter, LinkedIn)
 * - Service links for easy navigation
 * - Support and contact information
 * - Copyright notice with dynamic year
 * 
 * Features:
 * - Responsive grid layout (stacks on mobile, 4 columns on desktop)
 * - Interactive social media and service links
 * - Professional banking theme with dark background
 * - Hover effects for better user experience
 * - Accessible design with proper ARIA labels
 */

import React from 'react';
import { BanknotesIcon, HeartIcon } from '@heroicons/react/24/outline';

const Footer = () => {
     return (
          <footer className="bg-gray-900 text-white">
               <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
                    <div className="grid grid-cols-1 md:grid-cols-4 gap-8">

                         {/* Brand Section - spans 2 columns on desktop */}
                         <div className="col-span-1 md:col-span-2">
                              {/* Brand logo and name */}
                              <div className="flex items-center space-x-2 mb-4">
                                   <div className="bg-banking-600 p-2 rounded-lg flex items-center justify-center">
                                        <img
                                             src="/logo.png"
                                             alt="SecureBank Logo"
                                             className="h-6 w-6 object-contain"
                                             onError={(e) => {
                                                  // Fallback to icon if image fails to load
                                                  e.target.style.display = 'none';
                                                  e.target.nextSibling.style.display = 'block';
                                             }}
                                        />
                                        <BanknotesIcon className="h-6 w-6 text-white hidden" />
                                   </div>
                                   <span className="font-bold text-xl">SecureBank</span>
                              </div>

                              {/* Brand description */}
                              <p className="text-gray-400 mb-4 max-w-md">
                                   Your trusted partner for secure and professional banking services.
                                   Built with cutting-edge technology to keep your finances safe.
                              </p>

                              {/* Social media links with hover effects */}
                              <div className="flex space-x-4">
                                   <button type="button" className="text-gray-400 hover:text-white transition-colors" aria-label="Facebook">
                                        <svg className="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
                                             <path fillRule="evenodd" d="M22 12c0-5.523-4.477-10-10-10S2 6.477 2 12c0 4.991 3.657 9.128 8.438 9.878v-6.987h-2.54V12h2.54V9.797c0-2.506 1.492-3.89 3.777-3.89 1.094 0 2.238.195 2.238.195v2.46h-1.26c-1.243 0-1.63.771-1.63 1.562V12h2.773l-.443 2.89h-2.33v6.988C18.343 21.128 22 16.991 22 12z" clipRule="evenodd" />
                                        </svg>
                                   </button>
                                   <button type="button" className="text-gray-400 hover:text-white transition-colors" aria-label="Twitter">
                                        <svg className="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
                                             <path d="M8.29 20.251c7.547 0 11.675-6.253 11.675-11.675 0-.178 0-.355-.012-.53A8.348 8.348 0 0022 5.92a8.19 8.19 0 01-2.357.646 4.118 4.118 0 001.804-2.27 8.224 8.224 0 01-2.605.996 4.107 4.107 0 00-6.993 3.743 11.65 11.65 0 01-8.457-4.287 4.106 4.106 0 001.27 5.477A4.072 4.072 0 012.8 9.713v.052a4.105 4.105 0 003.292 4.022 4.095 4.095 0 01-1.853.07 4.108 4.108 0 003.834 2.85A8.233 8.233 0 012 18.407a11.616 11.616 0 006.29 1.84" />
                                        </svg>
                                   </button>
                                   <button type="button" className="text-gray-400 hover:text-white transition-colors" aria-label="LinkedIn">
                                        <svg className="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
                                             <path fillRule="evenodd" d="M19 0H5a5 5 0 00-5 5v14a5 5 0 005 5h14a5 5 0 005-5V5a5 5 0 00-5-5zM8 19H5V8h3v11zM6.5 6.732c-.966 0-1.75-.79-1.75-1.764s.784-1.764 1.75-1.764 1.75.79 1.75 1.764-.783 1.764-1.75 1.764zM20 19h-3v-5.604c0-3.368-4-3.113-4 0V19h-3V8h3v1.765c1.396-2.586 7-2.777 7 2.476V19z" clipRule="evenodd" />
                                        </svg>
                                   </button>
                              </div>
                         </div>

                         {/* Services Section */}
                         <div>
                              <h3 className="font-semibold text-lg mb-4">Services</h3>
                              <ul className="space-y-2 text-gray-400">
                                   <li><button type="button" className="hover:text-white transition-colors text-left">Online Banking</button></li>
                                   <li><button type="button" className="hover:text-white transition-colors text-left">Money Transfer</button></li>
                                   <li><button type="button" className="hover:text-white transition-colors text-left">Account Management</button></li>
                                   <li><button type="button" className="hover:text-white transition-colors text-left">Transaction History</button></li>
                              </ul>
                         </div>

                         {/* Support */}
                         <div>
                              <h3 className="font-semibold text-lg mb-4">Support</h3>
                              <ul className="space-y-2 text-gray-400">
                                   <li><button type="button" className="hover:text-white transition-colors text-left">Help Center</button></li>
                                   <li><button type="button" className="hover:text-white transition-colors text-left">Contact Us</button></li>
                                   <li><button type="button" className="hover:text-white transition-colors text-left">Security</button></li>
                                   <li><button type="button" className="hover:text-white transition-colors text-left">Privacy Policy</button></li>
                              </ul>
                         </div>
                    </div>

                    <div className="border-t border-gray-800 mt-8 pt-8 flex flex-col md:flex-row justify-between items-center">
                         <p className="text-gray-400 text-sm">
                              © 2025 SecureBank. All rights reserved.
                         </p>
                         <div className="flex items-center space-x-1 text-gray-400 text-sm mt-4 md:mt-0">
                              <span>Made with</span>
                              <HeartIcon className="h-4 w-4 text-red-500" />
                              <span>using Spring Boot & React</span>
                         </div>
                    </div>
               </div>
          </footer>
     );
};

export default Footer;
