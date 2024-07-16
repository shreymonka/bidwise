package com.online.auction.service;

import com.online.auction.dto.AuctionDTO;
import com.online.auction.exception.ServiceException;

public interface AuctionService {
    AuctionDTO getAuctionDetails(int itemId) throws ServiceException;
}
