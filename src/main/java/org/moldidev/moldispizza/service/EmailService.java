package org.moldidev.moldispizza.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.moldidev.moldispizza.dto.OrderDTO;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Async
    public void sendCompleteRegistrationEmail(String to, String verificationToken) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("verificationToken", verificationToken);
            String htmlContent = templateEngine.process("verify-account-email", context);

            helper.setTo(to);
            helper.setFrom("MS_LANYBX@trial-v69oxl5dj9k4785k.mlsender.net");
            helper.setSubject("Complete your registration for Moldi's Pizza");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        }

        catch (MessagingException e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }

    @Async
    public void sendResetPasswordEmail(String to, String resetPasswordToken) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("resetPasswordToken", resetPasswordToken);
            String htmlContent = templateEngine.process("reset-password-email", context);

            helper.setTo(to);
            helper.setFrom("MS_LANYBX@trial-v69oxl5dj9k4785k.mlsender.net");
            helper.setSubject("Reset your password for Moldi's Pizza");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        }

        catch (MessagingException e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }

    @Async
    public void sendOrderPaidEmail(String to, OrderDTO order) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();

            context.setVariable("firstName", order.user().firstName());
            context.setVariable("lastName", order.user().lastName());
            context.setVariable("pizzas", order.pizzas());
            context.setVariable("totalPrice", order.totalPrice());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            String formattedDate = order.createdDate().format(formatter);
            context.setVariable("createdDate", formattedDate);

            String htmlContent = templateEngine.process("order-paid", context);

            helper.setTo(to);
            helper.setFrom("MS_LANYBX@trial-v69oxl5dj9k4785k.mlsender.net");
            helper.setSubject("Thank you for your order");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        }

        catch (MessagingException e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
