package com.online.auction.repository;

import com.online.auction.model.AuctionBidDetails;
import com.online.auction.model.Item;
import com.online.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TradebookRepository extends JpaRepository<AuctionBidDetails,Integer> {
    @Query("SELECT abd FROM AuctionBidDetails abd WHERE abd.bidderId = :user")
    List<AuctionBidDetails> findAllByUser(User user);

    @Query("SELECT abd FROM AuctionBidDetails abd WHERE abd.auctionId.auctionId = :auctionId AND abd.isWon = true")
    Optional<AuctionBidDetails> findByAuctionIdAndIsWonTrue(int auctionId);

}
