package com.online.auction.config.application;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@ConfigurationProperties(prefix = "app")
public class LocaleConfig {
    private String locale;

    public Map<String, List<String>> getLocale() {
        return Arrays.stream(locale.split(";"))
                .map(entry -> entry.split("="))
                .collect(Collectors.toMap(
                        entry -> entry[0].trim(),
                        entry -> Arrays.stream(entry[1].split(",")).map(String::trim).collect(Collectors.toList())
                ));
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
