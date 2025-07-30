package com.BankProject.BankApplication.DTOs; // Or a dedicated 'error' package

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse implements Serializable{

     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'")
     private LocalDateTime timestamp;
     private int status;
     private String error;
     private String message;
     private String path;

     public ErrorResponse(HttpStatus status, String message, String path) {
          this.timestamp = LocalDateTime.now();
          this.status = status.value();
          this.error = status.getReasonPhrase();
          this.message = message;
          this.path = path;
     }
}