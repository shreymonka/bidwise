package com.online.auction.controller;

import com.online.auction.dto.PaymentDetailsDTO;
import com.online.auction.dto.SuccessResponse;
import com.online.auction.exception.ServiceException;
import com.online.auction.service.UpgradeToPremiumService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.USER;

@RestController
@RequestMapping(API_VERSION_V1 + USER)
@RequiredArgsConstructor
public class UpgradeToPremiumController {
    private final UpgradeToPremiumService upgradeToPremiumService;
    @PostMapping("/upgrade-to-premium")
    public ResponseEntity<SuccessResponse<String>> upgradeToPremium(@RequestBody PaymentDetailsDTO paymentDetails) throws ServiceException {
        upgradeToPremiumService.upgradeToPremium(paymentDetails.getEmail());
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, "User upgraded to premium successfully");
        return ResponseEntity.ok(response);
    }
}
