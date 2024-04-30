package com.example.LoanManagement.dao.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employee_seq")
    @SequenceGenerator(name = "employee_seq", sequenceName = "employee_seq", allocationSize = 1, initialValue = 101)
    @Column(name = "EMP_ID")
    private int empId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "PASSWORD")
    @Size(min = 4, max = 12)
    private String password;
    @Column(name = "CIBIL_SCORE")
    private String cibilScore;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empId")
    private List<Loan> allLoans = new ArrayList<>();
}
