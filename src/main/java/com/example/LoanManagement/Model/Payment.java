package com.example.LoanManagement.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
    @SequenceGenerator(name = "payment_seq", sequenceName = "payment_seq", allocationSize = 1, initialValue = 1001)
    @Column(name = "PID")
    private int pId;
    @Column(name = "AMOUNT")
    private String amount;
    @Column(name = "DATE")
    private Date date;
    @Column(name = "PAYMENT_TYPE")
    private String paymentType;
    @Column(name = "STATUS")
    private String status;
}
