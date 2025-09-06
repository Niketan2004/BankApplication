package com.BankProject.BankApplication.Service;

import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.BankProject.BankApplication.DTOs.UserAccountTemplate;
import com.BankProject.BankApplication.Entity.Account;
import com.BankProject.BankApplication.Entity.User;
import com.BankProject.BankApplication.Repository.AccountRepository;

@Service
public class AccountService {
     @Autowired
     private AccountRepository accountRepository;

     // Cache manager bean used for the caching purpose
     @Autowired
     private CacheManager cacheManager;



     // creating a new account when new user is registered
     public Account createAccount(UserAccountTemplate userAccountTemplate, User user) {
          // Cache for the account
          Cache accountCache = cacheManager.getCache("accounts");
          Account account = new Account();
          account.setBalance(userAccountTemplate.getBalance());
          account.setAccountType(userAccountTemplate.getAccountType());
          // setting a user to an account
          account.setUser(user);
          // saving account into database
          accountRepository.save(account);
          if (accountCache != null) {
               accountCache.put(account.getAccountNumber(), Account.class);
          }
          return account;
     }

     // deleting the account when user is deleted
     public boolean deleteAccount(Account account) {
          // Cache for the account
          Cache accountCache = cacheManager.getCache("accounts");
          if (accountRepository.existsById(account.getAccountNumber())) {
               accountRepository.delete(account);
               // deleting from the cache memory
               if (accountCache != null) {
                    accountCache.evict(account.getAccountNumber());
               }
               return true;
          }
          return false;
     }

     // CHECK ACOUNT BALANCE
     public Double checkBalance(Long accountNumber) throws AccountNotFoundException {
          // cache for the balance
          Cache balanceCache = cacheManager.getCache("balances");
          Double balance = accountRepository.findById(accountNumber).get().getBalance();
          if (balanceCache != null) {
               balanceCache.put(accountNumber, balance);// saving balance into cache
          }
          return balance;
     }

}
