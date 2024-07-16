package com.online.auction.service;

import com.online.auction.dto.ItemDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.Item;
import com.online.auction.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    String addItem(ItemDTO userDto, MultipartFile file, User user) throws ServiceException;
    List<ItemDTO> getAllItemsByUser(User user) throws ServiceException;
}
