package com.online.auction.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.online.auction.dto.CustomErrorResponseDTO;
import com.online.auction.repository.TokenRepository;
import com.online.auction.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;

import static com.online.auction.constant.AuctionConstants.APPLICATION_JSON;
import static com.online.auction.constant.AuctionConstants.AUTHORIZATION;
import static com.online.auction.constant.AuctionConstants.BEARER;
import static com.online.auction.constant.AuctionConstants.INTEGER_SEVEN;
import static com.online.auction.constant.AuctionConstants.JWT_EXPIRED_MSG;
import static com.online.auction.constant.AuctionConstants.UNAUTHORIZED;
import static com.online.auction.constant.AuctionConstants.UNAUTHORIZED_STATUS_CODE;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(INTEGER_SEVEN);
        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (ExpiredJwtException e) {
            handleExpiredJwtException(response);
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void handleExpiredJwtException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(APPLICATION_JSON);
        String jsonResponse = new ObjectMapper().writeValueAsString(new CustomErrorResponseDTO(
                new Date().toString(),
                UNAUTHORIZED_STATUS_CODE,
                UNAUTHORIZED,
                JWT_EXPIRED_MSG
        ));
        response.getWriter().write(jsonResponse);
    }
}
