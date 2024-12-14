package org.ahroo.nexus.repository;

import org.ahroo.nexus.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Integer> {
}
