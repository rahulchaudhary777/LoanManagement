package com.example.LoanManagement.service;

import com.example.LoanManagement.Model.UserPaymentData;
import com.example.LoanManagement.Model.Agent;
import com.example.LoanManagement.Model.Employee;
import com.example.LoanManagement.Model.Payment;
import com.example.LoanManagement.Model.PaymentMapping;
import com.example.LoanManagement.dao.AgentRepositiory;
import com.example.LoanManagement.dao.MappingRepository;
import com.example.LoanManagement.dao.PaymentRepositiory;
import com.example.LoanManagement.dao.UserRepositiory;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService{

    private final MappingRepository mappingRepository;
    private final LoanServiceImpl loanServiceImpl;
    private final PaymentRepositiory paymentRepositiory;
    private final AgentRepositiory agentRepositiory;
    private final UserRepositiory userRepositiory;
    @Autowired
    public PaymentServiceImpl(MappingRepository mappingRepository, LoanServiceImpl loanServiceImpl, PaymentRepositiory paymentRepositiory, AgentRepositiory agentRepositiory, UserRepositiory userRepositiory) {
        this.mappingRepository = mappingRepository;
        this.loanServiceImpl = loanServiceImpl;
        this.paymentRepositiory = paymentRepositiory;
        this.agentRepositiory = agentRepositiory;
        this.userRepositiory = userRepositiory;
    }

    @Override
    public List<UserPaymentData> userPayments(HttpSession session){

        Employee emp = loanServiceImpl.fetchEmployee(session);
        List<PaymentMapping> senderMappings = mappingRepository.getSenderPayments(emp.getEmpId());
        List<PaymentMapping> receiverMappings = mappingRepository.getReceiverPayments(emp.getEmpId());
        List<UserPaymentData> allPayments = new ArrayList<>();

        for(PaymentMapping i : receiverMappings){
            Payment payment = paymentRepositiory.getPaymentById(i.getPaymentId());
            System.out.println(payment);
            if(payment.getStatus().equals("Approved")){
                payment.setStatus("Credited");
            }
            String email = agentRepositiory.getAgentById(i.getSenderId()).getEmail();
            UserPaymentData data = new UserPaymentData(payment, email);
            allPayments.add(data);
        }

        for(PaymentMapping i : senderMappings){
            Payment payment = paymentRepositiory.getPaymentById(i.getPaymentId());
            if(payment.getStatus().equals("Paid")){
                payment.setStatus("Debited");
            }
            String email = agentRepositiory.getAgentById(i.getReceiverId()).getEmail();
            UserPaymentData data = new UserPaymentData(payment, email);
            allPayments.add(data);
        }
        // Sort the payments list in descending order based on the date
        Collections.sort(allPayments, (p1, p2) -> p2.getPayment().getDate().compareTo(p1.getPayment().getDate()));
        return allPayments;
    }

    @Override
    public List<UserPaymentData> agentPayments(HttpSession session){
        Agent agent = loanServiceImpl.fetchAgent(session);
        List<PaymentMapping> senderMappings = mappingRepository.getSenderPayments(agent.getAgentId());
        List<PaymentMapping> receiverMappings = mappingRepository.getReceiverPayments(agent.getAgentId());
        List<UserPaymentData> allPayments = new ArrayList<>();

        for(PaymentMapping i : receiverMappings){
            Payment payment = paymentRepositiory.getPaymentById(i.getPaymentId());

            if(payment.getStatus().equals("Paid")){
                payment.setStatus("Credited");
            }
            String email = userRepositiory.getEmployeeById(i.getSenderId()).getEmail();
            UserPaymentData data = new UserPaymentData(payment, email);
            allPayments.add(data);
        }

        for(PaymentMapping i : senderMappings){
            Payment payment = paymentRepositiory.getPaymentById(i.getPaymentId());
            if(payment.getStatus().equals("Approved")){
                payment.setStatus("Debited");
            }
            String email = userRepositiory.getEmployeeById(i.getReceiverId()).getEmail();
            UserPaymentData data = new UserPaymentData(payment, email);
            allPayments.add(data);
        }
        // Sort the payments list in descending order based on the date
        Collections.sort(allPayments, (p1, p2) -> p2.getPayment().getDate().compareTo(p1.getPayment().getDate()));
        return allPayments;
    }
}
