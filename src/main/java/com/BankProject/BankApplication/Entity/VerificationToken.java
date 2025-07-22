package com.BankProject.BankApplication.Entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class VerificationToken {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;

     private String token;

     @OneToOne
     private User user;

     private LocalDateTime expiryDate;

}
