package com.example.LoanManagement.controller;

import com.example.LoanManagement.service.LoanService;
import com.example.LoanManagement.service.LoanServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    private final LoanService loanService;
    @Autowired
    public UserController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("/index")
    public String home(Model model, HttpSession session){
        Long totalAmount = loanService.getTotalAmount(session);
        String cibil = loanService.fetchEmployee(session).getCibilScore();

        model.addAttribute("title", "Home");
        model.addAttribute("amount", totalAmount);
        model.addAttribute("cibil", cibil);
        return "user/index";
    }
    @PostMapping("/cibil")
    public String cibil(@RequestParam int cibil, HttpSession session){
        loanService.updateCibil(cibil, session);
        return "redirect:/user/apply-loan";
    }
}
