package com.example.User.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)  // This will initialize mocks automatically
public class EmailServiceTest {

    // Mock the JavaMailSender
    @Mock
    private JavaMailSender javaMailSender;

    // Inject mocks into the EmailService
    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendEmail() {
        // Arrange: prepare mock email details
        String subject = "Test Subject";
        String text = "Test email body content.";

        // Act: Call the method
        emailService.sendEmail(subject, text);

        // Assert: Verify that the send method was called with the expected message
        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom("nisha123@test.com");
        expectedMessage.setTo("test@example.com"); // Use the email value from properties or hardcoded
        expectedMessage.setSubject(subject);
        expectedMessage.setText(text);

    }
}
