package com.BankProject.BankApplication.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.BankProject.BankApplication.DTOs.AuthRequest;
import com.BankProject.BankApplication.Entity.User;
import com.BankProject.BankApplication.Repository.UserRepository;
import com.BankProject.BankApplication.Utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthController {
     @Autowired
     private AuthenticationManager authenticationManager;

     @Autowired
     private JwtUtils jwtUtils;

     @Autowired
     private UserRepository userRepository;

     @PostMapping("/authenticate")
     
     public ResponseEntity<?> generateJwtToken(@RequestBody AuthRequest authRequest) throws Exception {
          // Step 1: Fetch user from DB
          User user = userRepository.findUserByEmailIgnoreCase(authRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

          // Step 2: Check if user has verified email
          if (!user.getIsEnabled()) {
               return ResponseEntity
                         .status(HttpStatus.FORBIDDEN)
                         .body("Your account is not verified. Please check your email for the verification link.");
          }
          // authenticate the user
          authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
          // returns the generated jwt token
          return ResponseEntity.ok().body(jwtUtils.generateToken(authRequest.getUsername()));
     }

}
