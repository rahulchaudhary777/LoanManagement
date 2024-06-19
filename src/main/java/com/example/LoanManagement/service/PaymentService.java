package com.example.LoanManagement.service;

import com.example.LoanManagement.Model.UserPaymentData;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface PaymentService {
    List<UserPaymentData> userPayments(HttpSession session);
    List<UserPaymentData> agentPayments(HttpSession session);
}
