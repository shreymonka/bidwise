package com.online.auction.controller;

import com.online.auction.dto.*;
import com.online.auction.exception.ServiceException;
import com.online.auction.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.USER;

@RestController
@CrossOrigin
@RequestMapping(API_VERSION_V1 + USER)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponse<String>> register(@RequestBody UserDTO userDto) throws ServiceException {
        String authenticationResponse = userService.register(userDto);
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, authenticationResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @RequestBody AuthenticationRequestDTO authenticationRequest
    ) throws ServiceException {
        return ResponseEntity.ok(userService.authenticate(authenticationRequest));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponseDTO> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        return ResponseEntity.ok(userService.refreshToken(request, response));
    }

    @GetMapping("/welcome")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<String> getWelcomeMsg() {
        return ResponseEntity.ok("Hey This is an Auction Site.");
    }

    @GetMapping("/welcome-admin")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> getWelcomeMsgForAdmin() {
        return ResponseEntity.ok("Hey Admin!");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<SuccessResponse<String>> forgotPassword(@RequestBody ResetEmailDTO emailDTO) throws ServiceException {
        String email = emailDTO.getEmail();
        String resetLinkResponse = userService.sendPasswordResetLink(email);
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, resetLinkResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<SuccessResponse<String>> resetPassword(@RequestBody ResetTokenAndPasswordDTO resetDTO) throws ServiceException {
        String token = resetDTO.getToken();
        String newPassword = resetDTO.getNewPassword();
        String passwordUpdateResponse = userService.resetPassword(token, newPassword);
        SuccessResponse<String> response = new SuccessResponse<>(200, HttpStatus.OK, passwordUpdateResponse);
        return ResponseEntity.ok(response);
    }
}