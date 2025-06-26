package com.BankProject.BankApplication.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // This makes Spring respond with 404
public class UserNotFoundException extends RuntimeException {
     public UserNotFoundException(String message) {
          super(message);
     }
}