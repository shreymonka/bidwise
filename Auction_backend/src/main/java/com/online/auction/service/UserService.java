package com.online.auction.service;

import com.online.auction.dto.AuthenticationRequestDTO;
import com.online.auction.dto.AuthenticationResponseDTO;
import com.online.auction.dto.UserDTO;
import com.online.auction.exception.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface UserService {
    AuthenticationResponseDTO register(UserDTO userDto) throws ServiceException;

    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequest) throws ServiceException;

    AuthenticationResponseDTO refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException;
}
