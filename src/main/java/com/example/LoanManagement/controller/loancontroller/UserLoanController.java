package com.example.LoanManagement.controller.loancontroller;

import com.example.LoanManagement.Model.UserPaymentData;
import com.example.LoanManagement.Model.Agent;
import com.example.LoanManagement.Model.Loan;
import com.example.LoanManagement.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserLoanController {
    private final LoanService loanService;
    private final AgentService agentService;
    private final PaymentService paymentService;

    @Autowired
    public UserLoanController(LoanService loanService, AgentService agentService, PaymentService paymentService) {
        this.loanService = loanService;
        this.agentService = agentService;
        this.paymentService = paymentService;
    }

    @GetMapping("/apply-loan")
    public String applyLoan(Model model, HttpSession session){
        List<Agent> agents = agentService.getAllAgents();
        String response = loanService.getCibil(session);
        // can put cibil later
        System.out.println(response);
        switch (response) {
            case "notFound" -> {
                model.addAttribute("message", "Provide your cibil score");
                return "user/cibil";
            }
            case "lowCibil" -> model.addAttribute("message", "Sorry, You have a low cibil score");
            case "success" -> model.addAttribute("cibil", loanService.fetchEmployee(session).getCibilScore());
        }
        model.addAttribute("agents", agents);
        return "user/allAgents";
    }
    @GetMapping("/apply")
    public String getLoan(@RequestParam String agentId, Model model){
        Agent agent = agentService.fetchAgent(Integer.parseInt(agentId));
        model.addAttribute("agent", agent);
        return "user/applyLoan";
    }
    @PostMapping("/apply-loan")
    public RedirectView loan(@ModelAttribute Loan loan, @RequestParam("agentId") String agentId,
                             HttpSession session, RedirectAttributes attribute){
        loanService.saveLoan(loan, Integer.parseInt(agentId), session);
        attribute.addFlashAttribute("message", "Loan Applied successfully");
        return new RedirectView("/user/loan-history");
    }
    @GetMapping("/loan-history")
    public String loanHistory(HttpSession session, Model model){
        List<Loan> loans = loanService.loanHistory(session);
        model.addAttribute("loans", loans);
        return "user/loanHistory";
    }


    @GetMapping("/update-loan")
    public String updateLoan(@RequestParam("loanId") String loanId, Model model){
        Loan loan = loanService.getLoanDetail(Integer.parseInt(loanId));
        model.addAttribute("loan", loan);
        return "user/updateLoan";
    }

    @PostMapping("/update-loan")
    public RedirectView update(@ModelAttribute Loan loan, @RequestParam("loanId") String actualLoanId,
                         RedirectAttributes attribute){
        boolean res = loanService.updateLoan(loan, Integer.parseInt(actualLoanId));
        if(res){
            attribute.addFlashAttribute("message", "Updated Successfully");
        }
        else{
            attribute.addFlashAttribute("message", "Can't make changes, Loan already approved");
        }
        return new RedirectView("/user/loan-history");
    }

    @GetMapping("/delete-loan")
    public RedirectView deleteLoan(@RequestParam("loanId") String loanId,
                             HttpSession session, RedirectAttributes attribute){
        boolean res = loanService.deleteById(Integer.parseInt(loanId), session);
        if(res){
            attribute.addFlashAttribute("message", "deleted Successfully");
        }
        else{
            attribute.addFlashAttribute("message", "Can't delete, Loan already approved");
        }
        return new RedirectView("/user/loan-history");
    }

    @GetMapping("/pay-loan")
    public RedirectView payLoan(@RequestParam String loanId, RedirectAttributes attribute){
        loanService.payLoan(loanId);
        attribute.addFlashAttribute("message", "Loan payment successfully");
        return new RedirectView("/user/loan-history");
    }

    @GetMapping("/payments")
    public String payments(HttpSession session, Model model){
        List<UserPaymentData> payments = paymentService.userPayments(session);
        model.addAttribute("payments", payments);
        return "user/payments";
    }
}
