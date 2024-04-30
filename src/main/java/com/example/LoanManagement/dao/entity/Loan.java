package com.example.LoanManagement.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_seq")
    @SequenceGenerator(name = "loan_seq", sequenceName = "loan_seq", allocationSize = 1, initialValue = 5001)
    @Column(name = "LOAN_ID")
    private int loanId;
    @Column(name = "LOAN_TYPE")
    private String type;
    @Column(name = "AMOUNT")
    private String amount;
    @Column(name = "APPROVE_DATE")
    private Date approveDate;
    @Column(name = "DURATION")
    private int duration;
    @Column(name = "STATUS")
    private String status;
    @ManyToOne
    @JoinColumn(name = "EMP_ID")
    private Employee empId;
    @ManyToOne
    @JoinColumn(name = "Agent_ID")
    private Agent agentId;



    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", type='" + type + '\'' +
                ", amount='" + amount + '\'' +
                ", approveDate=" + approveDate +
                ", duration=" + duration +
                ", status='" + status + '\'' +
                '}';
    }
}
