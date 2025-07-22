package com.BankProject.BankApplication.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BankProject.BankApplication.DTOs.ApiResponse;
import com.BankProject.BankApplication.DTOs.CustomUserInfo;
import com.BankProject.BankApplication.DTOs.UserAccountTemplate;
import com.BankProject.BankApplication.Service.UserService;

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

     private ApiResponse apiResponse = new ApiResponse<>();
     
 

     @PostMapping("/signup")
     public ResponseEntity<ApiResponse> registerUser(@RequestBody UserAccountTemplate userAccountTemplate) {
          CustomUserInfo user = userService.registerUser(userAccountTemplate);
          if (user != null) {
               apiResponse.setMessage(
                         "User Registered Successfully and Verification link sent to the respective email!");
               apiResponse.setData(user);
               return ResponseEntity.ok(apiResponse);
          } else {
               apiResponse.setMessage("Failed to register User! Please try again");
               apiResponse.setData(user);
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
          }
     }

     @GetMapping("/dashboard")
     public String dashboard() {
          return "This is dashboard ! That is open to all ";
     }

}
