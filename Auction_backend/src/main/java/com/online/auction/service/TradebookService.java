package com.online.auction.service;

import com.online.auction.dto.ItemDTO;
import com.online.auction.dto.TradebookDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;

import java.util.List;

public interface TradebookService {
    List<TradebookDTO> getAllTradesByUser(User user) throws ServiceException;

}
