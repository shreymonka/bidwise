package com.online.auction.service.impl;

import com.online.auction.dto.TradebookDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.Item;
import com.online.auction.model.User;
import com.online.auction.repository.TradebookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.online.auction.constant.TestConstants.TEST_EMAIL;
import static com.online.auction.constant.TestConstants.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TradebookServiceImplTest {
    @Mock
    private TradebookRepository tradebookRepository;

    @InjectMocks
    private TradebookServiceImpl tradebookService;

    private User testUser;
    private Auction testAuction;
    private Item testItem;

    @BeforeEach
    public void setUp() {
        testUser = new User();
        testUser.setUserId(1);
        testUser.setEmail(TEST_EMAIL);

        testItem = new Item();
        testItem.setItemId(1);
        testItem.setItem_name("Test Item");
        testItem.setSelling_amount(100.0);

        testAuction = new Auction();
        testAuction.setAuctionId(1);
        testAuction.setEndTime(LocalDateTime.now());

        AuctionBidDetails bidDetails1 = AuctionBidDetails.builder()
                .auctionId(testAuction)
                .itemId(testItem)
                .bidderId(testUser)
                .bid_amount(110.0)
                .isWon(true)
                .build();

        AuctionBidDetails bidDetails2 = AuctionBidDetails.builder()
                .auctionId(testAuction)
                .itemId(testItem)
                .bidderId(testUser)
                .bid_amount(90.0)
                .isWon(false)
                .build();

        when(tradebookRepository.findAllByUser(testUser))
                .thenReturn(Arrays.asList(bidDetails1, bidDetails2));
    }
    @Test
    public void testGetAllTradesByUser_Success() throws ServiceException {
        List<TradebookDTO> tradebookDTOList = tradebookService.getAllTradesByUser(testUser);

        assertEquals(2, tradebookDTOList.size());
        TradebookDTO dto1 = tradebookDTOList.get(0);
        assertEquals("Test Item", dto1.getNameOfItem());
        assertEquals(100.0, dto1.getSoldPrice());
        assertEquals(110.0, dto1.getUserHighestBid());
        assertEquals(testAuction.getEndTime(), dto1.getDateOfAuction());
        assertEquals(true, dto1.isAuctionWon());

        TradebookDTO dto2 = tradebookDTOList.get(1);
        assertEquals("Test Item", dto2.getNameOfItem());
        assertEquals(100.0, dto2.getSoldPrice());
        assertEquals(90.0, dto2.getUserHighestBid());
        assertEquals(testAuction.getEndTime(), dto2.getDateOfAuction());
        assertEquals(false, dto2.isAuctionWon());

        verify(tradebookRepository, times(1)).findAllByUser(testUser);
    }

    @Test
    public void testGetAllTradesByUser_NoBids() throws ServiceException {
        when(tradebookRepository.findAllByUser(testUser)).thenReturn(Collections.emptyList());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            tradebookService.getAllTradesByUser(testUser);
        });
        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        assertEquals("No trades found for user", exception.getErrorMessage());
        verify(tradebookRepository, times(1)).findAllByUser(testUser);
    }

    @Test
    public void testGetAllTradesByUser_ThrowsServiceException() {
        RuntimeException runtimeException = new RuntimeException("Database error");
        when(tradebookRepository.findAllByUser(testUser)).thenThrow(runtimeException);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            tradebookService.getAllTradesByUser(testUser);
        });

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        assertEquals("Error fetching tradebook details", exception.getErrorMessage());
        verify(tradebookRepository, times(1)).findAllByUser(testUser);
    }
}
