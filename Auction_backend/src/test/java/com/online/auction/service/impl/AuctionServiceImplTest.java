package com.online.auction.service.impl;


import com.online.auction.dto.AuctionDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.model.Item;
import com.online.auction.model.User;
import com.online.auction.repository.AuctionListingRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static com.online.auction.constant.AuctionConstants.AUCTION_NOT_FOUND_MSG;
import static com.online.auction.constant.TestConstants.INTEGER_ONE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AuctionServiceImplTest {
    @Mock
    private AuctionListingRepository auctionListingRepository;

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

    }

    @Test
    public void testProcessPostAuctionStateNoBidDetails() {

    }
}