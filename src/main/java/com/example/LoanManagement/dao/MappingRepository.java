package com.example.LoanManagement.dao;

import com.example.LoanManagement.Model.PaymentMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MappingRepository extends JpaRepository<PaymentMapping, Integer> {
    @Query("SELECT u FROM PaymentMapping u WHERE u.senderId = :senderId")
    List<PaymentMapping> getSenderPayments(@Param("senderId") int senderId);

    @Query("SELECT u FROM PaymentMapping u WHERE u.receiverId = :receiverId")
    List<PaymentMapping> getReceiverPayments(@Param("receiverId") int receiverId);
}
