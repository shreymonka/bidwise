package com.online.auction.controller;

import com.online.auction.dto.PaymentDetailsDTO;
import com.online.auction.dto.SuccessResponse;
import com.online.auction.exception.ServiceException;
import com.online.auction.service.UpgradeToPremiumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpgradeToPremiumControllerTest {

    @Mock
    private UpgradeToPremiumService upgradeToPremiumService;

    @InjectMocks
    private UpgradeToPremiumController upgradeToPremiumController;

    private PaymentDetailsDTO paymentDetailsDTO;

    @BeforeEach
    public void setUp() {
        paymentDetailsDTO = new PaymentDetailsDTO("test@example.com","4111111111111111","12/25","123","SM");
    }

    @Test
    public void testUpgradeToPremium_Success() throws ServiceException {
        when(upgradeToPremiumService.upgradeToPremium(anyString())).thenReturn("test@example.com");

        ResponseEntity<SuccessResponse<String>> response = upgradeToPremiumController.upgradeToPremium(paymentDetailsDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User upgraded to premium successfully", response.getBody().getData());
        verify(upgradeToPremiumService, times(1)).upgradeToPremium("test@example.com");
    }

    @Test
    public void testUpgradeToPremium_UserNotFound() throws ServiceException {
        when(upgradeToPremiumService.upgradeToPremium(anyString())).thenThrow(new ServiceException(HttpStatus.BAD_REQUEST, "User not found"));

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            upgradeToPremiumController.upgradeToPremium(paymentDetailsDTO);
        });

        assertEquals("User not found", exception.getMessage());
        verify(upgradeToPremiumService, times(1)).upgradeToPremium("test@example.com");
    }
}
