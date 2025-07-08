package com.example.mailapp.controller;

import com.example.mailapp.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    @Value("${predefined.email}")
    private String predefinedEmail;

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(
            @RequestHeader("Authorization") String bearerToken,
            @RequestPart("file") MultipartFile file,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message) {
        try {
            String accessToken = bearerToken.replace("Bearer ", "");
            mailService.sendMail(subject, message, file, accessToken, predefinedEmail);
            return ResponseEntity.ok("Mail sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Mail failed: " + e.getMessage());
        }
    }
}
