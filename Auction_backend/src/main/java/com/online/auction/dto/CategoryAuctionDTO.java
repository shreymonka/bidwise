package com.online.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing categorized auction data.
 * <p>
 * This DTO includes a category name and a list of auction item details for that category.
 * </p>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAuctionDTO {
    private String categoryName;
    private List<AuctionItemDetailsDTO> items;

    /**
     * Nested DTO representing the details of an auction item.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuctionItemDetailsDTO {
        private String auctionId;
        private String itemId;
        private String itemName;
        private String itemPhoto;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String cityName;
        private boolean isOpen;
    }
}
