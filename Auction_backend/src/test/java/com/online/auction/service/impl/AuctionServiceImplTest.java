package com.online.auction.service.impl;


import com.online.auction.dto.AuctionDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Account;
import com.online.auction.model.Auction;
import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
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

import java.util.Optional;

import static com.online.auction.constant.AuctionConstants.AUCTION_NOT_FOUND_MSG;
import static com.online.auction.constant.AuctionConstants.INTEGER_SEVEN;
import static com.online.auction.constant.TestConstants.INTEGER_ONE;
import static com.online.auction.constant.TestConstants.ITEM_NAME_1;
import static com.online.auction.constant.TestConstants.PAINTING_ITEM_CATEGORY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    public void processPostAuctionStateNoBidDetailsTest() {
        when(auctionBidDetailRepository.findTopByItemIdOrderByBidAmountDesc(INTEGER_ONE)).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            auctionService.processPostAuctionState(INTEGER_ONE);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals("Record not found to update post Auction state", exception.getErrorMessage());
    }
}