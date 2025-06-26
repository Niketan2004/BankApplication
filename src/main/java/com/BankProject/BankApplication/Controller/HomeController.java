package com.BankProject.BankApplication.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BankProject.BankApplication.Service.UserService;
import com.BankProject.BankApplication.Utils.CustomUserInfo;
import com.BankProject.BankApplication.Utils.UserAccountTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
public class HomeController {

     @Autowired
     private UserService userService;

     @PostMapping("/signup")
     public ResponseEntity<CustomUserInfo> registerUser(@RequestBody UserAccountTemplate userAccountTemplate) {
          CustomUserInfo user = userService.registerUser(userAccountTemplate);
          if (user != null) {
               return ResponseEntity
                         .status(HttpStatus.CREATED)
                         .body(user);
          } else {
               return ResponseEntity
                         .status(HttpStatus.BAD_GATEWAY)
                         .body(user);
          }
     }

     @GetMapping("/dashboard")
     public String dashboard() {
          return "This is dashboard ! That is open to all ";
     }

}
