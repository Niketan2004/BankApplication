package com.BankProject.BankApplication.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
               verificationToken.setExpiryDate(LocalDateTime.now().plusHours(2));
               verificationTokenRepository.save(verificationToken);
               emailService.sendVerificationEmail(savedUser.getEmail(), token);

               return createCustomUserInfo(savedUser);
          }
          throw new UserAlreadyExistsException("User with email " + userAccountTemplate.getEmail() + " already exists");
     }

     // UPDATE THE EXISTING USER

     // @Caching(evict = { // Use @Caching to group multiple @CacheEvict annotations
     //           @CacheEvict(value = "currentUserInfo", key = "#root.target.findCurrentUserEmail()", beforeInvocation = true),
     //           @CacheEvict(value = "users", key = "#existingUser.email", beforeInvocation = true)
     // })
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
     // @CacheEvict(value = { "currentUserInfo", "users" }, allEntries = true)
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
     // @Cacheable(value = "accountBalance", key = "#root.target.findCurrentUserEmail()")
     public double accountBalance() throws AccountNotFoundException { 
          User user = userRepository.findUserByEmailIgnoreCase(findCurrentUserEmail())
                    .orElseThrow(() -> new UserNotFoundException(
                              "user for the given email  " + findCurrentUserEmail() + " not found"));
                              
                              log.info("called balance method ");
          return accountService.checkBalance(user.getAccount().getAccountNumber());
     }

     private Boolean validateUser(String id) {
          // GETS THE USER BY THE ID
          User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("user for the given id " + id + " not found"));
          /*
           * IF THE LOGGED IN USER'S EAMIL IS SAME AS THE USER RETRIEVED BY THE ID, ONLY
           * THEN THE USER IS DELETED IF NOT THEN IT THROWS THE ERROR
           * "ACCESSDENIED"..BECAUSE
           * ONLY THE USER LOGGED IN HAS THE PERMISSION TO DELTES HIS OWN ACCOUNT NOT
           * THE OTHER ACCOUNT.
           */
          return user.getEmail().equals(findCurrentUserEmail());
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
     // @Cacheable(value = "currentUserInfo", key = "#root.target.findCurrentUserEmail()")
     public CustomUserInfo getCurrentUserInfo() {
          User user = userRepository.findUserByEmailIgnoreCase(findCurrentUserEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
          return createCustomUserInfo(user);
     }

     // Find user by email
     // @Cacheable(value = "users", key = "#email")
     public User findUserByEmail(String email) {
          return userRepository.findUserByEmailIgnoreCase(email)
                    .orElse(null);
     }

     // Change password with current password validation
     // @Caching(evict = {
     //           @CacheEvict(value = "currentUserInfo", key = "#root.target.findCurrentUserEmail()", beforeInvocation = true),
     //           @CacheEvict(value = "users", key = "#root.target.findCurrentUserEmail()", beforeInvocation = true)
     // })
     public void changePassword(String currentPassword, String newPassword) {
          // get the user by email
          User user = userRepository.findUserByEmailIgnoreCase(findCurrentUserEmail())
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
          // Verify current password
          if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
               throw new IllegalArgumentException("Current password is incorrect");
          }
          // Update password
          user.setPassword(passwordEncoder.encode(newPassword));
          userRepository.save(user);
     }

     // @Cacheable(value = "allUsers", key = "#page + '-' + #size")
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
     // @Caching(evict = {
     //           @CacheEvict(value = "currentUserInfo", key = "#user.email", beforeInvocation = true),
     //           @CacheEvict(value = "users", key = "#user.email", beforeInvocation = true)
     // })
     public ResponseEntity<?> verifyToken(String token) {
          VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
          if (verificationToken != null) {

               if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
                    return ResponseEntity.badRequest().body("Token expired");
               }

               User user = verificationToken.getUser(); // gets the user from the token to enable the user.
               user.setIsEnabled(true);
               userRepository.save(user);
               return ResponseEntity.ok().body("User Verified Successfully!");
          }

          return ResponseEntity.badRequest().body("Invalid token!");

     }

     // finds user email
     public String findCurrentUserEmail() {
          return SecurityContextHolder.getContext().getAuthentication().getName();
     }
     // OTHER FUNCTIONALITIES SUCH AS DELETE AND CREATE USER IS APLLICABLE SAME AS
     // THE IUSER METHODS
}
