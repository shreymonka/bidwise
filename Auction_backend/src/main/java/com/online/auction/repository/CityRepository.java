package com.online.auction.repository;

import com.online.auction.model.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
    Optional<City> findByCityName(String cityName);
}
