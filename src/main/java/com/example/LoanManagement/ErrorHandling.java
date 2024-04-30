package com.example.LoanManagement;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandling {
    @ExceptionHandler(value = Exception.class)
    public String handler(){
        return "error";
    }
}
