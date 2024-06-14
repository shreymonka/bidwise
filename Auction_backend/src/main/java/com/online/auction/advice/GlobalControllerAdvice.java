package com.online.auction.advice;

import com.online.auction.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.online.auction.constant.AuctionConstants.ERROR_MSG;
import static com.online.auction.constant.AuctionConstants.HTTP_CODE;
import static com.online.auction.constant.AuctionConstants.HTTP_STATUS;
import static com.online.auction.constant.AuctionConstants.TIMESTAMP;

@ControllerAdvice
public class GlobalControllerAdvice {
    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Map<String, Object>> userNameNotFound(
            UsernameNotFoundException ex, WebRequest request) {
        Map<String, Object> errorInfo = new LinkedHashMap<>();
        errorInfo.put(TIMESTAMP, new Date());
        errorInfo.put(HTTP_CODE, HttpStatus.BAD_REQUEST.value());
        errorInfo.put(HTTP_STATUS, HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorInfo.put(ERROR_MSG, ex.getMessage());
        return new ResponseEntity<Map<String, Object>>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Map<String, Object>> accessDeniedException(
            UsernameNotFoundException ex, WebRequest request) {
        Map<String, Object> errorInfo = new LinkedHashMap<>();
        errorInfo.put(TIMESTAMP, new Date());
        errorInfo.put(HTTP_CODE, HttpStatus.FORBIDDEN.value());
        errorInfo.put(HTTP_STATUS, HttpStatus.FORBIDDEN.getReasonPhrase());
        errorInfo.put(ERROR_MSG, ex.getMessage());
        return new ResponseEntity<Map<String, Object>>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<Map<String, Object>> serviceException(
            ServiceException ex, WebRequest request) {
        Map<String, Object> errorInfo = new LinkedHashMap<>();
        errorInfo.put(TIMESTAMP, new Date());
        errorInfo.put(HTTP_CODE, ex.getStatusCode());
        errorInfo.put(HTTP_STATUS, HttpStatus.valueOf(ex.getStatusCode()).getReasonPhrase());
        errorInfo.put(ERROR_MSG, ex.getErrorMessage());
        return new ResponseEntity<Map<String, Object>>(errorInfo, HttpStatus.valueOf(ex.getStatusCode()));
    }
}
