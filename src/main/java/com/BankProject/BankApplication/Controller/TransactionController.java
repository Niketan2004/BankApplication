package com.BankProject.BankApplication.Controller;

import javax.naming.directory.InvalidAttributesException;
import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.BankProject.BankApplication.Entity.Transactions;
import com.BankProject.BankApplication.Service.TransactionService;
import com.BankProject.BankApplication.Utils.TransferSlip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
     @Autowired
     private TransactionService transactionService;

     // Displays transaction history
     @GetMapping("/history")
     public ResponseEntity<Page<Transactions>> getTransactions(
               @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size) {
          Page<Transactions> transactions = transactionService.checkTransactionHistory(page, size);
          return ResponseEntity.ok(transactions);
     }

     @PostMapping("/deposit")
     public ResponseEntity<?> deposit(@RequestBody Double amount) {
          try {
               Transactions transactions = transactionService.deposit(amount);
               return ResponseEntity.status(HttpStatus.OK).body(transactions);

          } catch (Exception e) {

               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
          }
     }

     @PostMapping("/withdraw")
     public ResponseEntity<?> withdraw(@RequestBody Double amount) {
          try {
               Transactions transactions = transactionService.withdraw(amount);
               return ResponseEntity.status(HttpStatus.OK).body(transactions);

          } catch (Exception e) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

          }
     }

     @PostMapping("/transfer")
     public ResponseEntity<?> transferAmount(@RequestBody TransferSlip transferSlip) {
          try {
               Transactions transactions = transactionService.transferAmount(transferSlip);
               return ResponseEntity.status(HttpStatus.OK).body(transactions);
          } catch (AccountNotFoundException e) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

          } catch (InvalidAttributesException e) {
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

          }

     }

}
