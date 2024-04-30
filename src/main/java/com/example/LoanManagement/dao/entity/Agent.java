package com.example.LoanManagement.dao.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agent {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agent_seq")
    @SequenceGenerator(name = "agent_seq", sequenceName = "agent_seq", allocationSize = 1, initialValue = 501)
    @Column(name = "Agent_ID")
    private int agentId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "DEPARTMENT")
    private String department;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "agentId")
    private List<Loan> allLoans = new ArrayList<>();
}
