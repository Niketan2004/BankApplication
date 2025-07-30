package com.BankProject.BankApplication.DTOs;


import java.io.Serializable;

import com.BankProject.BankApplication.Enum.AccountType;
import com.BankProject.BankApplication.Enum.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserInfo implements Serializable {
     private String userId;
     private String fullName;
     private String email;
     private Role role;
     private Long accountNumber;
     private double balance;
     private AccountType accountType;

}
