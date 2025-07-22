package com.BankProject.BankApplication.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.BankProject.BankApplication.Entity.Account;
@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
     Double findBalanceByAccountNumber(Long accountNumber);

    
}
