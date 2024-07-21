package com.online.auction.repository;

import com.online.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<User, Integer> {
    User findByUserId(Integer userId);
}
