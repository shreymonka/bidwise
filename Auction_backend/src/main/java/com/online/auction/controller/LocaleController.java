package com.online.auction.controller;

import com.online.auction.dto.SuccessResponse;
import com.online.auction.exception.ServiceException;
import com.online.auction.model.City;
import com.online.auction.model.Country;
import com.online.auction.service.LocaleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.online.auction.constant.AuctionConstants.API_VERSION_V1;
import static com.online.auction.constant.AuctionConstants.CITIES;
import static com.online.auction.constant.AuctionConstants.COUNTRIES;
import static com.online.auction.constant.AuctionConstants.LOCALE;

@RestController
@AllArgsConstructor
@RequestMapping(API_VERSION_V1 + LOCALE)
public class LocaleController {
    private final LocaleService localeService;

    @GetMapping(COUNTRIES)
    public ResponseEntity<SuccessResponse<List<String>>> getAllCities() throws ServiceException {
        List<Country> countries = localeService.getAllCountries();
        SuccessResponse<List<String>> successResponse = new SuccessResponse<>(200, HttpStatus.OK, countries.stream().map(Country::getCountryName).collect(Collectors.toList()));
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping(CITIES)
    public ResponseEntity<SuccessResponse<List<String>>> getCitiesByCountry(
            @RequestParam("countryName") String countryName) throws ServiceException {
        List<City> cities = localeService.getCitiesForCountry(countryName);
        SuccessResponse<List<String>> successResponse = new SuccessResponse<>(200, HttpStatus.OK, cities.stream().map(City::getCityName).collect(Collectors.toList()));
        return ResponseEntity.ok(successResponse);
    }

}
