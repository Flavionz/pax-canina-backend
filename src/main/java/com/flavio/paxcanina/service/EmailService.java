package com.flavio.paxcanina.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * EmailService handles all outgoing email operations (account validation, password reset, etc).
 * Uses Spring Boot's JavaMailSender for sending plain text or HTML emails.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a simple email for account validation with a tokenized URL.
     * @param to Recipient's email address
     * @param validationUrl Validation link (usually including a unique token)
     */
    public void sendValidationEmail(String to, String validationUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Pax Canina - Activate your account");
        message.setText(
                "Welcome to Pax Canina!\n\n" +
                        "To activate your account, please click the following link (valid for 60 minutes):\n" +
                        validationUrl + "\n\n" +
                        "If you did not request this account, please ignore this email."
        );
        mailSender.send(message);
    }

    /**
     * Example: Sends a password reset email with a secure reset link.
     * (You can use this for future password-reset features)
     */
    public void sendPasswordResetEmail(String to, String resetUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Pax Canina - Password reset");
        message.setText(
                "To reset your password, please click the following link (valid for 60 minutes):\n" +
                        resetUrl + "\n\n" +
                        "If you did not request a password reset, please ignore this email."
        );
        mailSender.send(message);
    }

    // You can add other methods for HTML emails or notifications if needed.
}
