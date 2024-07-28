package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.repository.UserRepository;
import com.online.auction.service.AccountService;
import com.online.auction.service.MembershipService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.online.auction.constant.AuctionConstants.USER_NOT_PRESENT_MSG;

/**
 * Service implementation for handling the membership related operations.
 */
@Service
@AllArgsConstructor
@Slf4j
public class MembershipServiceImpl implements MembershipService {
    private final UserRepository userRepository;
    private final AccountService accountService;

    /**
     * Upgrades a user to premium status.
     *
     * @param email the user email to upgrade
     * @return the email of the upgraded user
     * @throws ServiceException if the user is not found
     */
    public String upgradeToPremium(String email) throws ServiceException {
        log.info("Upgrading user to premium");
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST, USER_NOT_PRESENT_MSG));
        user.setPremium(true);
        userRepository.save(user);

        accountService.addFunds(user.getUserId(), 100);

        return email;
    }
}
