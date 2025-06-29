package com.BankProject.BankApplication.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferSlip {
     @NotNull(message = "Senders account is must..it should not be empty!")
     private Long senderAccountNumber;
     @NotNull(message = "Recievers account number is must...it should not be empty")
     private Long recieverAccountNumber;
     @NotNull
     @DecimalMin(value = "1.0", message = "Amount should be greater than 1.0 ")
     private Double amount;

}
