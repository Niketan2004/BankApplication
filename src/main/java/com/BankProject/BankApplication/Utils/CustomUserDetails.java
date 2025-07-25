package com.BankProject.BankApplication.Utils;

import java.util.Collection;
import java.util.List;

import com.BankProject.BankApplication.Entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

     private final User user;

     public CustomUserDetails(User user) {
          this.user = user;
     }

     @Override
     public Collection<? extends GrantedAuthority> getAuthorities() {
          return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
     }

     @Override
     public String getPassword() {
          return user.getPassword();
     }

     @Override
     public String getUsername() {
          return user.getEmail(); // or use user.getUsername()
     }

     @Override
     public boolean isAccountNonExpired() {
          return true;
     }

     @Override
     public boolean isAccountNonLocked() {
          return true;
     }

     @Override
     public boolean isCredentialsNonExpired() {
          return true;
     }

     @Override
     public boolean isEnabled() {
          return user.getIsEnabled();
     }

     public User getUser() {
          return user;
     }
}
