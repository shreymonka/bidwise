package com.online.auction.service;

import com.online.auction.dto.UserProfileDTO;
import com.online.auction.exception.ServiceException;

public interface ProfileService {
    UserProfileDTO getUserProfile(Integer userId) throws ServiceException;
}
