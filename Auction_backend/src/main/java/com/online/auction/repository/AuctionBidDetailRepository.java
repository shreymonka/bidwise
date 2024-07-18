package com.online.auction.repository;

import com.online.auction.model.AuctionBidDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface AuctionBidDetailRepository extends JpaRepository<AuctionBidDetails, Integer> {
    @Query(value = "SELECT * FROM auction_bid_detail WHERE item_id = :itemId ORDER BY bid_amount DESC LIMIT 1", nativeQuery = true)
    AuctionBidDetails findTopByItemIdOrderByBidAmountDesc(@Param("itemId") int itemId);
}
