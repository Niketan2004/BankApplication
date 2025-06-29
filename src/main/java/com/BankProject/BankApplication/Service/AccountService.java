package com.BankProject.BankApplication.Service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BankProject.BankApplication.DTOs.UserAccountTemplate;
import com.BankProject.BankApplication.Entity.Account;
import com.BankProject.BankApplication.Entity.User;
import com.BankProject.BankApplication.Repository.AccountRepository;

@Service
public class AccountService {
     @Autowired
     private AccountRepository accountRepository;


     // creating a new account when new user is registered
     public Account createAccount(UserAccountTemplate userAccountTemplate, User user) {
          Account account = new Account();
          account.setBalance(userAccountTemplate.getBalance());
          account.setAccountType(userAccountTemplate.getAccountType());
          // setting a user to an account
          account.setUser(user);
          // saving account into database
          accountRepository.save(account);
          return account;
     }

     // deleting the account when user is deleted
     public boolean deleteAccount(Account account) {
          if (accountRepository.existsById(account.getAccountNumber())) {
               accountRepository.delete(account);
               return true;
          }
          return false;
     }

     // CHECK ACOUNT BALANCE
     public Double checkBalance(Long accountNumber) throws AccountNotFoundException {
          return accountRepository.findById(accountNumber).get().getBalance();
     }

}
