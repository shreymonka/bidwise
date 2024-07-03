package com.online.auction.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
        @Autowired
        private JavaMailSender javaMailSender;
        public void sendEmail(String toEmail, String subject, String body){
            log.info("All information for mail received");
            SimpleMailMessage message = new SimpleMailMessage();
            log.info("Auction Bidwise email set");
            message.setFrom("auctionbidwise@gmail.com");
            log.info("Recepient email set");
            message.setTo(toEmail);
            log.info("Body of email set");
            message.setText(body);
            log.info("Subject of email set");
            message.setSubject(subject);
            log.info("Mail successfully sent");
            javaMailSender.send(message);
        }

}
