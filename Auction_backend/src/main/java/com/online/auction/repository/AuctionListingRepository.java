package com.online.auction.repository;

import com.online.auction.model.Auction;
import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AuctionListingRepository extends JpaRepository<Auction, Integer> {
    Optional<Auction> findByItems(Item items);
    @Transactional
    void deleteByItems(Item item);
    Optional<Auction> findByItems_ItemId(int itemId);
    @Query("SELECT a FROM Auction a WHERE (a.startTime > :currentTime OR (a.isOpen = true AND a.endTime > :currentTime))")
    List<Auction> findUpcomingAndCurrentAuctions(LocalDateTime currentTime);

}