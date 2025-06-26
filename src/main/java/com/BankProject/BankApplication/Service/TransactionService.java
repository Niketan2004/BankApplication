package com.BankProject.BankApplication.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import javax.naming.directory.InvalidAttributesException;
import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BankProject.BankApplication.Entity.Account;
import com.BankProject.BankApplication.Entity.Transactions;
import com.BankProject.BankApplication.Entity.User;
import com.BankProject.BankApplication.Enum.TransactionTypes;
import com.BankProject.BankApplication.Exceptions.InsufficientAmountException;
import com.BankProject.BankApplication.Exceptions.UserNotFoundException;
import com.BankProject.BankApplication.Repository.AccountRepository;
import com.BankProject.BankApplication.Repository.TransactionRepository;
import com.BankProject.BankApplication.Repository.UserRepository;
import com.BankProject.BankApplication.Utils.TransactionResponseDTO;
import com.BankProject.BankApplication.Utils.TransferSlip;

@Service
public class TransactionService {

     @Autowired
     private TransactionRepository transactionRepository;

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private AccountRepository accountRepository;

     // Logic behind getting all the transaction history
     public Page<TransactionResponseDTO> checkTransactionHistory(int page, int size) {
          User user = findUser();
          Pageable pageable = PageRequest.of(page, size);

          return transactionRepository.findByAccount_AccountNumber(user.getAccount().getAccountNumber(), pageable)
                    .map(transaction -> new TransactionResponseDTO(
                              transaction.getTransactionId(),
                              transaction.getAmount(),
                              transaction.getType(),
                              transaction.getTime(),
                              transaction.getAccount().getAccountNumber()));

     }

     // Deposit Amount
     public TransactionResponseDTO deposit(Double amount) throws IllegalArgumentException {
          if (amount < 0) {
               throw new IllegalArgumentException("Amount should not be negative");
          }
          User user = findUser();
          Account account = user.getAccount();
          account.setBalance(account.getBalance() + amount);
          accountRepository.save(account);
          return createTransactions(account, amount, TransactionTypes.DEPOSIT);

     }

     // Withdraw amount
     public TransactionResponseDTO withdraw(Double amount)
               throws IllegalArgumentException, InsufficientAmountException {
          if (amount < 0) {
               throw new IllegalArgumentException("Amount should be greater than 0");
          }
          User user = findUser();
          Account account = user.getAccount();
          if (amount > account.getBalance()) {
               throw new InsufficientAmountException("Insufficient amount");
          }
          account.setBalance(account.getBalance() - amount);
          accountRepository.save(account);
          return createTransactions(account, amount, TransactionTypes.WITHDRAW);
     }

     // transfer amount
     @Transactional
     public TransactionResponseDTO transferAmount(TransferSlip transferSlip)
               throws AccountNotFoundException, InvalidAttributesException, AccessDeniedException {
          if (transferSlip.getSenderAccountNumber() == null || transferSlip.getRecieverAccountNumber() == null
                    || transferSlip.getAmount() < 1.0) {
               throw new InvalidAttributesException("Please give valid data!");
          }

          String email = SecurityContextHolder.getContext().getAuthentication().getName();

          // this is the account of the sender
          Account senderAccount = accountRepository.findById(transferSlip.getSenderAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException(
                              "Account with given account number is not found "
                                        + transferSlip.getSenderAccountNumber()));

          if (!senderAccount.getUser().getEmail().equals(email)) {
               throw new AccessDeniedException("You can only transfer from your account");
          }
          // this is the account of the reciever
          Account recieverAccount = accountRepository.findById(transferSlip.getRecieverAccountNumber())
                    .orElseThrow(() -> new AccountNotFoundException(
                              "Account with given account number is not found "
                                        + transferSlip.getRecieverAccountNumber()));
          if (recieverAccount.getUser().getEmail().equals(email)) {
               throw new AccessDeniedException(
                         "You can not transfer into your same account");
          }

          // Started transactions
          // first amount will be withdrawn from senders account
          // checks the is there is sufficient amount in the senders account
          if (senderAccount.getBalance() < transferSlip.getAmount()) {
               throw new InsufficientAmountException("Insufficient amount!");
          }
          // SETTING SENDERS ACCOUNT BALANCE
          senderAccount.setBalance(senderAccount.getBalance() - transferSlip.getAmount());
          // STTING RECIVERS ACCOUNT BALANCE
          recieverAccount.setBalance(recieverAccount.getBalance() + transferSlip.getAmount());
          // SAVING SENDERS ACCOUNT INTO DATABASE
          accountRepository.save(senderAccount);
          // creating transaction for the senders account
          TransactionResponseDTO senderTransactions = createTransactions(senderAccount, transferSlip.getAmount(),
                    TransactionTypes.TRANSFER);
          accountRepository.save(recieverAccount);
          // CREATED TRANSACTION FOR THE RECIEVERS ACCOUNT
          createTransactions(recieverAccount, transferSlip.getAmount(), TransactionTypes.CREDIT);

          // RETURNS SENDERS TRANSACTIONS DETAILS
          return senderTransactions;

     }

     // Initiates transaction
     private TransactionResponseDTO createTransactions(Account account, Double amount, TransactionTypes type) {
          // Record transaction
          Transactions transaction = new Transactions();
          transaction.setAccount(account);
          transaction.setType(type);
          transaction.setAmount(amount);
          transaction.setTime(LocalDateTime.now());
          transactionRepository.save(transaction); // Save transaction record

          TransactionResponseDTO transactionResponseDTO = new TransactionResponseDTO();
          transactionResponseDTO.setAmount(amount);
          transactionResponseDTO.setTime(transaction.getTime());
          transactionResponseDTO.setTransactionId(transaction.getTransactionId());
          transactionResponseDTO.setType(transaction.getType());

          transactionResponseDTO.setAccountNumber(account.getAccountNumber());
          return transactionResponseDTO;

     }

     // Finds the respective user
     private User findUser() {
          String email = SecurityContextHolder.getContext().getAuthentication().getName();
          return userRepository.findUserByEmailIgnoreCase(email)
                    .orElseThrow(() -> new UserNotFoundException("user for the given email  " + email + " not found"));
     }

}
