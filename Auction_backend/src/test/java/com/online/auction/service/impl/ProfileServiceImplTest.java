package com.online.auction.service.impl;

import com.online.auction.dto.BidStatsDTO;
import com.online.auction.dto.CategoryBidStatsDTO;
import com.online.auction.dto.UserProfileDTO;
import com.online.auction.exception.ServiceException;
import com.online.auction.repository.ProfileRepository;
import jakarta.persistence.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUserProfileUserExistsTest() throws ServiceException {
        UserProfileDTO userProfile = new UserProfileDTO();
        when(profileRepository.findUserProfileByUserId(1)).thenReturn(userProfile);

        UserProfileDTO result = profileService.getUserProfile(1);

        assertNotNull(result);
        verify(profileRepository, times(1)).findUserProfileByUserId(1);
    }

    @Test
    void getUserProfileUserNotFoundTest() {
        when(profileRepository.findUserProfileByUserId(1)).thenReturn(null);

        ServiceException exception = assertThrows(ServiceException.class, () -> profileService.getUserProfile(1));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        verify(profileRepository, times(1)).findUserProfileByUserId(1);
    }

    @Test
    void countUserParticipatedAuctionsAuctionsFoundTest() throws ServiceException {
        when(profileRepository.countUserParticipatedAuctions(1)).thenReturn(5L);

        long result = profileService.countUserParticipatedAuctions(1);

        assertEquals(5L, result);
        verify(profileRepository, times(1)).countUserParticipatedAuctions(1);
    }


    @Test
    void countUserParticipatedAuctionsNoAuctionsFoundTest() throws ServiceException {
        when(profileRepository.countUserParticipatedAuctions(1)).thenReturn(0L);

        long result = profileService.countUserParticipatedAuctions(1);

        assertEquals(0L, result);
        verify(profileRepository, times(1)).countUserParticipatedAuctions(1);
    }

    @Test
    void countUserParticipatedAuctionsNegativeCountTest() {
        when(profileRepository.countUserParticipatedAuctions(1)).thenReturn(-1L);

        ServiceException exception = assertThrows(ServiceException.class, () -> profileService.countUserParticipatedAuctions(1));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        verify(profileRepository, times(1)).countUserParticipatedAuctions(1);
    }

    @Test
    void getBidStatsStatsFoundTest() throws ServiceException {
        List<BidStatsDTO> bidStats = new ArrayList<>();
        bidStats.add(new BidStatsDTO(1, 2, 3));
        when(profileRepository.findBidStatsByUserIdForCurrentYear(1)).thenReturn(bidStats);

        List<BidStatsDTO> result = profileService.getBidStats(1);

        assertEquals(12, result.size());
        assertEquals(2, result.get(0).getWonBids());
        assertEquals(3, result.get(0).getLostBids());
        verify(profileRepository, times(1)).findBidStatsByUserIdForCurrentYear(1);
    }

    @Test
    void getCategoryBidStatsStatsFoundTest() throws ServiceException {
        List<CategoryBidStatsDTO> categoryBidStats = new ArrayList<>();
        categoryBidStats.add(new CategoryBidStatsDTO("Electronics", 10));
        when(profileRepository.findCategoryBidStatsByUserId(1)).thenReturn(categoryBidStats);

        List<CategoryBidStatsDTO> result = profileService.getCategoryBidStats(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategoryName());
        assertEquals(10, result.get(0).getBidCount());
        verify(profileRepository, times(1)).findCategoryBidStatsByUserId(1);
    }

    @Test
    void getCategoryBidStatsNoStatsFoundTest() {
        when(profileRepository.findCategoryBidStatsByUserId(1)).thenReturn(new ArrayList<>());

        ServiceException exception = assertThrows(ServiceException.class, () -> profileService.getCategoryBidStats(1));

        assertEquals(HttpStatus.NOT_FOUND.value(), exception.getStatusCode());
        verify(profileRepository, times(1)).findCategoryBidStatsByUserId(1);
    }

}
