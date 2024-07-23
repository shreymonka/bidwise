package com.online.auction.controller;

import com.online.auction.service.BidService;
import com.online.auction.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.online.auction.constant.AuctionConstants.AUTHORIZATION;
import static com.online.auction.constant.AuctionConstants.INTEGER_ZERO;
import static com.online.auction.constant.AuctionConstants.NATIVE_HEADERS;
import static com.online.auction.constant.AuctionConstants.USER_NOT_PRESENT_MSG;

@RestController
@AllArgsConstructor
public class BidController {
    private final SimpMessagingTemplate messagingTemplate;
    private BidService bidService;
    private JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @MessageMapping("/bid/{topicId}")
    public void processBid(String msg, @DestinationVariable String topicId, @Headers Map<String, ?> header) throws Exception {
        LinkedMultiValueMap<String, String> authHeader = (LinkedMultiValueMap<String, String>) header.get(NATIVE_HEADERS);
        String userEmail = "";
        try {
            userEmail = jwtService.extractUsername(authHeader.get(AUTHORIZATION).get(INTEGER_ZERO));
        } catch (Exception e) {
            throw new UsernameNotFoundException(USER_NOT_PRESENT_MSG);
        }
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
        boolean isTokenValid = jwtService.isTokenValid(authHeader.get(AUTHORIZATION).get(0), userDetails);
        String destination = "/topic/greetings/" + topicId;
        bidService.processBid(msg, topicId, userEmail);
        messagingTemplate.convertAndSend(destination, msg);
    }
}
