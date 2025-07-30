package com.BankProject.BankApplication.DTOs;

import java.io.Serializable;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ApiResponse<T> implements Serializable {
     private String message;
     private T data;
}
