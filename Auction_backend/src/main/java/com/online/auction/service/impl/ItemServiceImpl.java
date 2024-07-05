package com.online.auction.service.impl;

import com.online.auction.dto.ItemDTO;
import com.online.auction.dto.UserDTO;
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

@Service
@AllArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemRepository;
    private ItemCategoryRepository itemCategoryRepository;
    private AuctionListingRepository auctionListingRepository;

    @Override
    public String addItem(ItemDTO itemDto, User user) throws ServiceException {
        Optional<ItemCategory> itemCategory = itemCategoryRepository.findByItemCategoryName(itemDto.getCategoryName());
        if (itemCategory.isEmpty()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Item requested is not present");
        }

        var item = com.online.auction.model.Item.builder()
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
        Item savedItem = itemRepository.save(item);

        var auction = com.online.auction.model.Auction.builder()
                .startTime(itemDto.getStartTime())
                .endTime(itemDto.getEndTime())
                .items(savedItem)
                .isOpen(false)
                .sellerId(user)
                .build();
        Auction savedAuction = auctionListingRepository.save(auction);

        return "";
    }
}
