package com.online.auction.service;

import com.online.auction.dto.ItemDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import org.springframework.web.multipart.MultipartFile;

public interface ItemService {
    String addItem(ItemDTO userDto, MultipartFile file, User user) throws ServiceException;

}
