package com.example.LoanManagement.controller;

import com.example.LoanManagement.dao.RegisterForm;
import com.example.LoanManagement.dao.entity.Agent;
import com.example.LoanManagement.dao.entity.Employee;
import com.example.LoanManagement.service.AgentService;
import com.example.LoanManagement.service.EmployeeService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class AuthenticationController {
    @Autowired
    private final EmployeeService employeeService;
    @Autowired
    private final AgentService agentService;

    public AuthenticationController(EmployeeService employeeService, AgentService agentService) {
        this.employeeService = employeeService;
        this.agentService = agentService;
    }

    @GetMapping("/register")
    public String registerUser(Model model){
        model.addAttribute("title", "Register User");
        return "register";
    }

    @PostMapping("/register")
    public RedirectView signup(@Valid @ModelAttribute RegisterForm registerForm,
                               BindingResult result, RedirectAttributes attribute, Model model){
        if(result.hasErrors()){
            System.out.println(result);
            return new RedirectView("/register");
        }
//        return "register";
        boolean save = employeeService.identifyUser(registerForm);
        if(save){
            attribute.addFlashAttribute("message", "successfully registered");
            return new RedirectView( "/login");
        }
        else{
            attribute.addFlashAttribute("message", "Email already exist");
            return new RedirectView("/register");
        }
    }
    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("title", "Login User");
        return "login";
    }

    @PostMapping("/login")
    public String signin(@RequestParam String email, @RequestParam String password,
                         Model model, HttpSession session){
        String response = employeeService.loginUser(email, password);
        Employee emp = employeeService.checkEmail(email);
        Agent agent = agentService.checkEmail(email);

        if(response.equals("invalidMail")){
            model.addAttribute("message", "Invalid email");
            return "login";
        }
        else if(response.equals("invalidPassword")){
            model.addAttribute("message", "Invalid password");
            return "login";
        }
        else if(response.equals("Success")){
            model.addAttribute("message", "Logged in successfully");
            session.setAttribute("userId", emp.getEmpId());
            session.setAttribute("username", emp.getName());
            session.setAttribute("role", "USER");
            return "redirect:/user/index";
        }
        else if(response.equals("successAdmin")){
            model.addAttribute("message", "Logged in successfully");
            session.setAttribute("userId", agent.getAgentId());
            session.setAttribute("username", agent.getName());
            session.setAttribute("role", "ADMIN");
            return "redirect:/admin/index";
        }
        else {
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, Model model){
        session.removeAttribute("username");
        session.removeAttribute("userId");
        session.removeAttribute("role");
        model.addAttribute("message", "Logged out successfully");
        model.addAttribute("title", "Login");

        return "redirect:/login";
    }
}
