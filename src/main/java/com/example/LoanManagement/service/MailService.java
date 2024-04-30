package com.example.LoanManagement.service;

import com.example.LoanManagement.dao.MailStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("spring.mail.username")
    private String fromMail;
    public void sendmail(String mail, MailStructure mailStructure){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(mailStructure.getSubject());
        simpleMailMessage.setText(mailStructure.getMessage());
//        double random = Math.random();
//        simpleMailMessage.setText("System genrated otp is :- "+ random()+"\n Valid for 5 min");
        simpleMailMessage.setTo(mail);
        javaMailSender.send(simpleMailMessage);
    }
}
