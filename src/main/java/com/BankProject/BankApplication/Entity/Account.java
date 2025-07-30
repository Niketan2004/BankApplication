package com.BankProject.BankApplication.Entity;

import com.BankProject.BankApplication.Enum.AccountType;
import jakarta.persistence.*; // Import all from persistence
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account implements Serializable {
     @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_number_sequence")
     @SequenceGenerator(name = "account_number_sequence", // Name of the generator in JPA
               sequenceName = "account_number_seq", // Name of the actual DB sequence to create
               initialValue = 1462000000, // Your desired starting account number
               allocationSize = 1 // How much to increment the sequence by
     )
     private Long accountNumber;
     private double balance;
     @Enumerated(EnumType.STRING)
     private AccountType accountType;
     // One-to-one relationship with User (inverse side)
     // mappedBy refers to the 'account' field in the User entity
     @OneToOne(mappedBy = "account", fetch = FetchType.LAZY, optional = false)
     private User user;
     // One-to-many relationship with Transactions
     // 'account' is the field name in the Transactions entity that maps back to this
     // Account
     @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
     private List<Transactions> transactions = new ArrayList<>(); // Initialize to prevent NullPointer
}