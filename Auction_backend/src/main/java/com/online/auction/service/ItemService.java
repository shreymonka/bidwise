package com.online.auction.service;

import com.online.auction.dto.ItemDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;

public interface ItemService {
    String addItem(ItemDTO userDto, User user) throws ServiceException;

}
