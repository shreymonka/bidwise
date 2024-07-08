package com.online.auction.service.impl;

import com.online.auction.dto.ItemDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Auction;
import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
import com.online.auction.model.User;
import com.online.auction.repository.AuctionListingRepository;
import com.online.auction.repository.ItemCategoryRepository;
import com.online.auction.repository.ItemRepository;
import com.online.auction.service.ItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.online.auction.constant.AuctionConstants.EMPTY_ITEM_NAME;
import static com.online.auction.constant.AuctionConstants.ITEM_CATEGORY_NOT_FOUND;
import static com.online.auction.constant.AuctionConstants.NEGATIVE_BID_AMOUNT;

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final AuctionListingRepository auctionListingRepository;

    @Override
    public String addItem(ItemDTO itemDto, User user) throws ServiceException {
        log.debug("Attempting to add a new item: {}", itemDto);

        Optional<ItemCategory> itemCategory = itemCategoryRepository.findByItemCategoryName(itemDto.getCategoryName());
        if (itemCategory.isEmpty()) {
            log.warn("Item category '{}' is not present", itemDto.getCategoryName());
            throw new ServiceException(HttpStatus.BAD_REQUEST,ITEM_CATEGORY_NOT_FOUND );
        }

        if (itemDto.getMinBidAmount() < 0) {
            log.warn("Minimum bid amount is negative: {}", itemDto.getMinBidAmount());
            throw new ServiceException(HttpStatus.BAD_REQUEST,NEGATIVE_BID_AMOUNT );
        }

        if (itemDto.getItemName() == null || itemDto.getItemName().isEmpty()) {
            log.warn("Item name is missing in the provided item data: {}", itemDto);
            throw new ServiceException(HttpStatus.BAD_REQUEST,EMPTY_ITEM_NAME );
        }

        var item = Item.builder()
                .item_name(itemDto.getItemName())
                .item_maker(itemDto.getItemMaker())
                .description(itemDto.getDescription())
                .min_bid_amount(itemDto.getMinBidAmount())
                .price_paid(itemDto.getPricePaid())
                .currency(itemDto.getCurrency())
                .item_photo(itemDto.getItemPhoto())
                .item_condition(itemDto.getItemCondition())
                .itemcategory(itemCategory.get())
                .sellerId(user)
                .build();

        log.debug("Built item entity: {}", item);

        Item savedItem = itemRepository.save(item);
        log.info("Item saved successfully: {}", savedItem);

        var auction = Auction.builder()
                .startTime(itemDto.getStartTime())
                .endTime(itemDto.getEndTime())
                .items(savedItem)
                .isOpen(false)
                .sellerId(user)
                .build();

        log.debug("Built auction entity: {}", auction);

        Auction savedAuction = auctionListingRepository.save(auction);
        log.info("Auction saved successfully: {}", savedAuction);

        String successMessage = "Item listed successfully for Auction";
        log.info(successMessage);
        return successMessage;
    }
}
