package com.BankProject.BankApplication.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.BankProject.BankApplication.Entity.Account;

public interface AccountRepository extends JpaRepository<Account,Long> {
     Double findBalanceByAccountNumber(Long accountNumber);

    
}
