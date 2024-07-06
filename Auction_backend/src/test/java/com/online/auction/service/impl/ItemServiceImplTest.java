package com.online.auction.service.impl;

import com.online.auction.dto.ItemDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
import com.online.auction.model.ItemCondition;
import com.online.auction.model.User;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.repository.ItemCategoryRepository;
import com.online.auction.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Optional;

import static com.online.auction.constant.TestConstants.END_TIME;
import static com.online.auction.constant.TestConstants.INTEGER_ONE;
import static com.online.auction.constant.TestConstants.PASSWORD;
import static com.online.auction.constant.TestConstants.START_TIME;
import static com.online.auction.constant.TestConstants.TEST_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private ItemCategoryRepository itemCategoryRepository;

    @Mock
    private AuctionListingRepository auctionListingRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private ItemDTO itemDto;
    private User user;

    private ItemCondition itemCondition;
    @BeforeEach
    void setUp() {
        // Setup a sample ItemDTO
        itemDto = new ItemDTO();
        itemDto.setItemName("Antique Vase");
        itemDto.setItemMaker("Unknown");
        itemDto.setDescription("A beautiful antique vase.");
        itemDto.setMinBidAmount(100);
        itemDto.setPricePaid(50);
        itemDto.setCurrency("USD");
        itemDto.setItemPhoto("photo_url");
        itemDto.setItemCondition(itemCondition);
        itemDto.setCategoryName("Paintings");
        itemDto.setStartTime(START_TIME);
        itemDto.setEndTime(END_TIME);

        // Setup a sample User
        user = new User();
        user.setUserId(INTEGER_ONE);
        user.setEmail(TEST_EMAIL);
        user.setPassword(PASSWORD);
    }

    @Test
    void addItemSuccessfulTest() throws ServiceException {
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryId(INTEGER_ONE);
        itemCategory.setItemCategoryName("Paintings");
        when(itemCategoryRepository.findByItemCategoryName(anyString())).thenReturn(Optional.of(itemCategory));
        when(itemRepository.save(any(Item.class))).thenReturn(new Item());
        when(auctionListingRepository.save(any(Auction.class))).thenReturn(new Auction());

        String response = itemService.addItem(itemDto, user);

        assertNotNull(response);
        assertEquals("Item listed successfully for Auction", response);

        verify(itemCategoryRepository).findByItemCategoryName(itemDto.getCategoryName());
        verify(itemRepository).save(any(Item.class));
        verify(auctionListingRepository).save(any(Auction.class));
    }

    @Test
    void addItemFailureDueToMissingCategoryTest() {
        when(itemCategoryRepository.findByItemCategoryName(anyString())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> itemService.addItem(itemDto, user));

        assertNotNull(exception);
        assertEquals(400, exception.getStatusCode());
        assertEquals("Item category is not present", exception.getMessage());

        verify(itemCategoryRepository).findByItemCategoryName(itemDto.getCategoryName());
        verify(itemRepository, never()).save(any(Item.class));
        verify(auctionListingRepository, never()).save(any(Auction.class));
    }

    @Test
    void addItemWithInvalidDataTest() {
        // Here we will create various invalid scenarios for itemDto and test each

        // Test with null Item Name
        itemDto.setItemName(null);
        ServiceException exception = assertThrows(ServiceException.class, () -> itemService.addItem(itemDto, user));
        assertEquals(400, exception.getStatusCode());
        assertEquals("Item name is required", exception.getMessage());
        itemDto.setItemName("Antique Vase");
        itemDto.setMinBidAmount(-10);
        exception = assertThrows(ServiceException.class, () -> itemService.addItem(itemDto, user));
        assertEquals(400, exception.getStatusCode());
        assertEquals("Minimum bid amount must be positive", exception.getMessage());
        verify(itemCategoryRepository, never()).findByItemCategoryName(anyString());
        verify(itemRepository, never()).save(any(Item.class));
        verify(auctionListingRepository, never()).save(any(Auction.class));
    }
}