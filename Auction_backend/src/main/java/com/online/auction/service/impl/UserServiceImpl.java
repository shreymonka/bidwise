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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

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
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequest) throws ServiceException {
        return null;
    }

    @Override
    public AuthenticationResponseDTO refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        return null;
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }
}
