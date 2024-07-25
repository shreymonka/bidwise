package com.online.auction.repository;

import com.online.auction.model.Item;
import com.online.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Integer> {
     List<Item> findBySellerId(User sellerId);
     Optional<Item> findById(Integer id);

    @Query("SELECT i FROM Item i JOIN Auction a ON i.itemId = a.items.itemId WHERE (a.startTime > :currentTime OR (a.isOpen = true AND a.endTime > :currentTime)) AND i.sellerId != :seller")
    List<Item> findUpcomingAndCurrentItemsExcludingUserItems(LocalDateTime currentTime, User seller);

     List<Item> findByItemId(Integer itemId);
}
