package com.BankProject.BankApplication.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.BankProject.BankApplication.Service.CustomUserDetailsService;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

     @Autowired
     private CustomUserDetailsService userDetailsService;

     // This is the security filter chain used to authenticate the user .
     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
          return httpSecurity
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .csrf(csrf -> csrf.disable())
                    .httpBasic(httpBasic -> {
                    })
                    .authorizeHttpRequests(http -> {
                         http.requestMatchers("/login", "/api/**", "/actuator/**").permitAll()
                                   .requestMatchers("/admin/**").hasRole("ADMIN")
                                   .requestMatchers("/user/**", "/transactions/**").hasAnyRole("USER", "ADMIN")
                                   .anyRequest().authenticated();
                    })
                    .logout(logout -> {
                         logout.logoutUrl("/logout");
                         // .logoutSuccessUrl("/login");
                    })
                    .build();
     }

     // CORS configuration
     @Bean
     public CorsConfigurationSource corsConfigurationSource() {
          CorsConfiguration configuration = new CorsConfiguration();
          configuration.setAllowedOriginPatterns(Arrays.asList("*"));
          configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
          configuration.setAllowedHeaders(Arrays.asList("*"));
          configuration.setAllowCredentials(true);

          UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
          source.registerCorsConfiguration("/**", configuration);
          return source;
     }

     // Password encoder that encodes the password in Bcrypt Password encoding
     // technique
     @Bean
     public PasswordEncoder passwordEncoder() {
          return new BCryptPasswordEncoder();
     }

     @Bean
     public DaoAuthenticationProvider daoAuthenticationProvider() {
          DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
          authProvider.setUserDetailsService(userDetailsService);
          authProvider.setPasswordEncoder(passwordEncoder());
          return authProvider;
     }
}
