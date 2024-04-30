package com.example.LoanManagement.dao.repositiory;

import com.example.LoanManagement.dao.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AgentRepositiory extends JpaRepository<Agent, Integer> {
    @Query("SELECT u FROM Agent u WHERE u.email =:email")
    Agent getAgentByEmail(@Param("email") String email);
    @Query("SELECT u FROM Agent u WHERE u.agentId =:agentId")
    Agent getAgentById(@Param("agentId") int agentId);
    @Query("SELECT u FROM Agent u")
    List<Agent> getAllAgents();
}
