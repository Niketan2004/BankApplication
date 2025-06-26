package com.BankProject.BankApplication.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.BankProject.BankApplication.Entity.Transactions;

public interface TransactionRepository extends JpaRepository<Transactions, String> {
     Page<Transactions> findByAccount_AccountNumber(Long accountNumber, Pageable pageable);

}
