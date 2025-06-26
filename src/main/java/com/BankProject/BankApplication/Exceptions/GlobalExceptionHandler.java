package com.BankProject.BankApplication.Exceptions; // Or wherever your GlobalExceptionHandler is located

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest; // Import WebRequest

import com.BankProject.BankApplication.Utils.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.AccountNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

     // Handles validation errors for @Valid DTOs
     @ExceptionHandler(MethodArgumentNotValidException.class)
     public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
          Map<String, String> errors = new HashMap<>();
          ex.getBindingResult().getFieldErrors()
                    .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
          return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
     }

     // Handles AccountNotFoundException (e.g., when an account is not found)
     @ExceptionHandler(AccountNotFoundException.class)
     public ResponseEntity<ErrorResponse> handleAccountNotFoundException(AccountNotFoundException ex,
               WebRequest request) {
          ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.NOT_FOUND,
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", "") // Extracts the request URI
          );
          return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
     }

     // NEW: Handles InsufficientFundsException (or InsufficientAmountException)
     @ExceptionHandler(InsufficientAmountException.class) // Or InsufficientAmountException.class
     public ResponseEntity<ErrorResponse> handleInsufficientFundsException(
               InsufficientAmountException ex,
               WebRequest request) {
          ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.BAD_REQUEST, // Or HttpStatus.FORBIDDEN if you prefer
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", ""));
          return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // Or FORBIDDEN
     }

     // Handles IllegalArgumentException (e.g., negative amounts, same account
     // transfer)
     @ExceptionHandler(IllegalArgumentException.class)
     public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex,
               WebRequest request) {
          ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.BAD_REQUEST,
                    ex.getMessage(),
                    request.getDescription(false).replace("uri=", ""));
          return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
     }

     // General exception handler for any other unhandled exceptions
     // IMPORTANT: In production, you might want to log the full trace but send a
     // generic message to the client.
     @ExceptionHandler(Exception.class)
     public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, WebRequest request) {
          // Log the exception for debugging purposes in real applications
          ex.printStackTrace(); // Log the full stack trace on your server
          ErrorResponse errorResponse = new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "An unexpected error occurred. Please try again later.", // Generic message for client
                    request.getDescription(false).replace("uri=", ""));
          return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
     }
}