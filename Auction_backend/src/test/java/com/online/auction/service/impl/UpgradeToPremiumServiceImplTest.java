package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.User;
import com.online.auction.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UpgradeToPremiumServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UpgradeToPremiumServiceImpl upgradeToPremiumService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPremium(false);
    }

    @Test
    public void testUpgradeToPremium_Success() throws ServiceException {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        String result = upgradeToPremiumService.upgradeToPremium("test@example.com");

        assertEquals("test@example.com", result);
        assertEquals(true, user.isPremium());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpgradeToPremium_UserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            upgradeToPremiumService.upgradeToPremium("test@example.com");
        });

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(userRepository, times(0)).save(any(User.class));
    }
}
