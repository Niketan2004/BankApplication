package com.BankProject.BankApplication.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND) // This makes Spring respond with 404
public class UserAlreadyExistsException extends RuntimeException {
     public UserAlreadyExistsException(String message) {
          super(message);
     }
}
