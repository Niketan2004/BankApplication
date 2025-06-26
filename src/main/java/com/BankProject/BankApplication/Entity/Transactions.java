package com.BankProject.BankApplication.Entity;

import java.time.LocalDateTime;
import com.BankProject.BankApplication.Enum.TransactionTypes;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transactions {

     @Id
     @GeneratedValue(strategy = GenerationType.UUID)
     private String transactionId;
     @NotNull
     Double amount;
     @Enumerated(EnumType.STRING)
     private TransactionTypes type;
     private LocalDateTime time;
     // Many-to-one relationship with Account (owning side)
     @ManyToOne(fetch = FetchType.LAZY) // Lazy fetch for ManyToOne is usually good
     @JoinColumn(name = "account_id", nullable = false)
     private Account account; // Field name is 'account'
}