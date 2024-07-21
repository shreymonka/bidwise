package com.online.auction.service;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;

public interface ProfileService {
    User getUserDetails(Integer userId) throws ServiceException;
}
