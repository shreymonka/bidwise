package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.repository.UserRepository;
import com.online.auction.service.UpgradeToPremiumService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UpgradeToPremiumServiceImpl implements UpgradeToPremiumService {
    private final UserRepository userRepository;

    /**
     * Upgrades a user to premium status.
     *
     * @param email the user email to upgrade
     * @return the email of the upgraded user
     * @throws ServiceException if the user is not found
     */
    public String upgradeToPremium(String email) throws ServiceException {
        log.info("Upgrading user to premium");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, "User not found"));
        user.setPremium(true);
        userRepository.save(user);

        return email;
    }
}
