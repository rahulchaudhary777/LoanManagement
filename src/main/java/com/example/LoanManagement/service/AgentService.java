package com.example.LoanManagement.service;

import com.example.LoanManagement.Model.Agent;

import java.util.List;

public interface AgentService {
    boolean saveAgent(Agent agent);
    Agent checkEmail(String email);
    List<Agent> getAllAgents();
    Agent fetchAgent(int agentId);
}
