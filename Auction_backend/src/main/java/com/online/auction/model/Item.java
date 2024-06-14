package com.online.auction.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemId;

    @ManyToOne
    @JoinColumn(name = "item_category_id", referencedColumnName = "itemCategoryId")
    private ItemCategory itemcategory;

    private String description;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User sellerId;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private User buyerId;

    private double min_bid_amount;

    @Column(nullable = true)
    private double selling_amount;

    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auction;
}
