package com.online.auction.service.impl;

import com.online.auction.dto.AuctionDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Account;
import com.online.auction.model.Auction;
import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.Item;
import com.online.auction.repository.AccountRepository;
import com.online.auction.repository.AuctionBidDetailRepository;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.repository.ItemRepository;
import com.online.auction.service.AuctionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static com.online.auction.constant.AuctionConstants.AUCTION_NOT_FOUND_MSG;

@AllArgsConstructor
@Service
@Slf4j
public class AuctionServiceImpl implements AuctionService {

    private AuctionListingRepository auctionListingRepository;
    private AuctionBidDetailRepository auctionBidDetailRepository;
    private AccountRepository accountRepository;
    private ItemRepository itemRepository;

    @Override
    public AuctionDTO getAuctionDetails(int itemId) throws ServiceException {
        log.info("Fetching the Auction Details for the itemId: {}", itemId);
        Optional<Auction> auctionOptional = auctionListingRepository.findByItems_ItemId(itemId);
        if (auctionOptional.isEmpty()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, AUCTION_NOT_FOUND_MSG);
        }
        Auction auctionDb = auctionOptional.get();
        log.info("The Auction details in Db are: {}", auctionDb);
        return AuctionDTO.builder()
                .auctionId(String.valueOf(auctionDb.getAuctionId()))
                .isOpen(auctionDb.isOpen())
                .startTime(auctionDb.getStartTime())
                .endTime(auctionDb.getEndTime())
                .sellerId(String.valueOf(auctionDb.getSellerId().getUserId()))
                .itemId(String.valueOf(auctionDb.getItems().getItemId()))
                .build();
    }

    @Override
    @Transactional
    public boolean processPostAuctionState(int itemId) throws ServiceException {
        log.info("Started the auction Postprocessing");
        AuctionBidDetails auctionBidDetails = auctionBidDetailRepository.findTopByItemIdOrderByBidAmountDesc(itemId);
        if (Objects.isNull(auctionBidDetails)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Record not found to update post Auction state");
        }
        log.info("The winning bid information is : {}", auctionBidDetails);
        auctionBidDetails.setWon(true);
        auctionBidDetailRepository.save(auctionBidDetails);
        log.info("Successfully updated the winner in Auction Bid Details : {}", auctionBidDetails);

        Optional<Item> itemOptional = itemRepository.findById(itemId);
        if (!itemOptional.isEmpty()) {
            log.info("Updating the selling amount for the item: {}", itemOptional.get());
            Item item = itemOptional.get();
            item.setSelling_amount(auctionBidDetails.getBid_amount());
            item.setBuyerId(auctionBidDetails.getBidderId());
            itemRepository.save(item);
            log.info("Successfully updated the selling amount for the item: {}", item);
        }

        Account account = accountRepository.findByUserId(auctionBidDetails.getBidderId().getUserId());
        if (Objects.nonNull(account)) {
            log.info("Debiting the fund for the user: {}", account.getUserId());
            double currentFunds = account.getFunds();
            account.setFunds(currentFunds - auctionBidDetails.getBid_amount());
            accountRepository.save(account);
            log.info("Successfully debited the funds from the winner's account : {}", account);
        }

        Optional<Auction> auctionOptional = auctionListingRepository.findByItems_ItemId(itemId);
        if (!auctionOptional.isEmpty()) {
            Auction auction = auctionOptional.get();
            log.info("Closing the Auction: {}", auction);
            auction.setOpen(false);
            auctionListingRepository.save(auction);
            log.info("Successfully closed the Auction: {}", auction);
        }
        return true;
    }
}
