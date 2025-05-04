package com.swasthyamitra.healthportal.controller;

import com.swasthyamitra.healthportal.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@Slf4j
@RequiredArgsConstructor
public class EmailAPI {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(
            @RequestParam String toEmail,
            @RequestParam String otp) {
        log.info("Sending email to {}", toEmail);
        boolean isSent = emailService.sendEmail(otp, toEmail);
        if (isSent) {
            return ResponseEntity.ok("Email sent successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to send email");
        }
    }
}
