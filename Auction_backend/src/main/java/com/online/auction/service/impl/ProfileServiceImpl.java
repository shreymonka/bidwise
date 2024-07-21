package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.repository.ProfileRepository;
import com.online.auction.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    @Override
    public User getUserDetails(Integer userId) throws ServiceException {
        User user = profileRepository.findByUserId(userId);
        if (user == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "User not found for user id: " + userId);
        }
        return user;
    }
}
