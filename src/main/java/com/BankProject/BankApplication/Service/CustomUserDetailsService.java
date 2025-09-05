package com.BankProject.BankApplication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.BankProject.BankApplication.Entity.User;
import com.BankProject.BankApplication.Repository.UserRepository;
import com.BankProject.BankApplication.Utils.CustomUserDetails;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
     @Autowired
     private UserRepository userRepository;

     @Override
     // @Cacheable(value = "userdetails", key = "#username")
     public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

          User user = userRepository.findUserByEmailIgnoreCase(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
          log.info("custom userdetails service is callled");
          return new CustomUserDetails(user);
     }

}
