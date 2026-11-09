package com.napier.sem.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CapitalCityTest {
    /**
     * Tests that the constructor initializes all fields correctly
     * and that each getter returns the expected value.
     */
    @Test
    void testCapitalCityConstructorAndGetters() {
        CapitalCity capital = new CapitalCity(
                "Naypyidaw",
                "Myanmar",
                924608);

        assertEquals("Naypyidaw", capital.getName());
        assertEquals("Myanmar", capital.getCountry());
        assertEquals(924608, capital.getPopulation());

        System.out.println("CapitalCity constructor and getters Passed");
    }
}