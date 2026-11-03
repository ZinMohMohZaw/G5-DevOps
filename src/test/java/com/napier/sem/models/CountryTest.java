package com.napier.sem.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CountryTest {
    @Test
    public void testCountryCreationAndGetters() {
        Country country = new Country(
                "MMR",
                "Myanmar",
                "Asia",
                "Southeast Asia",
                54409800,
                "Naypyidaw"
        );

        assertEquals("MMR", country.getCode());
        assertEquals("Myanmar", country.getName());
        assertEquals("Asia", country.getContinent());
        assertEquals("Southeast Asia", country.getRegion());
        assertEquals(54409800, country.getPopulation());
        assertEquals("Naypyidaw", country.getCapital());
    }
}