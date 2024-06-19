package com.example.LoanManagement.service;

import com.example.LoanManagement.Model.*;
import com.example.LoanManagement.dao.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService{
    private final LoanRepositiory loanRepositiory;
    private final UserRepositiory userRepositiory;
    private final AgentRepositiory agentRepositiory;
    private final PaymentRepositiory paymentRepositiory;
    private final MappingRepository mappingRepo;
    private final MailService mailService;
    @Autowired
    public LoanServiceImpl(LoanRepositiory loanRepositiory, UserRepositiory userRepositiory, AgentRepositiory agentRepositiory, PaymentRepositiory paymentRepositiory, MappingRepository mappingRepo, MailService mailService) {
        this.loanRepositiory = loanRepositiory;
        this.userRepositiory = userRepositiory;
        this.agentRepositiory = agentRepositiory;
        this.paymentRepositiory = paymentRepositiory;
        this.mappingRepo = mappingRepo;
        this.mailService = mailService;
    }

    @Override
    public Employee fetchEmployee(HttpSession session){
        int empId = (Integer)session.getAttribute("userId");
        return userRepositiory.getEmployeeById(empId);
    }
    @Override
    public Agent fetchAgentById(int empId){
        return agentRepositiory.getAgentById(empId);
    }

    @Override
    public Agent fetchAgent(HttpSession session){
        int agentId = (Integer)session.getAttribute("userId");
        return agentRepositiory.getAgentById(agentId);
    }

    @Override
    public String getCibil(HttpSession session){
        System.out.println("Hello");
        String cibil = fetchEmployee(session).getCibilScore();
        System.out.println(cibil);
        if(cibil == null){
            return "notFound";
        } else if (Double.parseDouble(cibil) < 500) {
            return "lowCibil";
        } else{
            return "success";
        }
    }
    @Override
    public void updateCibil(int cibil, HttpSession session){
        Employee emp = fetchEmployee(session);
        emp.setCibilScore(cibil+"");
        userRepositiory.save(emp);
    }
    @Override
    public List<Loan> getPendingLoans(Agent agent){
        List<Loan> pendingLoans = new ArrayList<>();
        for(Loan i : agent.getAllLoans()){
            if((i.getStatus()).equals("Pending")){
                pendingLoans.add(i);
            }
        }
        return pendingLoans;
    }

    @Override
    public Date getCurrDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return new Date();
    }
    @Override
    public void saveLoan(Loan loan, int agentId, HttpSession session){
        Employee emp = fetchEmployee(session);
        Agent agent = fetchAgentById(agentId);

        loan.setStatus("Pending");
        // save reference key of emp table in loan table
        loan.setEmpId(emp);
        // set agent reference in loan table
        loan.setAgentId(agent);

        Loan temp = loanRepositiory.save(loan);
//        save loan table reference key in emp table
        emp.getAllLoans().add(temp);
        agent.getAllLoans().add(temp);
    }

    @Override
    public List<Loan> loanHistory(HttpSession session){
        // fetch all loans of user
        return fetchEmployee(session).getAllLoans();
    }

    @Override
    public Loan getLoanDetail(int loadId){
        System.out.println(loadId);
        return loanRepositiory.getLoanById(loadId);
    }

    @Override
    public boolean updateLoan(Loan loan, int actualLoanId){
        Loan actualLoan = getLoanDetail(actualLoanId);
        if((actualLoan.getStatus()).equals("Pending")){
            actualLoan.setAmount(loan.getAmount());
            actualLoan.setDuration(loan.getDuration());
            loanRepositiory.save(actualLoan);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean deleteById(int loanId, HttpSession session){
        String status = getLoanDetail(loanId).getStatus();
        Agent agent = getLoanDetail(loanId).getAgentId();
        if(status.equals("Pending")){
            Employee emp = fetchEmployee(session);
            Loan loan = getLoanDetail(loanId);

            // remove loan reference from Employee table
            emp.getAllLoans().remove(loan);
            agent.getAllLoans().remove(loan);
            System.out.println("Hello Agent -->"+agent);
            // remove specific loan from loanId
            loanRepositiory.deleteById(loanId);
            return true;
        }
        else {
            return false;
        }
    }
    @Override
    public void approveLoan(int loanId) {
        Loan loan = getLoanDetail(loanId);
        // add approve date
        loan.setApproveDate(getCurrDate());

        loan.setStatus("Approved");
        loanRepositiory.save(loan);

        // save approved payment detail
        Payment payment = new Payment();
        payment.setAmount(loan.getAmount());
        payment.setStatus("Approved");
        payment.setDate(getCurrDate());
        payment.setPaymentType(loan.getType()+" Loan");
        Payment pay = paymentRepositiory.save(payment);

        savePaymentMapping(pay.getPId(), loan.getAgentId().getAgentId(), loan.getEmpId().getEmpId());
    }
    @Override
    public void rejectLoan(int loanId){
        Loan loan = getLoanDetail(loanId);
        loan.setApproveDate(getCurrDate());
        loan.setStatus("Reject");
        loanRepositiory.save(loan);
    }

    @Override
    public List<Loan> getNonPendingLoans(Agent agent){
        List<Loan> nonPendingLoans = new ArrayList<>();
        for(Loan i : agent.getAllLoans()){
            if(!(i.getStatus()).equals("Pending")){
                if(i.getStatus().equals("Paid")) {
                    i.setStatus("Paid by user");
                }
                nonPendingLoans.add(i);
            }
        }
        return nonPendingLoans;
    }

    @Override
    public Date addMonthsToDate(Date date, int months) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, months);
        return calendar.getTime();
    }
    // Calculate difference in months between two dates
    @Override
    public DateDifference calculateDateDifference(Date currDate, Date lastdate) {
        LocalDate currentDate = LocalDate.ofInstant(currDate.toInstant(), ZoneId.systemDefault());
        LocalDate dueDate = LocalDate.ofInstant(lastdate.toInstant(), ZoneId.systemDefault());

        Period period;
        if (currentDate.isAfter(dueDate)) {
            period = Period.between(dueDate, currentDate);
        } else {
            period = Period.between(currentDate, dueDate);
        }
        return new DateDifference(period.getMonths(), (period.getMonths())*30+period.getDays());
    }

    @Override
    public String sendReminder(String loanId){
        Loan loan = getLoanDetail(Integer.parseInt(loanId));
        if(loan.getStatus().equals("Paid")){
            return "alreadyPaid";
        }
        else if(!loan.getStatus().equals("Approved")){
            return "notApproved";
        }
        Date updatedDate = addMonthsToDate(loan.getApproveDate(), loan.getDuration());
        DateDifference dateDifference = calculateDateDifference(new Date(), updatedDate);

        MailStructure mail = new MailStructure();
        mail.setSubject("Reminder To Pay Loan");
        mail.setMessage("Pay Your "+loan.getType()+" Loan \n Due date is :- "+updatedDate);

        if(dateDifference.getMonths() < 1){
            mailService.sendmail(loan.getEmpId().getEmail(), mail);
            System.out.println("Mail Sent");
            return "sent";
        }
        return "notSent";
    }

    @Override
    public Long getTotalAmount(HttpSession session){
        List<Loan> loans = fetchEmployee(session).getAllLoans();
        long totalAmount = 0L;
        for(Loan i : loans){
            if(i.getStatus().equals("Approved")){
                totalAmount += Long.parseLong(i.getAmount());
            }
        }
        return totalAmount;
    }

    @Override
    public void payLoan(String loanId){
        Loan loan = getLoanDetail(Integer.parseInt(loanId));
        savePayment(loan);
    }

    @Override
    public void savePayment(Loan loan){
        Payment payment = new Payment();
        payment.setAmount(loan.getAmount());
        payment.setDate(getCurrDate());
        payment.setStatus("Paid");
        System.out.println(loan.getType());
        payment.setPaymentType(loan.getType()+" Loan");

        Payment payment1 = paymentRepositiory.save(payment);
        System.out.println(payment);

        savePaymentMapping(payment1.getPId(), loan.getEmpId().getEmpId(), loan.getAgentId().getAgentId());

        loan.setStatus("Paid");
        loanRepositiory.save(loan);

        // update cibil score according to due date
        Date loanDuration = addMonthsToDate(loan.getApproveDate(), loan.getDuration());
        int exceedDays = calculateDateDifference(new Date(), loanDuration).getDayExceeded();

        Employee emp = loan.getEmpId();
        double x;
        if(exceedDays > 0){
            x = Double.parseDouble(emp.getCibilScore()) + exceedDays*0.1;
        } else {
            x = Double.parseDouble(emp.getCibilScore()) - exceedDays*0.1;
        }
        emp.setCibilScore(x+"");
        userRepositiory.save(emp);
    }

    @Override
    public void savePaymentMapping(int pId, int senderId, int receiverId){
        PaymentMapping mapping = new PaymentMapping();
        mapping.setReceiverId(receiverId);
        mapping.setSenderId(senderId);
        mapping.setPaymentId(pId);
        mappingRepo.save(mapping);
    }
}
