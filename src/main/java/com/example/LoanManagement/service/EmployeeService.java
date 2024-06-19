package com.example.LoanManagement.service;

import com.example.LoanManagement.Model.RegisterForm;
import com.example.LoanManagement.Model.Employee;

public interface EmployeeService {
    boolean identifyUser(RegisterForm registerForm);
    Employee checkEmail(String email);
    boolean saveEmployee(Employee employee);
    String loginUser(String email, String password);
}
