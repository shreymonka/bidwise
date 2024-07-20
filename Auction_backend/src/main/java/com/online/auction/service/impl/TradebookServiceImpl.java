package com.online.auction.service.impl;

import com.online.auction.dto.InvoiceDTO;
import com.online.auction.dto.ItemDTO;
import com.online.auction.dto.TradebookDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.Item;
import com.online.auction.model.User;
import com.online.auction.repository.TradebookRepository;
import com.online.auction.service.TradebookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class TradebookServiceImpl implements TradebookService {
    private final TradebookRepository tradebookRepository;

    @Override
    public List<TradebookDTO> getAllTradesByUser(User user) throws ServiceException {
        log.debug("Fetching items for user: {}", user.getEmail());
        List<AuctionBidDetails> bids;
        try {
            bids = tradebookRepository.findAllByUser(user);
        } catch (Exception e) {
            log.error("Error fetching tradebook details for user: {}", user.getEmail(), e);
            throw new ServiceException(HttpStatus.NOT_FOUND,"Error fetching tradebook details");
        }

        if (bids == null || bids.isEmpty()) {
            log.error("No trades found for user: {}", user.getEmail());
            throw new ServiceException(HttpStatus.NOT_FOUND,"No trades found for user");
        }
        return bids.stream()
                .map(bid -> TradebookDTO.builder()
                        .nameOfItem(bid.getItemId().getItem_name())
                        .soldPrice(bid.getItemId().getSelling_amount())
                        .userHighestBid(bid.getBid_amount())
                        .dateOfAuction(bid.getAuctionId().getEndTime())
                        .isAuctionWon(bid.isWon())
                        .AuctionId(bid.getAuctionId().getAuctionId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO getInvoiceByAuctionId(int auctionId) throws ServiceException {
        AuctionBidDetails bidDetails = tradebookRepository.findByAuctionIdAndIsWonTrue(auctionId)
                .orElseThrow(() -> new ServiceException(HttpStatus.NOT_FOUND, "No winning bid found for the given auction ID"));

        Auction auction = bidDetails.getAuctionId();
        Item item = bidDetails.getItemId();
        User seller = item.getSellerId();

        return InvoiceDTO.builder()
                .itemName(item.getItem_name())
                .sellerName(seller.getFirstName() + " " + seller.getLastName())
                .sellerEmail(seller.getEmail())
                .itemCategory(item.getItemcategory().getItemCategoryName())
                .dateOfAuction(auction.getEndTime())
                .dateOfInvoice(LocalDateTime.now())
                .pricePaid(bidDetails.getBid_amount())
                .build();
    }
}
