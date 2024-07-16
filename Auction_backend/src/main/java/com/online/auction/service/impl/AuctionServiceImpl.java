package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.service.AuctionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.online.auction.constant.AuctionConstants.AUCTION_NOT_FOUND_MSG;

@AllArgsConstructor
@Service
public class AuctionServiceImpl implements AuctionService {

    private AuctionListingRepository auctionListingRepository;

    @Override
    public Auction getAuctionDetails(int itemId) throws ServiceException {
        Optional<Auction> auctionOptional = auctionListingRepository.findByItems_ItemId(itemId);
        if (auctionOptional.isEmpty()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, AUCTION_NOT_FOUND_MSG);
        }
        return auctionOptional.get();
    }
}
