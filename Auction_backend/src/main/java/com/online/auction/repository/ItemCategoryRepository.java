package com.online.auction.repository;

import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCategoryRepository extends JpaRepository<ItemCategory,Integer> {
    Optional<ItemCategory> findByItemCategoryName(String name);
}
