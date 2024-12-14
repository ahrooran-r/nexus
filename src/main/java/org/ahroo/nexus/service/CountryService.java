package org.ahroo.nexus.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.ahroo.nexus.entity.Country;
import org.ahroo.nexus.repository.CountryRepository;
import org.jetbrains.annotations.UnmodifiableView;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CountryService {

    @UnmodifiableView
    private Map<String, Country> countries;

    private final CountryRepository countryRepository;

    @PostConstruct
    public void init() {
        Map<String, Country> countries = new HashMap<>();
        countryRepository.findAll().forEach(country -> countries.put(country.getName(), country));
        this.countries = Collections.unmodifiableMap(countries);
    }

    public boolean isValid(String country) {
        return countries.containsKey(country);
    }

    public Country findCountryByName(String country) {
        return countries.get(country);
    }
}
