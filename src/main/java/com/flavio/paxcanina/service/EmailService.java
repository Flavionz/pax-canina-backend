package com.flavio.paxcanina.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * EmailService
 * ------------
 * Centralized HTML email sender for:
 *  - account creation (temp password + verify link)
 *  - email verification
 *  - password reset
 *
 * Notes:
 *  - Keep content simple and brand-consistent.
 *  - All templates are inline for now; extract to files if they grow.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /** Optional "from" address configured in application.properties; fallback to default if empty. */
    @Value("${mail.from:}")
    private String fromAddress;

    /* ===================
       Public entry points
       =================== */

    public void sendAccountCreatedEmail(@NonNull String to,
                                        @NonNull String tempPassword,
                                        @NonNull String validationUrl) {
        String subject = "Pax Canina - Votre compte a été créé";

        String html = """
            <p>Bonjour,</p>
            <p>Un administrateur a créé un compte pour vous sur <b>Pax Canina</b>.</p>
            <p>
                <b>Mot de passe temporaire&nbsp;:</b><br>
                <code style="font-size:1.1em;background:#f5f5f5;padding:4px 8px;border-radius:4px;">%s</code>
            </p>
            <p>
                Veuillez confirmer votre adresse e-mail (lien valable 60 minutes)&nbsp;:<br>
                <a href="%s" style="color:#1976d2;font-weight:bold;text-decoration:underline;">
                    👉 Confirmer mon adresse e-mail
                </a>
            </p>
            <p>Après validation, connectez-vous et changez votre mot de passe depuis votre profil.</p>
            <p style="color:#666">Si vous n'êtes pas à l'origine de cette demande, ignorez ce message.</p>
            """.formatted(tempPassword, validationUrl);

        sendHtml(to, subject, html);
    }

    public void sendValidationEmail(@NonNull String to,
                                    @NonNull String validationUrl) {
        String subject = "Pax Canina - Activez votre compte";

        String html = """
            <p>Bienvenue sur <b>Pax Canina</b> !</p>
            <p>
                Pour activer votre compte (lien valable 60 minutes)&nbsp;:<br>
                <a href="%s" style="color:#1976d2;font-weight:bold;text-decoration:underline;">
                    👉 Activer mon compte
                </a>
            </p>
            <p style="color:#666">Si vous n'avez pas demandé ce compte, ignorez cet e-mail.</p>
            """.formatted(validationUrl);

        sendHtml(to, subject, html);
    }

    public void sendPasswordResetEmail(@NonNull String to,
                                       @NonNull String resetUrl) {
        String subject = "Pax Canina - Réinitialisation du mot de passe";

        String html = """
            <p>Vous avez demandé à réinitialiser votre mot de passe.</p>
            <p>
                Cliquez sur le lien ci-dessous (valable 30 minutes)&nbsp;:<br>
                <a href="%s" style="color:#1976d2;font-weight:bold;text-decoration:underline;">
                    👉 Réinitialiser mon mot de passe
                </a>
            </p>
            <p style="color:#666">Si vous n'êtes pas à l'origine de cette demande, ignorez cet e-mail.</p>
            """.formatted(resetUrl);

        sendHtml(to, subject, html);
    }

    public void sendPasswordChangedEmail(@NonNull String to) {
        String subject = "Pax Canina - Votre mot de passe a été modifié";

        String html = """
        <p>Bonjour,</p>
        <p>Votre mot de passe <b>Pax Canina</b> vient d'être modifié.</p>
        <p style="margin: 12px 0 0 0;">
            Si ce n'était pas vous, <b>modifiez-le à nouveau immédiatement</b> et contactez le support.
        </p>
        <p style="color:#666;margin-top:12px;">
            Ceci est un message automatique — aucune réponse n'est requise.
        </p>
        """;

        sendHtml(to, subject, html);
    }


    /* ==============
       Private helper
       ============== */

    /**
     * Minimal, reusable HTML sender.
     * - Sets UTF-8 properly
     * - Supports optional configured "from" address
     * - Wraps MessagingException into RuntimeException (service layer simplicity)
     */
    private void sendHtml(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // "true" → multipart; ensures proper encoding for some clients
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // HTML

            if (fromAddress != null && !fromAddress.isBlank()) {
                helper.setFrom(fromAddress);
            }

            mailSender.send(message);
            log.debug("Email sent: to={} subject={}", to, subject);

        } catch (MessagingException e) {
            log.error("Email send failed: to={}, subject={}", to, subject, e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
