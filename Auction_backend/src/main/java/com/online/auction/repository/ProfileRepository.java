package com.online.auction.repository;

import com.online.auction.dto.BidStatsDTO;
import com.online.auction.dto.UserProfileDTO;
import com.online.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProfileRepository extends JpaRepository<User, Integer> {
    @Query("SELECT new com.online.auction.dto.UserProfileDTO(u.userId, u.firstName, u.lastName, u.email, c.cityName, co.countryName) " +
            "FROM User u " +
            "JOIN u.city c " +
            "JOIN c.country co " +
            "WHERE u.userId = :userId")
    UserProfileDTO findUserProfileByUserId(Integer userId);

    @Query("SELECT COUNT(DISTINCT abd.auctionId) " +
            "FROM AuctionBidDetails abd " +
            "WHERE abd.bidderId.userId = :userId")
    long countUserParticipatedAuctions(Integer userId);
}
