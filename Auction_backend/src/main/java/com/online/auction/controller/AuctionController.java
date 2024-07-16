package com.online.auction.controller;

import com.online.auction.dto.SuccessResponse;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.service.AuctionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.AUCTION_MAPPING;

@RestController
@AllArgsConstructor
@RequestMapping(API_VERSION_V1 + AUCTION_MAPPING)
public class AuctionController {
    private final AuctionService auctionService;

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Auction>> auctionDetails(@PathVariable(value = "id") Integer itemId) throws ServiceException {
        Auction auction = auctionService.getAuctionDetails(itemId);
        SuccessResponse<Auction> successResponse = new SuccessResponse<>(200, HttpStatus.OK, auction);
        return ResponseEntity.ok(successResponse);
    }

}
