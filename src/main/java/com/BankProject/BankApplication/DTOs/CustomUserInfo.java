package com.BankProject.BankApplication.DTOs;


import com.BankProject.BankApplication.Enum.AccountType;
import com.BankProject.BankApplication.Enum.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserInfo {
     private String userId;
     private String fullName;
     private String email;
     private Role role;
     private Long accountNumber;
     private double balance;
     private AccountType accountType;

}
