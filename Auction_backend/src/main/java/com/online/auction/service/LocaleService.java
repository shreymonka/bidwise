package com.online.auction.service;

import com.online.auction.exception.ServiceException;
import com.online.auction.model.City;
import com.online.auction.model.Country;

import java.util.List;

public interface LocaleService {
    List<Country> getAllCountries();

    List<City> getCitiesForCountry(String countryName) throws ServiceException;
}
