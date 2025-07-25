package com.BankProject.BankApplication.Filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.BankProject.BankApplication.Service.CustomUserDetailsService;
import com.BankProject.BankApplication.Utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

     @Autowired
     private JwtUtils jwtUtils;

     @Autowired
     private CustomUserDetailsService customUserDetailsService;

     @Override
     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
               throws ServletException, IOException {
          // getting authheader from the authorization
          String authHeader = request.getHeader("Authorization");
          String token = null;
          String username = null;
          if (authHeader != null && authHeader.startsWith("Bearer ")) {
               // setting the token
               token = authHeader.substring(7);
               // extracting the username from the jwt token
               username = jwtUtils.extractUsername(token);

          }
          
          // geting userdtails from the jwt token
          if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
               UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

               if (jwtUtils.validateToken(username, userDetails, token)) {
                    // creating username password auth token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                              null, userDetails.getAuthorities());
                    // Setting up the details of the authtoken
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
               }

          }
          filterChain.doFilter(request, response);
     }

}
