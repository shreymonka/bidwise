package com.online.auction.service;

import com.online.auction.dto.BidStatsDTO;
import com.online.auction.dto.UserProfileDTO;
import com.online.auction.exception.ServiceException;

import java.util.List;

public interface ProfileService {
    UserProfileDTO getUserProfile(Integer userId) throws ServiceException;
    long countUserParticipatedAuctions(Integer userId) throws ServiceException;
}
