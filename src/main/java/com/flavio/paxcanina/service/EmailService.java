package com.flavio.paxcanina.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * EmailService
 * ------------
 * Handles all outgoing email operations (account validation, password reset, account creation, etc).
 * Uses JavaMailSender for sending HTML emails.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends an account creation email (for admin-created users),
     * containing a temporary password and a clickable validation link (all content in French).
     *
     * @param to            Recipient's email address
     * @param tempPassword  Temporary password generated for first login
     * @param validationUrl Validation link (with unique token)
     */
    public void sendAccountCreatedEmail(String to, String tempPassword, String validationUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Pax Canina - Votre compte a été créé");

            String html = String.format("""
                <p>Bonjour,</p>
                <p>Un administrateur a créé un compte pour vous sur <b>Pax Canina</b>.</p>
                <p>
                    <b>Votre mot de passe temporaire est&nbsp;:</b><br>
                    <code style="font-size:1.2em;background:#f5f5f5;padding:4px 8px;border-radius:4px;">%s</code>
                </p>
                <p>
                    Avant de pouvoir accéder à la plateforme, veuillez confirmer votre adresse e-mail en cliquant sur ce lien (valable 60 minutes)&nbsp;:<br>
                    <a href="%s" style="color:#1976d2;font-weight:bold;text-decoration:underline;">
                        👉 Cliquez ici pour confirmer votre adresse e-mail
                    </a>
                </p>
                <p>
                    ⚠️ Après avoir confirmé votre e-mail, connectez-vous avec le mot de passe ci-dessus puis changez-le dans votre profil pour garantir la sécurité de votre compte.
                </p>
                <p>
                    Si vous n'êtes pas à l'origine de cette demande, ignorez simplement ce message.<br>
                    <br>
                    Bienvenue dans la communauté <b>Pax Canina</b>&nbsp;!
                </p>
            """, tempPassword, validationUrl);

            helper.setText(html, true); // true = HTML email
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail", e);
        }
    }

    /**
     * Sends an account validation email with a clickable link (content in French).
     *
     * @param to            Recipient's email address
     * @param validationUrl Validation link (usually with a unique token)
     */
    public void sendValidationEmail(String to, String validationUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Pax Canina - Activez votre compte");

            String html = String.format("""
                <p>Bienvenue sur <b>Pax Canina</b>&nbsp;!</p>
                <p>
                    Pour activer votre compte, veuillez cliquer sur le lien ci-dessous (valable 60 minutes)&nbsp;:<br>
                    <a href="%s" style="color:#1976d2;font-weight:bold;text-decoration:underline;">
                        👉 Cliquez ici pour activer votre compte
                    </a>
                </p>
                <p>
                    Si vous n'avez pas demandé ce compte, veuillez ignorer cet e-mail.
                </p>
            """, validationUrl);

            helper.setText(html, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail", e);
        }
    }

    /**
     * Sends a password reset email with a clickable reset link (content in French).
     *
     * @param to       Recipient's email address
     * @param resetUrl Secure password reset link
     */
    public void sendPasswordResetEmail(String to, String resetUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject("Pax Canina - Réinitialisation du mot de passe");

            String html = String.format("""
                <p>Pour réinitialiser votre mot de passe, veuillez cliquer sur le lien ci-dessous (valable 60 minutes)&nbsp;:</p>
                <a href="%s" style="color:#1976d2;font-weight:bold;text-decoration:underline;">
                    👉 Cliquez ici pour réinitialiser votre mot de passe
                </a>
                <p>
                    Si vous n'avez pas demandé la réinitialisation du mot de passe, veuillez ignorer cet e-mail.
                </p>
            """, resetUrl);

            helper.setText(html, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail", e);
        }
    }
}
