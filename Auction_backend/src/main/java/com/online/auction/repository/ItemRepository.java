package com.online.auction.repository;

import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
import com.online.auction.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Integer> {
     List<Item> findBySellerId(User sellerId);
     Optional<Item> findById(Integer id);
     List<Item> findByItemId(Integer itemId);
}
