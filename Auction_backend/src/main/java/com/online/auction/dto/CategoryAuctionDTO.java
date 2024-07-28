package com.online.auction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryAuctionDTO {
    private String categoryName;
    private List<AuctionItemDetailsDTO> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AuctionItemDetailsDTO {
        private String itemId;
        private String itemName;
        private String itemPhoto;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String cityName;
    }
}
