package com.BankProject.BankApplication.DTOs;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiResponse<T> {
     private String message;
     private T data;
}
