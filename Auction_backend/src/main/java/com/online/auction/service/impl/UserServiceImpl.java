package com.online.auction.service.impl;

import com.online.auction.dto.AuthenticationRequestDTO;
import com.online.auction.dto.AuthenticationResponseDTO;
import com.online.auction.dto.UserDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Token;
import com.online.auction.model.TokenType;
import com.online.auction.model.User;
import com.online.auction.repository.TokenRepository;
import com.online.auction.repository.UserRepository;
import com.online.auction.service.JwtService;
import com.online.auction.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static com.online.auction.constant.AuctionConstants.INVALID_CREDENTIALS_MSG;
import static com.online.auction.constant.AuctionConstants.USER_ALREADY_PRESENT_MSG;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponseDTO register(UserDTO userDto) {
        log.info("User register call started in the UserServiceImpl");
        var user = com.online.auction.model.User.builder()
                .firstName(userDto.getFirstname())
                .lastName(userDto.getLastname())
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .role(userDto.getRole())
                .build();

        log.info("Checking if the user is present in the database");
        Optional<User> userEntryInDb = userRepository.findByEmail(userDto.getEmail());
        if (userEntryInDb.isPresent()) {
            log.error("The user is already present in the database");
            throw new UsernameNotFoundException(USER_ALREADY_PRESENT_MSG);
        }
        var userDb = userRepository.save(user);
        log.info("Successfully saved the user information in the database");
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(userDb, jwtToken);
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) throws ServiceException {
        log.info("User authenticate call started in the UserServiceImpl");
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequestDTO.getEmail(),
                            authenticationRequestDTO.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.error("Invalid Credentials Exception: {}", e.getMessage());
            throw new ServiceException(HttpStatus.FORBIDDEN, INVALID_CREDENTIALS_MSG);
        }

        var user = userRepository.findByEmail(authenticationRequestDTO.getEmail())
                .orElseThrow();
        log.info("Generating the Access token for the user");
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        log.info("Successfully completed the access token generation for the user:{}", user);
        return AuthenticationResponseDTO.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponseDTO refreshToken(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        log.info("Refresh token generation call started in the UserServiceImpl");
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("The Authentication header is NULL in the refresh token generation");
            return null;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if (userEmail != null) {
            log.info("The user found with the email Id: {}", userEmail);
            var user = this.userRepository.findByEmail(userEmail)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                log.info("Generating the new Access token with the Refresh token for the user: {}", user);
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponseDTO.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                return authResponse;
            }
        }
        return null;
    }

    private void saveUserToken(User user, String jwtToken) {
        log.info("Saving the new Access token generated for the user : {}", user);
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        log.info("Revoking all the older access token for the user :{}", user);
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
