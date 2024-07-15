package com.online.auction.repository;

import com.online.auction.model.AuctionBidDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionBidDetailRepository extends JpaRepository<AuctionBidDetails, Integer> {
}
