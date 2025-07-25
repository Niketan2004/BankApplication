package com.BankProject.BankApplication.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BankProject.BankApplication.DTOs.CustomUserInfo;
import com.BankProject.BankApplication.DTOs.UserAccountTemplate;
import com.BankProject.BankApplication.Entity.User;
import com.BankProject.BankApplication.Entity.VerificationToken;
import com.BankProject.BankApplication.Enum.Role;
import com.BankProject.BankApplication.Exceptions.UserAlreadyExistsException;
import com.BankProject.BankApplication.Exceptions.UserNotFoundException;
import com.BankProject.BankApplication.Repository.UserRepository;
import com.BankProject.BankApplication.Repository.VerificationTokenRepository;

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
     @Autowired
     private EmailService emailService;

     @Autowired
     private VerificationTokenRepository verificationTokenRepository;
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
               user.setIsEnabled(false);
               User savedUser = userRepository.save(user);
               // setting an account to the user
               savedUser.setAccount(accountService.createAccount(userAccountTemplate, savedUser));
               // saving user into database
               savedUser = userRepository.save(savedUser);

               // -=================== Email verification ========================
               String token = UUID.randomUUID().toString();
               VerificationToken verificationToken = new VerificationToken();
               verificationToken.setToken(token);
               verificationToken.setUser(savedUser);
               verificationToken.setExpiryDate(LocalDateTime.now().plusHours(12));
               verificationTokenRepository.save(verificationToken);
               emailService.sendVerificationEmail(savedUser.getEmail(), token);

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
          boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
          if (isAdmin || validateUser(id)) {
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

          // Check if user has an account (regular users have accounts, admins don't)
          if (user.getAccount() != null) {
               customUserInfo.setAccountNumber(user.getAccount().getAccountNumber());
               customUserInfo.setBalance(user.getAccount().getBalance());
               customUserInfo.setAccountType(user.getAccount().getAccountType());
          } else {
               // For admin users who don't have accounts
               customUserInfo.setAccountNumber(null);
               customUserInfo.setBalance(0.0);
               customUserInfo.setAccountType(null);
          }

          return customUserInfo;
     }

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

     // ======================================================================

     // =====================ADMIN SIDE FUNCTIONALITIES========================

     public Page<CustomUserInfo> getAllUsers(int page, int size) {
          Pageable pageable = PageRequest.of(page, size);
          Page<User> userPage = userRepository.findAll(pageable);
          List<CustomUserInfo> filteredUsers = userPage
                    .stream()
                    .filter(user -> user.getRole() == Role.USER && user.getAccount() != null)
                    .map(user -> new CustomUserInfo(
                              user.getUserId(),
                              user.getFullName(),
                              user.getEmail(),
                              user.getRole(),
                              user.getAccount().getAccountNumber(),
                              user.getAccount().getBalance(),
                              user.getAccount().getAccountType()))
                    .toList();
          return new PageImpl<>(filteredUsers, pageable, filteredUsers.size());
     }

     // verifies the token sent from the email
     public ResponseEntity<?> verifyToken(String token) {
          VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
          if (verificationToken != null) {

               if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                    return ResponseEntity.badRequest().body("Token expired");
               }

               User user = verificationToken.getUser(); // gets the user from the token to enable the user.
               user.setIsEnabled(true);
               userRepository.save(user);
               return ResponseEntity.ok().body("User Verified Succesfully!");

          }

          return ResponseEntity.badRequest().body("Invalid token!");

     }

     // OTHER FUNCTIONALITIES SUCH AS DELETE AND CREATE USER IS APLLICABLE SAME AS
     // THE IUSER METHODS
}
