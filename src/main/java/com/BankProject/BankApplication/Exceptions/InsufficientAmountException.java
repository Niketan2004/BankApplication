package com.BankProject.BankApplication.Exceptions;

public class InsufficientAmountException extends RuntimeException {
     public InsufficientAmountException(String message) {
          super(message);
     }
}
