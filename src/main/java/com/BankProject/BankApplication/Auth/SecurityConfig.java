package com.BankProject.BankApplication.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.BankProject.BankApplication.Filters.JwtAuthFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

     // @Autowired
     // private CustomUserDetailsService userDetailsService;

     @Autowired
     private JwtAuthFilter JwtAuthFilter;

     // This is the security filter chain used to authenticate the user .
     @Bean
     public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
          return httpSecurity
                    .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(http -> {
                         http.requestMatchers("/login", "/api/**", "/actuator/**", "/authenticate", "/user/verify").permitAll()
                                   .requestMatchers("/admin/**").hasRole("ADMIN")
                                   .requestMatchers("/user/**", "/transactions/**").hasAnyRole("USER", "ADMIN")
                                   .anyRequest().authenticated();
                    })
                    .addFilterBefore(JwtAuthFilter,
                              UsernamePasswordAuthenticationFilter.class)
                    .logout(logout -> {
                         logout.logoutUrl("/logout");
                         // .logoutSuccessUrl("/login");
                    })
                    .build();
     }

     // CORS configuration
     @Value("${app.cors.allowed-origins}")
     private String allowedOrigins;

     @Bean
     public CorsConfigurationSource corsConfigurationSource() {
          CorsConfiguration configuration = new CorsConfiguration();

          List<String> origins = Arrays.asList(allowedOrigins.split(","));
          configuration.setAllowedOriginPatterns(origins);
          configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
          configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
          configuration.setAllowCredentials(true);
          configuration.setMaxAge(3600L);

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
     public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
          return configuration.getAuthenticationManager();
     }
}
