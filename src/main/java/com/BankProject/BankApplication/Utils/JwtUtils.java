package com.BankProject.BankApplication.Utils;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {
     private final String SECRET = "1b96e96bc69e36e1db9de72031747e1b";
     private final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes());
     private final long EXPIRATION_TIME = 1000 * 60 * 60;

     // Generates the jwt token
     public String generateToken(String username) {
          return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();
     }

     // Exracting claims from the token
     public Claims extractClaims(String token) {
          return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
     }

     // Extracting username from the token
     public String extractUsername(String token) {
          return extractClaims(token).getSubject();
     }

     // Validating the token is expired or not
     public boolean validateToken(String username, UserDetails userDetails, String token) {
          return username.equals(userDetails.getUsername()) && !isTokenExpired(token);

     }

     // is token expired
     public boolean isTokenExpired(String token) {
          return extractClaims(token).getExpiration().before(new Date());
     }

}
