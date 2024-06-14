package com.online.auction.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

@Entity
@Table(name = "auction_bid_detail")
public class AuctionBidDetails {
    @Id
    @ManyToOne
    @JoinColumn(name = "auction_id", nullable = false)
    private Auction auctionId;

    @Id
    @ManyToOne
    @JoinColumn(name = "bidder_id", nullable = false)
    private User bidderId;

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item itemId;

    private double bid_amount;

    @Column(name = "bidTime", columnDefinition = "DATETIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bidTime;

    private boolean isWon;
}
