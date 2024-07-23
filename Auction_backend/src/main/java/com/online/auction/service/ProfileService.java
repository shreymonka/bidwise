package com.online.auction.service;

import com.online.auction.dto.BidStatsDTO;
import com.online.auction.dto.CategoryBidStatsDTO;
import com.online.auction.dto.UserProfileDTO;
import com.online.auction.exception.ServiceException;

import java.util.List;

public interface ProfileService {
    UserProfileDTO getUserProfile(Integer userId) throws ServiceException;
    long countUserParticipatedAuctions(Integer userId) throws ServiceException;
    List<BidStatsDTO> getBidStats(Integer userId) throws ServiceException;
    List<CategoryBidStatsDTO> getCategoryBidStats(Integer userId) throws ServiceException;

}
