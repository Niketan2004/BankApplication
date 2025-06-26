package com.BankProject.BankApplication.Controller;

import java.nio.file.AccessDeniedException;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BankProject.BankApplication.Entity.User;
import com.BankProject.BankApplication.Service.UserService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {
     @Autowired
     private UserService userService;

     @GetMapping("/dashboard")
     public String userDashboard() {
          return "This is User dashbaord ";
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

}
