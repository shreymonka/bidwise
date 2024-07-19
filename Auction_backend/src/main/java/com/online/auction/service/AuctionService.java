package com.online.auction.service;

import com.online.auction.dto.AuctionDTO;
import com.online.auction.dto.AuctionItemsDTO;
import com.online.auction.exception.ServiceException;
import java.util.List;

public interface AuctionService {
    AuctionDTO getAuctionDetails(int itemId) throws ServiceException;

    boolean processPostAuctionState(int itemId) throws ServiceException;

    List<AuctionItemsDTO> getUpcomingAuctions();
    List<AuctionItemsDTO> getItemsForExistingUser(int sellerId) throws ServiceException;

}
