package com.online.auction.service.impl;

import com.online.auction.dto.AuctionDTO;
import com.online.auction.dto.AuctionItemsDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Account;
import com.online.auction.model.Auction;
import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.Item;
import com.online.auction.repository.AccountRepository;
import com.online.auction.repository.AuctionBidDetailRepository;
import com.online.auction.model.User;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.repository.ItemRepository;
import com.online.auction.service.AuctionService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Objects;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.online.auction.constant.AuctionConstants.AMERICAN_TIME_ZONE;
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
        if (auctionBidDetails.isWon()) {
            log.info("The Auction state is already updated");
            throw new ServiceException(HttpStatus.OK, "Record Already updated");
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
            double updatedFunds = currentFunds - auctionBidDetails.getBid_amount();
            if (updatedFunds >= 0) {
                account.setFunds(updatedFunds);
                accountRepository.save(account);
                log.info("Successfully debited the funds from the winner's account : {}", account);
            }
        }

        // Crediting funds to the seller's account
        Account sellerAccount = accountRepository.findByUserId(auctionBidDetails.getItemId().getSellerId().getUserId());
        if (Objects.nonNull(sellerAccount)) {
            log.info("Crediting the fund to the seller: {}", sellerAccount.getUserId());
            double sellerCurrentFunds = sellerAccount.getFunds();
            sellerAccount.setFunds(sellerCurrentFunds + auctionBidDetails.getBid_amount());
            accountRepository.save(sellerAccount);
            log.info("Successfully credited the funds to the seller's account : {}", sellerAccount);
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

    /**
     * Retrieves a list of upcoming auctions and maps them to AuctionItemsDTO.
     * <p>
     * This method fetches the list of upcoming and current auctions from the
     * repository using the current time as the reference. It then maps each
     * Auction entity to an AuctionItemsDTO, which includes details about the item
     * and auction, such as item ID, name, photo, maker, description, minimum bid
     * amount, price paid, currency, item condition, item category, auction start
     * and end times, and the seller's city name.
     *
     * @return List of AuctionItemsDTO representing upcoming auctions.
     */
    @Override
    public List<AuctionItemsDTO> getUpcomingAuctions() {
        log.info("Started the method getUpcomingAuctions");
        List<Auction> upcomingAuctions = auctionListingRepository.findUpcomingAndCurrentAuctions(
                LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(AMERICAN_TIME_ZONE)).toLocalDateTime()
        );
        log.info("The upcomingAuctions is: {}", upcomingAuctions);
        log.info("Completed the getUpcomingAuctions method");

        return upcomingAuctions.stream()
                .map(auction -> AuctionItemsDTO.builder()
                        .itemId(auction.getItems().getItemId())
                        .itemName(auction.getItems().getItem_name())
                        .auctionStartTime(auction.getStartTime())
                        .itemPhoto(auction.getItems().getItem_photo())
                        .itemMaker(auction.getItems().getItem_maker())
                        .description(auction.getItems().getDescription())
                        .minBidAmount(auction.getItems().getMin_bid_amount())
                        .price_paid(auction.getItems().getPrice_paid())
                        .currency(auction.getItems().getCurrency())
                        .item_condition(auction.getItems().getItem_condition())
                        .itemcategory(auction.getItems().getItemcategory())
                        .auctionEndTime(auction.getEndTime())
                        .cityName(auction.getSellerId().getCity().getCityName())
                        .build())
                .collect(Collectors.toList());

    }

    /**
     * Retrieves a list of auctions for items not owned by the specified user
     * and maps them to AuctionItemsDTO.
     * <p>
     * This method fetches the list of upcoming and current items that are not
     * owned by the specified user from the item repository. For each item, it
     * fetches the associated auction details from the auction listing repository
     * and maps them to AuctionItemsDTO. If an auction is not found for an item,
     * it throws a ServiceException. The AuctionItemsDTO includes details about
     * the item and auction, such as item ID, name, photo, maker, description,
     * minimum bid amount, price paid, currency, item condition, item category,
     * auction start and end times, and the seller's city name.
     *
     * @param sellerId ID of the seller for whom the auctions are to be retrieved.
     * @return List of AuctionItemsDTO representing auctions for items not owned by the specified user.
     * @throws ServiceException if an auction is not found for an item.
     */
    @Override
    public List<AuctionItemsDTO> getAuctionsForExistingUser(int sellerId) throws ServiceException {
        log.info("Started the getAuctionsForExistingUser method");
        User seller = new User();
        seller.setUserId(sellerId);

        List<Item> items = itemRepository.findUpcomingAndCurrentItemsExcludingUserItems(
                LocalDateTime.now().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of(AMERICAN_TIME_ZONE)).toLocalDateTime()
                , seller);
        log.info("The items are: {}", items);
        List<AuctionItemsDTO> auctionItemsDTOList = new ArrayList<>();
        for (Item item : items) {
            Optional<Auction> auctionOptional = auctionListingRepository.findByItems_ItemId(item.getItemId());
            if (auctionOptional.isEmpty()) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, AUCTION_NOT_FOUND_MSG);
            }
            log.info("The Auctions for the itemId : {} is: {}", item.getItemId(), auctionOptional.get());
            Auction auctionDb = auctionOptional.get();
            AuctionItemsDTO auctionItemsDTO = AuctionItemsDTO.builder()
                    .itemId(item.getItemId())
                    .itemName(item.getItem_name())
                    .auctionStartTime(auctionDb.getStartTime())
                    .itemPhoto(item.getItem_photo())
                    .itemMaker(item.getItem_maker())
                    .description(item.getDescription())
                    .minBidAmount(item.getMin_bid_amount())
                    .price_paid(item.getPrice_paid())
                    .currency(item.getCurrency())
                    .item_condition(item.getItem_condition())
                    .itemcategory(item.getItemcategory())
                    .auctionEndTime(auctionDb.getEndTime())
                    .cityName(auctionDb.getSellerId().getCity().getCityName())
                    .build();
            auctionItemsDTOList.add(auctionItemsDTO);
        }
        log.info("The Final auction list is: {}", auctionItemsDTOList);
        log.info("Completed the getAuctionsForExistingUser method");
        return auctionItemsDTOList;
    }
}
