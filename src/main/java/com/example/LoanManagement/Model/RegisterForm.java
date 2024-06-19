package com.example.LoanManagement.Model;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterForm {
//    @NotEmpty
    private String name;
//    @Email
//    @NotEmpty
    private String email;
//    @Size(min = 4, max = 12)
    private String password;
    private String cibilScore;
    private String role;
    private String department;
}
