package com.example.LoanManagement.dao.repositiory;

import com.example.LoanManagement.dao.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepositiory extends JpaRepository<Employee, Integer> {
    @Query("SELECT u FROM Employee u WHERE u.email =:email")
    Employee getEmployeeByEmail(@Param("email") String email);
    @Query("SELECT u FROM Employee u WHERE u.empId =:empId")
    Employee getEmployeeById(@Param("empId") int empId);
//    @Query("SELECT u FROM Employee u WHERE u.role =:role order by u.adminRole")
//    List<Employee> getAdmins(@Param("role") String role);
}
