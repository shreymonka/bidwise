package com.online.auction.repository;

import com.online.auction.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Account findByUserId(Integer userId);
}
