package com.BankProject.BankApplication.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.BankProject.BankApplication.DTOs.AuthRequest;
import com.BankProject.BankApplication.Utils.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthController {
     @Autowired
     private AuthenticationManager authenticationManager;

     @Autowired
     private JwtUtils jwtUtils;

     @PostMapping("/authenticate")
     public String generateToken(@RequestBody AuthRequest authRequest) throws Exception {
          authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
          return jwtUtils.generateToken(authRequest.getUsername());
     }

}
