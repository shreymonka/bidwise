package com.online.auction.service;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;

public interface MembershipService {
    String upgradeToPremium(String email) throws ServiceException;

    Boolean isPremium(User user) throws ServiceException;

    void cancelPremium(String email) throws ServiceException;

}
