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

        email.setTo(to);
        email.setFrom("MS_LANYBX@trial-v69oxl5dj9k4785k.mlsender.net");
        email.setSubject("Complete your registration for Moldi's Pizza");
        email.setText("If this was not you, please ignore this email. Otherwise, use this code to verify your account: " + verificationToken);

        javaMailSender.send(email);
    }

    @Async
    public void sendResetPasswordEmail(String to, String resetPasswordToken) {
        SimpleMailMessage email = new SimpleMailMessage();

        email.setTo(to);
        email.setFrom("MS_LANYBX@trial-v69oxl5dj9k4785k.mlsender.net");
        email.setSubject("Reset your password for Moldi's Pizza");
        email.setText("If this was not you, please ignore this email. Otherwise, use this code to reset your account's password: " + resetPasswordToken);

        javaMailSender.send(email);
    }
}
