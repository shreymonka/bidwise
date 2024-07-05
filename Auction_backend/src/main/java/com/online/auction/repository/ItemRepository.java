package com.online.auction.repository;

import com.online.auction.model.Item;
import com.online.auction.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Integer> {
    Optional<Item> findAllByItemcategory_ItemCategoryName(String categoryName);
}
