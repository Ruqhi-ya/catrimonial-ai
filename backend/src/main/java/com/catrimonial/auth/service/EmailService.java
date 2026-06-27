package com.catrimonial.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    @Async
    public void sendVerificationEmail(String email, String name, String token) {
        String subject = "Verify Your Email - Catrimonial AI";
        String verifyUrl = baseUrl + "/verify-email?token=" + token;
        String content = buildVerificationEmailHtml(name, verifyUrl);
        sendHtmlEmail(email, subject, content);
    }

    @Async
    public void sendPasswordResetEmail(String email, String name, String token) {
        String subject = "Reset Your Password - Catrimonial AI";
        String resetUrl = baseUrl + "/reset-password?token=" + token;
        String content = buildPasswordResetEmailHtml(name, resetUrl);
        sendHtmlEmail(email, subject, content);
    }

    @Async
    public void sendWelcomeEmail(String email, String name) {
        String subject = "Welcome to Catrimonial AI! 🐱";
        String content = buildWelcomeEmailHtml(name);
        sendHtmlEmail(email, subject, content);
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mimeMessage);
            log.info("Email sent to: {} with subject: {}", to, subject);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {} - {}", to, e.getMessage());
        }
    }

    private String buildVerificationEmailHtml(String name, String verifyUrl) {
        return """
                <!DOCTYPE html>
                <html>
                <head><meta charset="UTF-8"></head>
                <body style="font-family: 'Inter', sans-serif; background: #FAFCFF; padding: 40px;">
                    <div style="max-width: 600px; margin: 0 auto; background: white; border-radius: 20px; padding: 40px; box-shadow: 0 4px 20px rgba(0,0,0,0.05);">
                        <h1 style="color: #FF6B9A; text-align: center;">🐱 Catrimonial AI</h1>
                        <h2 style="color: #1E293B;">Hi %s! 👋</h2>
                        <p style="color: #64748B; font-size: 16px;">Welcome to the Responsible Cat Owner Network! Please verify your email to get started.</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" style="background: #FF6B9A; color: white; padding: 14px 32px; border-radius: 12px; text-decoration: none; font-weight: bold; font-size: 16px;">Verify Email</a>
                        </div>
                        <p style="color: #94A3B8; font-size: 14px;">This link expires in 24 hours. If you didn't create this account, you can ignore this email.</p>
                    </div>
                </body>
                </html>
                """.formatted(name, verifyUrl);
    }

    private String buildPasswordResetEmailHtml(String name, String resetUrl) {
        return """
                <!DOCTYPE html>
                <html>
                <head><meta charset="UTF-8"></head>
                <body style="font-family: 'Inter', sans-serif; background: #FAFCFF; padding: 40px;">
                    <div style="max-width: 600px; margin: 0 auto; background: white; border-radius: 20px; padding: 40px; box-shadow: 0 4px 20px rgba(0,0,0,0.05);">
                        <h1 style="color: #FF6B9A; text-align: center;">🐱 Catrimonial AI</h1>
                        <h2 style="color: #1E293B;">Password Reset</h2>
                        <p style="color: #64748B; font-size: 16px;">Hi %s, we received a request to reset your password. Click the button below to set a new password.</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" style="background: #FF6B9A; color: white; padding: 14px 32px; border-radius: 12px; text-decoration: none; font-weight: bold; font-size: 16px;">Reset Password</a>
                        </div>
                        <p style="color: #94A3B8; font-size: 14px;">This link expires in 1 hour. If you didn't request this, please ignore this email.</p>
                    </div>
                </body>
                </html>
                """.formatted(name, resetUrl);
    }

    private String buildWelcomeEmailHtml(String name) {
        return """
                <!DOCTYPE html>
                <html>
                <head><meta charset="UTF-8"></head>
                <body style="font-family: 'Inter', sans-serif; background: #FAFCFF; padding: 40px;">
                    <div style="max-width: 600px; margin: 0 auto; background: white; border-radius: 20px; padding: 40px; box-shadow: 0 4px 20px rgba(0,0,0,0.05);">
                        <h1 style="color: #FF6B9A; text-align: center;">🐱 Welcome to Catrimonial AI!</h1>
                        <h2 style="color: #1E293B;">Hi %s! 🎉</h2>
                        <p style="color: #64748B; font-size: 16px;">You're now part of the Responsible Cat Owner Network. Here's what you can do:</p>
                        <ul style="color: #64748B; font-size: 16px; line-height: 2;">
                            <li>🐾 Create profiles for your cats</li>
                            <li>❤️ Find compatible companions</li>
                            <li>💊 Track vaccinations & health</li>
                            <li>💬 Connect with other cat owners</li>
                            <li>🤖 Get AI-powered recommendations</li>
                        </ul>
                        <p style="color: #1E293B; font-weight: bold;">Start by adding your first cat profile!</p>
                    </div>
                </body>
                </html>
                """.formatted(name);
    }
}
