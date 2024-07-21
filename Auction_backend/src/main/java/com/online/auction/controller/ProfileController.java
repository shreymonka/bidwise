package com.online.auction.controller;

import com.online.auction.dto.SuccessResponse;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.PROFILE;

@RestController
@RequestMapping(API_VERSION_V1 + PROFILE)
@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @GetMapping("/details")
    public ResponseEntity<SuccessResponse<User>> getUserDetails(
            @AuthenticationPrincipal User user) throws ServiceException {
        profileService.getUserDetails(user.getUserId());
        SuccessResponse<User> response = new SuccessResponse<>(200, HttpStatus.OK, user);
        return ResponseEntity.ok(response);
    }
}
