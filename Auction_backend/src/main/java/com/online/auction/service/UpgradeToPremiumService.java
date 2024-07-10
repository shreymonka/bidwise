package com.online.auction.service;

import com.online.auction.exception.ServiceException;

public interface UpgradeToPremiumService {
    String upgradeToPremium(String email) throws ServiceException;

}
