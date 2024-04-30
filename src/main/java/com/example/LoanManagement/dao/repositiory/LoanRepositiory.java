package com.example.LoanManagement.dao.repositiory;

import com.example.LoanManagement.dao.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoanRepositiory extends JpaRepository<Loan, Integer> {
    @Query("SELECT u FROM Loan u WHERE u.loanId = :loanId")
    Loan getLoanById(@Param("loanId") int loanId);
}