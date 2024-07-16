package com.online.auction.service.impl;


import com.online.auction.exception.ServiceException;
import com.online.auction.repository.AuctionListingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    }

    @Test
    void getAuctionDetailsWhenAuctionNotPresentTest() {

    }
}