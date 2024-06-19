package com.example.LoanManagement.service;

import com.example.LoanManagement.Model.DateDifference;
import com.example.LoanManagement.Model.Agent;
import com.example.LoanManagement.Model.Employee;
import com.example.LoanManagement.Model.Loan;
import jakarta.servlet.http.HttpSession;

import java.util.Date;
import java.util.List;

public interface LoanService {
    Employee fetchEmployee(HttpSession session);
    String getCibil(HttpSession session);
    Agent fetchAgent(HttpSession session);
    Agent fetchAgentById(int empId);
    Date getCurrDate();
    void saveLoan(Loan loan, int agentId, HttpSession session);
    List<Loan> getPendingLoans(Agent agent);
    void updateCibil(int cibil, HttpSession session);

    List<Loan> loanHistory(HttpSession session);
    Loan getLoanDetail(int loadId);
    boolean updateLoan(Loan loan, int actualLoanId);
    boolean deleteById(int loanId, HttpSession session);

    void approveLoan(int loanId);
    void rejectLoan(int loanId);
    List<Loan> getNonPendingLoans(Agent agent);
    Date addMonthsToDate(Date date, int months);

    DateDifference calculateDateDifference(Date currDate, Date lastdate);
    String sendReminder(String loanId);
    Long getTotalAmount(HttpSession session);
    void payLoan(String loanId);
    void savePayment(Loan loan);
    void savePaymentMapping(int pId, int senderId, int receiverId);
}
