package com.BankProject.BankApplication.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;
import javax.security.auth.login.AccountNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.BankProject.BankApplication.DTOs.TransactionResponseDTO;
import com.BankProject.BankApplication.DTOs.TransferSlip;
import com.BankProject.BankApplication.Entity.Account;
import com.BankProject.BankApplication.Entity.Transactions;
import com.BankProject.BankApplication.Entity.User;
import com.BankProject.BankApplication.Enum.TransactionTypes;
import com.BankProject.BankApplication.Exceptions.InsufficientAmountException;
import com.BankProject.BankApplication.Exceptions.UserNotFoundException;
import com.BankProject.BankApplication.Repository.AccountRepository;
import com.BankProject.BankApplication.Repository.TransactionRepository;
import com.BankProject.BankApplication.Repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionService {

     @Autowired
     private TransactionRepository transactionRepository;

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private AccountRepository accountRepository;

     // cache manager
     @Autowired
     private CacheManager cacheManager;



     // Logic behind getting all the transaction history
     public Page<TransactionResponseDTO> checkTransactionHistory(int page, int size) {
          // transaction cache
          Cache txCache = cacheManager.getCache("transactions");
          User user = findUser();
          log.info("Checking transaction history of user {}", user.getEmail());
          Pageable pageable = PageRequest.of(page, size);
          if (txCache != null) {
               // get the list of transactions if available in cache memory
               List<Transactions> txList = txCache.get(user.getAccount().getAccountNumber(), List.class);
               // check if the transactions list is availabe for the respective account
               if (txList != null && !txList.isEmpty()) {
                    log.info("Returnig the list of transactions from the cache memory for the user {}",
                              user.getEmail());
                    // Applying pagination manually
                    int start = Math.min((int) pageable.getOffset(), txList.size()); // index of the first record for
                                                                                     // the current page
                    int end = Math.min((start + pageable.getPageSize()), txList.size()); // the index where the page
                                                                                         // should end
                    // converting it into pagiable of the TransactionResponseDto
                    List<TransactionResponseDTO> responseList = txList.subList(start, end)
                              .stream()
                              .map(transaction -> new TransactionResponseDTO(
                                        transaction.getTransactionId(),
                                        transaction.getAmount(),
                                        transaction.getType(),
                                        transaction.getTime(),
                                        transaction.getAccount().getAccountNumber()))
                              .toList();
                              // returning the response of the pageable
                    return new PageImpl<>(responseList, pageable, txList.size());
               }
          }
          log.info("Cache failed : Calling DB for the transaction history of the user {}", user.getEmail());
          return transactionRepository
                    .findByAccount_AccountNumber(user.getAccount().getAccountNumber(), pageable)
                    .map(transaction -> new TransactionResponseDTO(
                              transaction.getTransactionId(),
                              transaction.getAmount(),
                              transaction.getType(),
                              transaction.getTime(),
                              transaction.getAccount().getAccountNumber()));
     }

     // Deposit Amount
     @Transactional
     public TransactionResponseDTO deposit(Double amount) throws IllegalArgumentException {
          
          // balance cache
          Cache balanceCache = cacheManager.getCache("balances");
          // account cache
          Cache accountCache = cacheManager.getCache("accounts");
          if (amount < 0) {
               log.error("Amount should not  Negative!");
               throw new IllegalArgumentException("Amount should not be negative");
          }
          User user = findUser();
          Account account = user.getAccount();
          account.setBalance(account.getBalance() + amount);
          accountRepository.save(account);
          if (accountCache !=null && balanceCache != null) {
               accountCache.put(account.getAccountNumber(), account);
               balanceCache.put(account.getAccountNumber(), account.getBalance());
               
          }

          return createTransactions(account, amount, TransactionTypes.DEPOSIT);
     }

     // Withdraw amount
     @Transactional
     public TransactionResponseDTO withdraw(Double amount)
               throws IllegalArgumentException, InsufficientAmountException {
                    
          // balance cache
          Cache balanceCache = cacheManager.getCache("balances");
          // account cache
          Cache accountCache = cacheManager.getCache("accounts");
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
          if (accountCache != null && balanceCache != null) {
               accountCache.put(account.getAccountNumber(), account);
               balanceCache.put(account.getAccountNumber(), account.getBalance());

          }
          return createTransactions(account, amount, TransactionTypes.WITHDRAW);
     }

     // transfer amount
     @Transactional
     public TransactionResponseDTO transferAmount(TransferSlip transferSlip)
               throws AccountNotFoundException, InvalidAttributesException, AccessDeniedException {
          if (transferSlip.getSenderAccountNumber() == null || transferSlip.getRecieverAccountNumber() == null
                    || transferSlip.getAmount() < 1.0) {
               log.error("Invalid data {}", transferSlip);
               throw new InvalidAttributesException("Please give valid data!");
          }
          String email = findUserEmail();
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
          // SETTING RECIVERS ACCOUNT BALANCE
          recieverAccount.setBalance(recieverAccount.getBalance() + transferSlip.getAmount());
          // SAVING SENDERS ACCOUNT INTO DATABASE
          accountRepository.save(senderAccount);
          // creating transaction for the senders account
          TransactionResponseDTO senderTransactions = createTransactions(senderAccount, transferSlip.getAmount(),
                    TransactionTypes.TRANSFER);
          accountRepository.save(recieverAccount);
          // CREATED TRANSACTION FOR THE RECIEVERS ACCOUNT
          createTransactions(recieverAccount, transferSlip.getAmount(), TransactionTypes.CREDIT);
          
          // balance cache
          Cache balanceCache = cacheManager.getCache("balances");
          // account cache
          Cache accountCache = cacheManager.getCache("accounts");
          // adding it to cache
          if (accountCache != null && balanceCache != null) {
               accountCache.put(senderAccount.getAccountNumber(), senderAccount);
               accountCache.put(recieverAccount.getAccountNumber(), recieverAccount);
               balanceCache.put(senderAccount.getAccountNumber(), senderAccount.getBalance());
               balanceCache.put(recieverAccount.getAccountNumber(), recieverAccount.getBalance());


          }
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

          // Applying caching logic
          Cache cacheTransactions = cacheManager.getCache("transactions");
          // checks cache is available
          if (cacheTransactions != null) {
               List<Transactions> txList = cacheTransactions.get(account.getAccountNumber(), List.class);
               // if transactions list is empty then create it
               if (txList == null) {
                    txList = new ArrayList<>();
               }
               // add the transctions to the list
               txList.add(transaction);
               // puts it into cache memory
               cacheTransactions.put(account.getAccountNumber(), txList);

          }
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
          String email = findUserEmail();
          return userRepository.findUserByEmailIgnoreCase(email)
                    .orElseThrow(() -> new UserNotFoundException("user for the given email  " + email + " not found"));
     }

     // Finding email for caching
     public String findUserEmail() {
          return SecurityContextHolder.getContext().getAuthentication().getName();
     }

}
