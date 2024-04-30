package com.example.LoanManagement.dao.repositiory;

import com.example.LoanManagement.dao.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentRepositiory extends JpaRepository<Payment, Integer> {
    @Query("SELECT u FROM Payment u WHERE u.pId = :pId")
    Payment getPaymentById(@Param("pId") int pId);
}
