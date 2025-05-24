package com.swasthyamitra.healthportal.service.impl;

import com.swasthyamitra.healthportal.service.EmailService;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${app.url.base}")
    private String baseUrl;

    @Value("${app.url.reset}")
    private String appResetUrl;

    @Override
    public boolean sendEmail(String resetLink, String toEmail) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            String emailBodyHtml = prepareEmailBodyHtml(resetLink);
            String emailBodyText = "Click the following link to reset your password: " + resetLink;

            helper.setFrom(new InternetAddress(sender));
            helper.setTo(toEmail);
            helper.setSubject("Reset Your Password");
            helper.setText(emailBodyText, emailBodyHtml);

            javaMailSender.send(mimeMessage);

            log.info("Reset password email sent to {}", toEmail);
            return true;
        } catch (Exception e) {
            log.error("Failed to send reset password email to {}", toEmail, e);
            return false;
        }
    }

    @Override
    public void sendForgotPasswordMail(String email, String token) {
        String resetPasswordLink = baseUrl + appResetUrl;
        String link = resetPasswordLink.replace("{token}", token);

        log.info("Sending forgot password email to: {}", email);
        log.debug("Reset password link: {}", link); // Only for debug, remove in production

        sendEmail(link, email);
    }



    private String prepareEmailBodyHtml(String resetLink) {
        return "<html><body>" +
                "<p><strong>Dear User,</strong></p>" +
                "<p>You requested to reset your password. Click the button below to proceed:</p>" +
                "<p><a href=\"" + resetLink + "\" style=\"" +
                "background-color: #4CAF50;" +
                "color: white;" +
                "padding: 10px 20px;" +
                "text-align: center;" +
                "text-decoration: none;" +
                "display: inline-block;" +
                "border-radius: 4px;" +
                "\">Reset Password</a></p>" +
                "<p>If you didn’t request this, you can safely ignore this email.</p>" +
                "<p>Thank you,<br>Swasthya Mitra Team</p>" +
                "</body></html>";
    }

}
