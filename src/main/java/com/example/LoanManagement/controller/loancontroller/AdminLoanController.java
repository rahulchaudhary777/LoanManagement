package com.example.LoanManagement.controller.loancontroller;

import com.example.LoanManagement.dao.UserPaymentData;
import com.example.LoanManagement.dao.entity.Agent;
import com.example.LoanManagement.dao.entity.Loan;
import com.example.LoanManagement.service.LoanService;
import com.example.LoanManagement.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminLoanController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/approve-loan")
    public String approveLoan(HttpSession session, Model model){
        Agent agent = loanService.fetchAgent(session);
        List<Loan> pendingLoans = loanService.getPendingLoans(agent);
        model.addAttribute("loans", pendingLoans);
        return "admin/approveLoan";
    }
    @GetMapping("/approve")
    public RedirectView approveLoan(@RequestParam String loanId, RedirectAttributes attribute) throws ParseException {
        loanService.approveLoan(Integer.parseInt(loanId));
        attribute.addFlashAttribute("message", "Loan has been Approved");
        return new RedirectView("/admin/approve-loan");
    }
    @GetMapping("/delete-loan")
    public RedirectView rejectLoan(@RequestParam String loanId, RedirectAttributes attribute){
        loanService.rejectLoan(Integer.parseInt(loanId));
        attribute.addFlashAttribute("message", "Loan has been rejected");
        return new RedirectView("/admin/approve-loan");
    }

    @GetMapping("loan-history")
    public String loanHistory(HttpSession session, Model model){
        Agent agent = loanService.fetchAgent(session);
        List<Loan> nonPendingLoans = loanService.getNonPendingLoans(agent);
        model.addAttribute("loans", nonPendingLoans);
        return "admin/loanHistory";
    }
    @GetMapping("/reminder")
    public RedirectView reminder(@RequestParam String loanId, RedirectAttributes attribute){
        String response = loanService.sendReminder(loanId);
        if(response.equals("sent")){
            attribute.addFlashAttribute("message", "Reminder has been sent");
        }
        else if(response.equals("alreadyPaid")){
            attribute.addFlashAttribute("message", "Loan already had been Paid");
        }
        else if(response.equals("notSent")){
            attribute.addFlashAttribute("message", "The due date extends beyond one month");
        }
        else{
            attribute.addFlashAttribute("message", "The loan has not been approved");
        }
        return new RedirectView("/admin/loan-history");
    }
    @GetMapping("/payments")
    public String payments(HttpSession session, Model model){
        List<UserPaymentData> payments = paymentService.agentPayments(session);
        model.addAttribute("payments", payments);
        return "user/payments";
    }
}
