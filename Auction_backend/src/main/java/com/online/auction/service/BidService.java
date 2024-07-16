package com.online.auction.service;

import com.online.auction.exception.ServiceException;

public interface BidService {
    void processBid(String bidAmount,String itemId,String userEmail) throws ServiceException;
}
