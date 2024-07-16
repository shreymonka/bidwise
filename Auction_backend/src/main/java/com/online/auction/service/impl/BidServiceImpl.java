package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.Item;
import com.online.auction.model.User;
import com.online.auction.repository.AuctionBidDetailRepository;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.repository.ItemRepository;
import com.online.auction.repository.UserRepository;
import com.online.auction.service.BidService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.online.auction.constant.AuctionConstants.AUCTION_NOT_FOUND_MSG;
import static com.online.auction.constant.AuctionConstants.ITEM_NOT_FOUND_MSG;
import static com.online.auction.constant.AuctionConstants.USER_NOT_PRESENT_MSG;

@AllArgsConstructor
@Service
@Slf4j
public class BidServiceImpl implements BidService {

    private AuctionBidDetailRepository auctionBidDetailRepository;
    private AuctionListingRepository auctionListingRepository;
    private ItemRepository itemRepository;
    private UserRepository userRepository;

    @Override
    public void processBid(String bidAmount, String itemId, String userEmail) throws ServiceException {
        log.info("Started the processBid method");
        Optional<Auction> auction = auctionListingRepository.findByItems_ItemId(Integer.parseInt(itemId));
        log.info("The auction details are:{}", auction);
        Optional<Item> item = itemRepository.findById(Integer.parseInt(itemId));
        log.info("The Item details are: {}", item);
        Optional<User> user = userRepository.findByEmail(userEmail);
        log.info("The user details are: {}", user);
        if (!auction.isPresent()) {
            log.error("Auction not found for the corresponding Item Id");
            throw new ServiceException(HttpStatus.BAD_REQUEST, AUCTION_NOT_FOUND_MSG);
        }
        if (!item.isPresent()) {
            log.error("Item not found with the given Item Id");
            throw new ServiceException(HttpStatus.BAD_REQUEST, ITEM_NOT_FOUND_MSG);
        }
        if (!user.isPresent()) {
            log.error("User not found with the given Email Id");
            throw new ServiceException(HttpStatus.BAD_REQUEST, USER_NOT_PRESENT_MSG);
        }
        AuctionBidDetails auctionBidDetails = AuctionBidDetails.builder()
                .auctionId(auction.get())
                .bidTime(LocalDateTime.now())
                .bid_amount(Double.parseDouble(bidAmount))
                .itemId(item.get())
                .bidderId(user.get())
                .build();
        auctionBidDetailRepository.save(auctionBidDetails);
        log.info("Successfully saved the bid details to the Auction Bid Details table");
        log.info("Completed the processBid method");
    }
}