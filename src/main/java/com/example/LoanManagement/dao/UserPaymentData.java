package com.example.LoanManagement.dao;

import com.example.LoanManagement.dao.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPaymentData {
    private Payment payment;
    private String email;
}
