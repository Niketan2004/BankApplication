package com.BankProject.BankApplication.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.BankProject.BankApplication.Entity.User;
import com.BankProject.BankApplication.Repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
     @Autowired
     private UserRepository userRepository;

     @Override
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          // System.out.println(username);
          Optional<User> user = userRepository.findUserByEmailIgnoreCase(username);// Find by email (or username)
          // System.out.println(user.isEmpty());
          // System.out.println(user.toString());
          if (user.isEmpty()) {
               throw new UsernameNotFoundException("User not found");
          }
          // System.out.println("Password from DB (Encoded): " + user.get().getPassword()); // Log the encoded password
          return org.springframework.security.core.userdetails.User.builder() // Use Spring Security's User
                    .username(user.get().getEmail())
                    .password(user.get().getPassword()) // Password should already be encoded
                    .build();
     }

}
