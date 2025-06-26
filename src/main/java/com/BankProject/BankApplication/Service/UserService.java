package com.BankProject.BankApplication.Service;

import java.nio.file.AccessDeniedException;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BankProject.BankApplication.Entity.User;
import com.BankProject.BankApplication.Exceptions.UserAlreadyExistsException;
import com.BankProject.BankApplication.Exceptions.UserNotFoundException;
import com.BankProject.BankApplication.Repository.UserRepository;
import com.BankProject.BankApplication.Utils.CustomUserInfo;
import com.BankProject.BankApplication.Utils.UserAccountTemplate;

@Service
public class UserService {
     // Password encoder is declare to encode the raw password entered by the user
     @Autowired
     private PasswordEncoder passwordEncoder;
     // Connected User repository to perform database Operations
     @Autowired
     private UserRepository userRepository;
     @Autowired
     private AccountService accountService;

     // ====================USER SIDE FUNCTIONALITIES=============================

     // registers a new user
     @Transactional
     public CustomUserInfo registerUser(UserAccountTemplate userAccountTemplate) throws UserAlreadyExistsException {
          if (userRepository.findUserByEmailIgnoreCase(userAccountTemplate.getEmail()).isEmpty()) {
               User user = new User();
               // setting users details
               user.setFullName(userAccountTemplate.getFullName());
               user.setEmail(userAccountTemplate.getEmail());
               user.setPassword(passwordEncoder.encode(userAccountTemplate.getPassword()));
               user.setRole(userAccountTemplate.getRole());
               User savedUser = userRepository.save(user);
               // setting an account to the user
               savedUser.setAccount(accountService.createAccount(userAccountTemplate, savedUser));
               // saving user into database
               savedUser = userRepository.save(savedUser);
               return createCustomUserInfo(savedUser);
          }
          throw new UserAlreadyExistsException("User with email " + userAccountTemplate.getEmail() + " already exists");
     }

     // UPDATE THE EXISTING USER
     public CustomUserInfo updateUser(String id, User updatedUser) throws AccessDeniedException {
          // FINDS THE USER BY THE ID
          User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("user for the given id " + id + " not found"));
          if (validateUser(id)) {
               // IF USER'S FULL NAME IS PROVIDED THEN UPDATES IT IF NOT THEN KEEP THE EXISTING
               // ONE
               if (updatedUser.getFullName() != null && !updatedUser.getFullName().isEmpty()) {
                    existingUser.setFullName(updatedUser.getFullName());
               }
               // IF USER'S EMAIL IS PROVIDED THEN UPDATES IT IF NOT THEN KEEP THE EXISTING ONE
               if (updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty()) {
                    existingUser.setEmail((updatedUser.getEmail()));
               }
               // IF USER'S PASSWORD IS PROVIDED THEN UPDATES IT WITH ENCODING, IF NOT THEN
               // KEEP THE EXISTING ONE
               if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
               }
               // SAVES THE UPDATED USER INTO DATABASE
               userRepository.save(existingUser);
               return createCustomUserInfo(existingUser);
          } else {
               throw new AccessDeniedException("You can only edit Your account");
          }
     }

     // deletes the user from the database

     public Boolean deleteUser(String id) throws AccessDeniedException {
          if (validateUser(id)) {
               userRepository.deleteById(id);
               return true;
          } else {
               throw new AccessDeniedException("You can only delete Your account");
          }
     }

     // checking acount balance
     public double accountBalance() throws AccountNotFoundException {
          String email = SecurityContextHolder.getContext().getAuthentication().getName();
          User user = userRepository.findUserByEmailIgnoreCase(email)
                    .orElseThrow(() -> new UserNotFoundException("user for the given email  " + email + " not found"));

          return accountService.checkBalance(user.getAccount().getAccountNumber());
     }

     private Boolean validateUser(String id) {
          // GETS THE USER BY THE ID
          User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("user for the given id " + id + " not found"));
          // FINDS LOGGED IN USER
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          // GET THE LOGGED IN USERS ID i.e EMAIL
          String email = authentication.getName();
          /*
           * IF THE LOGGED IN USER'S EAMIL IS SAME AS THE USER RETRIEVED BY THE ID, ONLY
           * THEN THE USER IS DELETED IF NOT THEN IT THROWS THE ERROR
           * "ACCESSDENIED"..BECAUSE
           * ONLY THE USER LOGGED IN HAS THE PERMISSION TO DELTES HIS OWN ACCOUNT NOT
           * THE OTHER ACCOUNT.
           */
          return user.getEmail().equals(email);
     }

     private CustomUserInfo createCustomUserInfo(User user) {
          // CREATED CUSTOM USER INFO THAT SHOULD BE DISPLAYED TO THE USER
          // THIS HELPS TO HIDE SENSITIVE DATA AND ONLY SHOW IMPORTANT DATA TO THE USER
          CustomUserInfo customUserInfo = new CustomUserInfo();
          customUserInfo.setUserId(user.getUserId());
          customUserInfo.setFullName(user.getFullName());
          customUserInfo.setEmail(user.getEmail());
          customUserInfo.setRole(user.getRole());
          customUserInfo.setAccountNumber(user.getAccount().getAccountNumber());
          customUserInfo.setBalance(user.getAccount().getBalance());
          customUserInfo.setAccountType(user.getAccount().getAccountType());
          return customUserInfo;
     }
     // ======================================================================

     // =====================ADMIN SIDE
     // FUNCTIONALITIES==============================================

     // Get current user info for dashboard
     public CustomUserInfo getCurrentUserInfo() {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          String email = authentication.getName();

          User user = userRepository.findUserByEmailIgnoreCase(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

          return createCustomUserInfo(user);
     }

     // Find user by email
     public User findUserByEmail(String email) {
          return userRepository.findUserByEmailIgnoreCase(email)
                    .orElse(null);
     }

     // Change password with current password validation
     public void changePassword(String currentPassword, String newPassword) {
          Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
          String email = authentication.getName();

          User user = userRepository.findUserByEmailIgnoreCase(email)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));

          // Verify current password
          if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
               throw new IllegalArgumentException("Current password is incorrect");
          }

          // Update password
          user.setPassword(passwordEncoder.encode(newPassword));
          userRepository.save(user);
     }

}
