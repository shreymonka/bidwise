package com.online.auction.service;

import com.online.auction.exception.ServiceException;

public interface AccountService {
    float getAccountBalance(Integer userId) throws ServiceException;
}
