package com.example.LoanManagement.service;

import com.example.LoanManagement.Model.RegisterForm;
import com.example.LoanManagement.Model.Agent;
import com.example.LoanManagement.dao.AgentRepositiory;
import com.example.LoanManagement.Model.Employee;
import com.example.LoanManagement.dao.UserRepositiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService{
    private final UserRepositiory userRepositiory;
    private final AgentRepositiory agentRepositiory;
    private final AgentServiceImpl agentServiceImpl;

    @Autowired
    public EmployeeServiceImpl(UserRepositiory userRepositiory, AgentRepositiory agentRepositiory, AgentServiceImpl agentServiceImpl) {
        this.userRepositiory = userRepositiory;
        this.agentRepositiory = agentRepositiory;
        this.agentServiceImpl = agentServiceImpl;
    }

    @Override
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
            return agentServiceImpl.saveAgent(agent);
        }

    }

    @Override
    public String loginUser(String email, String password){
        Employee emp = checkEmail(email);
        Agent agent = agentServiceImpl.checkEmail(email);
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

    @Override
    public boolean saveEmployee(Employee employee){
        Employee emp = userRepositiory.getEmployeeByEmail(employee.getEmail());
        Agent agent = agentRepositiory.getAgentByEmail(employee.getEmail());
        if(emp == null && agent == null){
            userRepositiory.save(employee);
            return true;
        }
        return false;
    }

    @Override
    public Employee checkEmail(String email){
        return userRepositiory.getEmployeeByEmail(email);
    }

}
