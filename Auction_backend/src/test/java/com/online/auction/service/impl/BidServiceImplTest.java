package com.online.auction.service.impl;


import com.online.auction.repository.AuctionBidDetailRepository;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.repository.ItemRepository;
import com.online.auction.repository.UserRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BidServiceImplTest {

    @Mock
    private AuctionBidDetailRepository auctionBidDetailRepository;

    @Mock
    private AuctionListingRepository auctionListingRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BidServiceImpl bidService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @SneakyThrows
    void processBidSuccessTest() {

    }

    @Test
    void processBid_auctionNotFound() {

    }

    @Test
    void processBidItemNotFoundTest() {

    }

    @Test
    void processBid_userNotFound() {

    }
}