package com.BankProject.BankApplication.Entity;

import java.io.Serializable;
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
public class VerificationToken implements Serializable {

     @Id
     @GeneratedValue(strategy = GenerationType.AUTO)
     private Long id;

     private String token;

     @OneToOne
     private User user;

     private LocalDateTime expiryDate;

}
