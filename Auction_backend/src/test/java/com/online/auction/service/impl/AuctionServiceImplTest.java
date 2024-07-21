package com.online.auction.service.impl;


import com.online.auction.dto.AuctionDTO;
import com.online.auction.dto.AuctionItemsDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Account;
import com.online.auction.model.Auction;
import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
import com.online.auction.model.ItemCondition;
import com.online.auction.model.User;
import com.online.auction.repository.AccountRepository;
import com.online.auction.repository.AuctionBidDetailRepository;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.repository.ItemRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.online.auction.constant.AuctionConstants.AUCTION_NOT_FOUND_MSG;
import static com.online.auction.constant.AuctionConstants.INTEGER_SEVEN;
import static com.online.auction.constant.TestConstants.END_TIME;
import static com.online.auction.constant.TestConstants.INTEGER_ONE;
import static com.online.auction.constant.TestConstants.START_TIME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;



class AuctionServiceImplTest {
    @Mock
    private AuctionListingRepository auctionListingRepository;

    @Mock
    private AuctionBidDetailRepository auctionBidDetailRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private AuctionServiceImpl auctionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAuctionDetailsTest() throws ServiceException {
        Item item = new Item();
        item.setItemId(INTEGER_ONE);

        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setAuctionId(String.valueOf(INTEGER_ONE));
        auctionDTO.setItemId(String.valueOf(INTEGER_ONE));
        auctionDTO.setSellerId(String.valueOf(INTEGER_ONE));

        User user = new User();
        user.setUserId(INTEGER_ONE);

        Auction auction = new Auction();
        auction.setAuctionId(INTEGER_ONE);
        auction.setItems(item);
        auction.setSellerId(user);
        when(auctionListingRepository.findByItems_ItemId(INTEGER_ONE)).thenReturn(Optional.of(auction));

        AuctionDTO result = auctionService.getAuctionDetails(INTEGER_ONE);

        assertEquals(auctionDTO, result);
    }

    @Test
    void getAuctionDetailsWhenAuctionNotPresentTest() {
        when(auctionListingRepository.findByItems_ItemId(INTEGER_ONE)).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            auctionService.getAuctionDetails(INTEGER_ONE);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(AUCTION_NOT_FOUND_MSG, exception.getErrorMessage());
    }

    @Test
    @SneakyThrows
    public void processPostAuctionStateSuccessTest() {
        User user = new User();
        user.setUserId(INTEGER_ONE);
        AuctionBidDetails auctionBidDetails = new AuctionBidDetails();
        auctionBidDetails.setWon(false);
        auctionBidDetails.setBidderId(user);

        when(auctionBidDetailRepository.findTopByItemIdOrderByBidAmountDesc(INTEGER_ONE)).thenReturn(auctionBidDetails);

        Auction auction = new Auction();
        auction.setOpen(true);
        when(auctionListingRepository.findByItems_ItemId(INTEGER_ONE)).thenReturn(Optional.of(auction));

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName(PAINTING_ITEM_CATEGORY);

        Item item1 = Item.builder()
                .itemId(INTEGER_ONE)
                .item_name(ITEM_NAME_1)
                .sellerId(user)
                .itemcategory(itemCategory)
                .build();

        when(itemRepository.findById(INTEGER_ONE)).thenReturn(Optional.of(item1));

        Account account = new Account();
        account.setUserId(INTEGER_ONE);
        account.setFunds(INTEGER_SEVEN);
        when(accountRepository.findByUserId(anyInt())).thenReturn(account);

        boolean result = auctionService.processPostAuctionState(INTEGER_ONE);

        assertTrue(result);
        assertTrue(auctionBidDetails.isWon());
        verify(auctionBidDetailRepository, times(INTEGER_ONE)).save(auctionBidDetails);
        verify(auctionListingRepository, times(INTEGER_ONE)).save(auction);
    }

    @Test
    public void processPostAuctionStateWhenStateAlreadyUpdatedTest() {
        User user = new User();
        user.setUserId(INTEGER_ONE);
        AuctionBidDetails auctionBidDetails = new AuctionBidDetails();
        auctionBidDetails.setWon(true);
        auctionBidDetails.setBidderId(user);

        when(auctionBidDetailRepository.findTopByItemIdOrderByBidAmountDesc(INTEGER_ONE)).thenReturn(auctionBidDetails);
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            auctionService.processPostAuctionState(INTEGER_ONE);
        });

        assertEquals(HttpStatus.OK.value(), exception.getStatusCode());
    }

    @Test
    public void processPostAuctionStateNoBidDetailsTest() {
        when(auctionBidDetailRepository.findTopByItemIdOrderByBidAmountDesc(INTEGER_ONE)).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            auctionService.processPostAuctionState(INTEGER_ONE);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(AUCTION_RECORD_NOT_FOUND_MSG, exception.getErrorMessage());
    }

    @Test
    void getUpcomingAuctionsTest() {


        List<Auction> auctions = new ArrayList<>();
        Item item = new Item();
        item.setItemId(INTEGER_ONE);
        item.setItem_name("Test Item");
        item.setItem_photo("photo_url");
        item.setItem_maker("Maker");
        item.setDescription("Description");
        item.setMin_bid_amount(100);
        item.setPrice_paid(200);
        item.setCurrency("USD");
        item.setItem_condition(ItemCondition.NEW);

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName("Electronics");

        Auction auction = new Auction();
        auction.setAuctionId(INTEGER_ONE);
        auction.setItems(item);
        auction.setStartTime(START_TIME);
        auction.setEndTime(END_TIME);
        auctions.add(auction);

        when(auctionListingRepository.findUpcomingAndCurrentAuctions(any(LocalDateTime.class))).thenReturn(auctions);

        List<AuctionItemsDTO> result = auctionService.getUpcomingAuctions();

        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getItemName());
    }

    @Test
    void getItemsForExistingUserTest() throws ServiceException {
        User user = new User();
        user.setUserId(INTEGER_ONE);

        Item item = new Item();
        item.setItemId(INTEGER_ONE);
        item.setItem_name("Test Item");
        item.setItem_photo("photo_url");
        item.setItem_maker("Maker");
        item.setDescription("Description");
        item.setMin_bid_amount(100);
        item.setPrice_paid(200);
        item.setCurrency("USD");
        item.setItem_condition(ItemCondition.NEW);

        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryName("Electronics");

        List<Item> items = new ArrayList<>();
        items.add(item);

        Auction auction = new Auction();
        auction.setAuctionId(INTEGER_ONE);
        auction.setItems(item);
        auction.setStartTime(START_TIME);
        auction.setEndTime(END_TIME);

        when(itemRepository.findUpcomingAndCurrentItemsExcludingUserItems(any(LocalDateTime.class), eq(user))).thenReturn(items);
        when(auctionListingRepository.findByItems_ItemId(item.getItemId())).thenReturn(Optional.of(auction));

        List<AuctionItemsDTO> result = auctionService.getItemsForExistingUser(INTEGER_ONE);

        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getItemName());
    }

    @Test
    void getItemsForExistingUserWhenAuctionNotPresentTest() {
        User user = new User();
        user.setUserId(INTEGER_ONE);

        Item item = new Item();
        item.setItemId(INTEGER_ONE);

        List<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findUpcomingAndCurrentItemsExcludingUserItems(any(LocalDateTime.class), eq(user))).thenReturn(items);
        when(auctionListingRepository.findByItems_ItemId(item.getItemId())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            auctionService.getItemsForExistingUser(INTEGER_ONE);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(AUCTION_NOT_FOUND_MSG, exception.getErrorMessage());
    }



}