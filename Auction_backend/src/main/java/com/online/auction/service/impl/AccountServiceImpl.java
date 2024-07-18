package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.Account;
import com.online.auction.repository.AccountRepository;
import com.online.auction.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public float getAccountBalance(Integer userId) throws ServiceException {
        Account account = accountRepository.findByUserId(userId);
        if (account == null) {
            throw new ServiceException(HttpStatus.NOT_FOUND, "Account not found for user id: " + userId);
        }
        return account.getFunds();
    }
}
