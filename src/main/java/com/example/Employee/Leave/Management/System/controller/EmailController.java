package com.example.Employee.Leave.Management.System.controller;

import com.example.Employee.Leave.Management.System.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping ("/test")
    public ResponseEntity<String> testEmail(@RequestParam String to) {
        try {
            emailService.sendSimple(to, "Test Email", "Your email service is working!");
            return ResponseEntity.ok("Email sent to " + to);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error: " +e.getMessage());
        }
    }
}
