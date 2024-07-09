package com.online.auction.repository;

import com.online.auction.model.Auction;
import com.online.auction.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuctionListingRepository extends JpaRepository<Auction,Integer> {

}
