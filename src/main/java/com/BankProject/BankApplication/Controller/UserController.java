package com.BankProject.BankApplication.Controller;

import java.nio.file.AccessDeniedException;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BankProject.BankApplication.Entity.User;
import com.BankProject.BankApplication.Service.UserService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/user")
public class UserController {
     @Autowired
     private UserService userService;

     @GetMapping("/dashboard")
     public ResponseEntity<?> userDashboard() {
          try {
               return ResponseEntity.ok().body(userService.getCurrentUserInfo());
          } catch (Exception e) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                         .body("Failed to load user dashboard: " + e.getMessage());
          }
     }

     // Updates the User with proper validations...
     @PutMapping("/{id}")
     public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) {
          try {
               return ResponseEntity
                         .status(HttpStatus.ACCEPTED)
                         .body(userService.updateUser(id, user));
          } catch (AccessDeniedException e) {
               return ResponseEntity
                         .status(HttpStatus.FORBIDDEN)
                         .body(e.getMessage());
          }
     }

     // Deletes the uer with proper validations
     @DeleteMapping("/{id}")
     public ResponseEntity<?> deleteUser(@PathVariable String id) {
          try {
               userService.deleteUser(id);
          } catch (AccessDeniedException e) {
               return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
          }
          return ResponseEntity.status(HttpStatus.OK)
                    .body("User Deleted");
     }

     // Check Account Balance

     @GetMapping("/balance")
     public ResponseEntity<?> checkBalance() {
          try {
               return ResponseEntity.ok().body(userService.accountBalance());
          } catch (AccountNotFoundException e) {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
          }
     }

     // Change password with current password validation
     @PostMapping("/change-password")
     public ResponseEntity<?> changePassword(@RequestBody Map<String, String> passwordData) {
          try {
               String currentPassword = passwordData.get("currentPassword");
               String newPassword = passwordData.get("newPassword");

               if (currentPassword == null || currentPassword.trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("Current password is required");
               }

               if (newPassword == null || newPassword.trim().isEmpty()) {
                    return ResponseEntity.badRequest().body("New password is required");
               }

               if (newPassword.length() < 6) {
                    return ResponseEntity.badRequest().body("New password must be at least 6 characters");
               }

               userService.changePassword(currentPassword, newPassword);
               return ResponseEntity.ok().body("Password changed successfully");
          } catch (Exception e) {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body("Failed to change password: " + e.getMessage());
          }
     }

}
