package com.online.auction.repository;

import com.online.auction.dto.BidStatsDTO;
import com.online.auction.dto.CategoryBidStatsDTO;
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

    @Query("SELECT new com.online.auction.dto.BidStatsDTO(FUNCTION('MONTH', abd.bidTime), " +
            "SUM(CASE WHEN abd.isWon = true THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN abd.isWon = false THEN 1 ELSE 0 END)) " +
            "FROM AuctionBidDetails abd " +
            "WHERE abd.bidderId.userId = :userId " +
            "AND FUNCTION('YEAR', abd.bidTime) = FUNCTION('YEAR', CURRENT_DATE) " +
            "GROUP BY FUNCTION('MONTH', abd.bidTime)")
    List<BidStatsDTO> findBidStatsByUserIdForCurrentYear(Integer userId);

    @Query("SELECT new com.online.auction.dto.CategoryBidStatsDTO(ic.itemCategoryName, COALESCE(COUNT(abd.auction_bid_details_id), 0)) " +
            "FROM ItemCategory ic " +
            "LEFT JOIN Item i ON ic.itemCategoryId = i.itemcategory.itemCategoryId " +
            "LEFT JOIN AuctionBidDetails abd ON i.itemId = abd.itemId.itemId AND abd.bidderId.userId = :userId " +
            "GROUP BY ic.itemCategoryName")
    List<CategoryBidStatsDTO> findCategoryBidStatsByUserId(Integer userId);
}
