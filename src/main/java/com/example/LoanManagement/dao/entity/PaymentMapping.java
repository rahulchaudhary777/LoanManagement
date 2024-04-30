package com.example.LoanManagement.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mapping_seq")
    @SequenceGenerator(name = "mapping_seq", sequenceName = "mapping_seq", allocationSize = 1, initialValue = 10001)
    @Column(name = "MAP_ID")
    private int mapId;
    @Column(name = "SENDER")
    private int senderId;
    @Column(name = "RECEIVER")
    private int receiverId;
    @Column(name = "PAYMENT")
    private int paymentId;

}
