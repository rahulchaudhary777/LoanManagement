package com.example.LoanManagement.service;

import com.example.LoanManagement.Model.Agent;
import com.example.LoanManagement.dao.AgentRepositiory;
import com.example.LoanManagement.Model.Employee;
import com.example.LoanManagement.dao.UserRepositiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgentServiceImpl implements AgentService{
    private final AgentRepositiory agentRepositiory;
    private final UserRepositiory userRepositiory;

    @Autowired
    public AgentServiceImpl(AgentRepositiory agentRepositiory, UserRepositiory userRepositiory) {
        this.agentRepositiory = agentRepositiory;
        this.userRepositiory = userRepositiory;
    }

    @Override
    public boolean saveAgent(Agent agent){
        Employee emp = userRepositiory.getEmployeeByEmail(agent.getEmail());
        Agent checkEmail = agentRepositiory.getAgentByEmail(agent.getEmail());
        if(checkEmail == null && emp == null){
            agentRepositiory.save(agent);
            return true;
        }
        return false;
    }
    @Override
    public Agent checkEmail(String email){
        return agentRepositiory.getAgentByEmail(email);
    }

    @Override
    public List<Agent> getAllAgents(){
        return agentRepositiory.getAllAgents();
    }

    @Override
    public Agent fetchAgent(int agentId){
        return agentRepositiory.getAgentById(agentId);
    }
}
