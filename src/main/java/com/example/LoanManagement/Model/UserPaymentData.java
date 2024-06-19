package com.example.LoanManagement.Model;

import com.example.LoanManagement.Model.Payment;
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
