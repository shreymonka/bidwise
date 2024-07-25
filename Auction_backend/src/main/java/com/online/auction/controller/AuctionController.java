package com.online.auction.controller;

import com.online.auction.dto.AuctionDTO;
import com.online.auction.dto.SuccessResponse;
import com.online.auction.dto.AuctionItemsDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.service.AuctionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.AUCTION_MAPPING;

@RestController
@AllArgsConstructor
@RequestMapping(API_VERSION_V1 + AUCTION_MAPPING)
public class AuctionController {
    private final AuctionService auctionService;

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<AuctionDTO>> auctionDetails(@PathVariable(value = "id") Integer itemId) throws ServiceException {
        AuctionDTO auction = auctionService.getAuctionDetails(itemId);
        SuccessResponse<AuctionDTO> successResponse = new SuccessResponse<>(200, HttpStatus.OK, auction);
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/postAuction/{itemId}")
    public ResponseEntity<SuccessResponse<Boolean>> postAuctionUpdate(@PathVariable(value = "itemId") Integer itemId) throws ServiceException {
        boolean isSuccess = auctionService.processPostAuctionState(itemId);
        SuccessResponse<Boolean> successResponse = new SuccessResponse<>(200, HttpStatus.OK, isSuccess);
        return ResponseEntity.ok(successResponse);
    }

    //For user who hasn't logged in show all the upcoming auctions
    @GetMapping("/upcoming")
    public List<AuctionItemsDTO> getUpcomingAuctions() throws ServiceException {
        return auctionService.getUpcomingAuctions();
    }

    //for user who is logged-in and has listed  item
    @GetMapping("/getAuction")
    public ResponseEntity<List<AuctionItemsDTO>> getItemsForExistingUser(@AuthenticationPrincipal User user
    ) throws ServiceException {
        List<AuctionItemsDTO> items = auctionService.getAuctionsForExistingUser(user.getUserId());
        return ResponseEntity.ok(items);
    }

}
