package com.napier.sem.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CountryTest {
    @Test
    void testCountryConstructorAndGetters() {
        //Instantiate a new Country object with sample test data
        Country country = new Country(
                "MMR",
                "Myanmar",
                "Asia",
                "Southeast Asia",
                54409800,
                "Naypyidaw"
        );

        // Verify that each getter returns the exact value assigned in the constructor
        assertEquals("MMR", country.getCode());
        assertEquals("Myanmar", country.getName());
        assertEquals("Asia", country.getContinent());
        assertEquals("Southeast Asia", country.getRegion());
        assertEquals(54409800, country.getPopulation());
        assertEquals("Naypyidaw", country.getCapital());
    }
}