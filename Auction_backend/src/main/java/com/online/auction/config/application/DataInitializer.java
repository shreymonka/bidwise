package com.online.auction.config.application;

import com.online.auction.model.City;
import com.online.auction.model.Country;
import com.online.auction.repository.CityRepository;
import com.online.auction.repository.CountryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@AllArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final LocaleConfig localeConfig;
    private CountryRepository countryRepository;
    private CityRepository cityRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing the initial Locale Data");

        //Fetching the Locale details from configuration
        Map<String, List<String>> locale = localeConfig.getLocale();
        for (Map.Entry<String, List<String>> entry : locale.entrySet()) {
            String countryName = entry.getKey();

            //Checking if the Country with the name is present
            Optional<Country> countryOptional = countryRepository.findByCountryName(countryName);
            List<String> cities = entry.getValue();
            if (countryOptional.isEmpty()) {

                //Saving the new Country in the database
                log.info("Inserting the country with name : {}", countryName);
                Country country = new Country();
                country.setCountryName(countryName);
                Country countryDb = countryRepository.save(country);
                log.info("Saved the country record : {}", countryDb);
                for (String cityName : cities) {
                    //checking if the city details are already present
                    Optional<City> cityOptional = cityRepository.findByCityName(cityName);
                    if (cityOptional.isEmpty()) {
                        //Inserting the new city details in the database
                        log.info("Inserting the city with the name : {}", cityName);
                        City city = new City();
                        city.setCityName(cityName);
                        city.setCountry(countryDb);
                        City cityDb = cityRepository.save(city);
                        log.info("Saved the city record : {}", cityDb);
                    } else {
                        log.info("The data is already present for city : {}", cityOptional.get());
                    }
                }
            } else {
                //Inserting the cities for the already existing country
                log.info("The data is already present for the country : {}", countryOptional.get());
                for (String cityName : cities) {
                    Optional<City> cityOptional = cityRepository.findByCityName(cityName);
                    if (cityOptional.isEmpty()) {
                        log.info("Inserting the city with the name : {}", cityName);
                        City city = new City();
                        city.setCityName(cityName);
                        city.setCountry(countryOptional.get());
                        City cityDb = cityRepository.save(city);
                        log.info("Saved the city record : {}", cityDb);
                    } else {
                        log.info("The data is already present for city : {}", cityOptional.get());
                    }
                }
            }
        }
    }
}
