package com.online.auction.repository;

import com.online.auction.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByCityName(String cityName);

    @Query("SELECT c FROM City c WHERE c.country.countryName = :countryName")
    List<City> findAllByCountryName(@Param("countryName") String countryName);
}
