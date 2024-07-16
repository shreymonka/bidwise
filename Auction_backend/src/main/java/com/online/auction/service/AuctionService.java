package com.online.auction.service;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;

public interface AuctionService {
    Auction getAuctionDetails(int itemId) throws ServiceException;
}
