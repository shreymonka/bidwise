package com.online.auction.service.impl;

import com.online.auction.utils.EmailUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class EmailUtilsTest {

    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailUtils emailUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendEmail_Success() {
        // Arrange
        String toEmail = "test@example.com";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act
        emailUtils.sendEmail(toEmail, subject, body);

        // Capture the SimpleMailMessage that was passed to the javaMailSender
        ArgumentCaptor<SimpleMailMessage> messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender).send(messageCaptor.capture());

        // Assert
        SimpleMailMessage capturedMessage = messageCaptor.getValue();
        assertEquals("auctionbidwise@gmail.com", capturedMessage.getFrom());
        assertEquals(toEmail, Objects.requireNonNull(capturedMessage.getTo())[0]);
        assertEquals(subject, capturedMessage.getSubject());
        assertEquals(body, capturedMessage.getText());
    }
}
