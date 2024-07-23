package com.online.auction.service.impl;

import com.online.auction.exception.ServiceException;
import com.online.auction.repository.CityRepository;
import com.online.auction.repository.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class LocaleServiceImplTest {

    @Mock
    private CityRepository cityRepository;

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private LocaleServiceImpl localeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCountriesTest() {

    }

    @Test
    void getCitiesForCountrySuccessTest() throws ServiceException {

    }

    @Test
    void getCitiesForCountryWhenNoCitiesFoundTest() {

    }

    @Test
    void getCitiesForCountryWhenCitiesIsNullTest() {

    }
}