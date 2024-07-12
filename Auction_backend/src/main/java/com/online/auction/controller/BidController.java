package com.online.auction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BidController {
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public BidController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/bid/{topicId}")
    public void processBid(String msg, @DestinationVariable String topicId) throws Exception {
        String destination = "/topic/greetings/" + topicId;
        messagingTemplate.convertAndSend(destination, msg);
    }
}
