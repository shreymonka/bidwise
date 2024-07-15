package com.online.auction.repository;

import com.online.auction.model.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuctionListingRepository extends JpaRepository<Auction, Integer> {
    Optional<Auction> findByItems_ItemId(int itemId);
}
