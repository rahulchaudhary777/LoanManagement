package com.example.LoanManagement.service;

import com.example.LoanManagement.dao.entity.Agent;
import com.example.LoanManagement.dao.repositiory.AgentRepositiory;
import com.example.LoanManagement.dao.entity.Employee;
import com.example.LoanManagement.dao.repositiory.UserRepositiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentService {
    @Autowired
    private AgentRepositiory agentRepositiory;
    @Autowired
    private UserRepositiory userRepositiory;
    public boolean saveAgent(Agent agent){
        Employee emp = userRepositiory.getEmployeeByEmail(agent.getEmail());
        Agent checkEmail = agentRepositiory.getAgentByEmail(agent.getEmail());
        if(checkEmail == null && emp == null){
            agentRepositiory.save(agent);
            return true;
        }
        return false;
    }
    public Agent checkEmail(String email){
        return agentRepositiory.getAgentByEmail(email);
    }

    public List<Agent> getAllAgents(){
        return agentRepositiory.getAllAgents();
    }

    public Agent fetchAgent(int agentId){
        return agentRepositiory.getAgentById(agentId);
    }
}
