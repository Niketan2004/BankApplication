package com.BankProject.BankApplication.DTOs;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.BankProject.BankApplication.Enum.TransactionTypes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDTO implements Serializable {
     private String transactionId;
     private Double amount;
     private TransactionTypes type;
     private LocalDateTime time;
     private Long accountNumber;
}
