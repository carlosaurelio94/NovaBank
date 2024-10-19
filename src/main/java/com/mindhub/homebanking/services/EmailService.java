package com.mindhub.homebanking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
/*
@Service
public class EmailService {


    @Autowired
    JavaMailSender emailSender;
//
//    public void EmailSender() {}

    public void EmailSender(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void send(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("carlosaureliorodriguez23@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        this.emailSender.send(message);

        System.out.println("Mail sent succesfully...");
    }

}
*/