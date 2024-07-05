package org.moldidev.moldispizza.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendCompleteRegistrationEmail(String to, String verificationToken) {
        SimpleMailMessage email = new SimpleMailMessage();

        String link = "http://localhost:8080/api/v1/users/verify?email=" + to + "&token=" + verificationToken;

        email.setTo(to);
        email.setFrom("MS_LANYBX@trial-v69oxl5dj9k4785k.mlsender.net");
        email.setSubject("Complete your registration for Moldi's Pizza");
        email.setText("To confirm your account, please click here: " + link);

        javaMailSender.send(email);
    }
}
