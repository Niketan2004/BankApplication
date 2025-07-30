package com.BankProject.BankApplication.DTOs;



import java.io.Serializable;

import com.BankProject.BankApplication.Enum.AccountType;
import com.BankProject.BankApplication.Enum.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountTemplate implements Serializable {
     private String fullName;
     private String email;
     private String password;
     private Role role;
     private double balance;
     private AccountType accountType;
}
