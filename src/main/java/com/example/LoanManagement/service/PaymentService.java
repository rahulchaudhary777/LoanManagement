package com.example.LoanManagement.service;

import com.example.LoanManagement.dao.UserPaymentData;
import com.example.LoanManagement.dao.entity.Agent;
import com.example.LoanManagement.dao.entity.Employee;
import com.example.LoanManagement.dao.entity.Payment;
import com.example.LoanManagement.dao.entity.PaymentMapping;
import com.example.LoanManagement.dao.repositiory.AgentRepositiory;
import com.example.LoanManagement.dao.repositiory.MappingRepository;
import com.example.LoanManagement.dao.repositiory.PaymentRepositiory;
import com.example.LoanManagement.dao.repositiory.UserRepositiory;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    @Autowired
    private MappingRepository mappingRepository;
    @Autowired
    private LoanService loanService;
    @Autowired
    private PaymentRepositiory paymentRepositiory;
    @Autowired
    private AgentRepositiory agentRepositiory;
    @Autowired
    private UserRepositiory userRepositiory;

    public List<UserPaymentData> userPayments(HttpSession session){

        Employee emp = loanService.fetchEmployee(session);
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

    public List<UserPaymentData> agentPayments(HttpSession session){
        Agent agent = loanService.fetchAgent(session);
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
