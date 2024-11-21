package com.example.User.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;

public class EmailTest {

    @Autowired
    private static EmailService emailService;

    public static void main(String[] args) {

        sendTestEmail();
    }

    // A simple method to send a test email
    public static void sendTestEmail() {
        // You would get this from the EmailService
        String subject = "Test Email from Main Class";
        String body = "<h1>This is a simple test email sent from the main class!</h1>";

        // Send email
        emailService.sendEmail(subject, body);

        System.out.println("Test email sent successfully!");
    }
}
