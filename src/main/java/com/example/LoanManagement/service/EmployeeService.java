package com.example.LoanManagement.service;

import com.example.LoanManagement.dao.RegisterForm;
import com.example.LoanManagement.dao.entity.Agent;
import com.example.LoanManagement.dao.repositiory.AgentRepositiory;
import com.example.LoanManagement.dao.entity.Employee;
import com.example.LoanManagement.dao.repositiory.UserRepositiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    @Autowired
    private UserRepositiory userRepositiory;
    @Autowired
    private AgentRepositiory agentRepositiory;
    @Autowired
    private AgentService agentService;

    public boolean identifyUser(RegisterForm registerForm){
        if(registerForm.getRole().equals("USER")){
            Employee emp = new Employee();
            emp.setName(registerForm.getName()); emp.setEmail(registerForm.getEmail());
            emp.setPassword(registerForm.getPassword());

            if(!registerForm.getCibilScore().isEmpty()) emp.setCibilScore(registerForm.getCibilScore());
            return saveEmployee(emp);
        }
        else{
            Agent agent = new Agent();
            agent.setName(registerForm.getName()); agent.setEmail(registerForm.getEmail());
            agent.setPassword(registerForm.getPassword()); agent.setDepartment(registerForm.getDepartment());
            return agentService.saveAgent(agent);
        }

    }

    public String loginUser(String email, String password){
        Employee emp = checkEmail(email);
        Agent agent = agentService.checkEmail(email);
        if(emp == null && agent == null){
            return "invalidMail";
        }
        else if(emp != null) {
            if (!password.equals(emp.getPassword())) {
                return "invalidPassword";
            } else {
                return "Success";
            }
        }
        else {
            if(!password.equals(agent.getPassword())){
                return "invalidPassword";
            }
            else{
                return "successAdmin";
            }
        }
    }

    public boolean saveEmployee(Employee employee){
        Employee emp = userRepositiory.getEmployeeByEmail(employee.getEmail());
        Agent agent = agentRepositiory.getAgentByEmail(employee.getEmail());
        if(emp == null && agent == null){
            userRepositiory.save(employee);
            return true;
        }
        return false;
    }

    public Employee checkEmail(String email){
        return userRepositiory.getEmployeeByEmail(email);
    }

}
