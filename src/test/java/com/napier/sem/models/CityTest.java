
package com.napier.sem.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the City model class.
 */
class CityTest {

    /**
     * Tests that the City constructor correctly initializes
     * all fields and that each getter returns the expected value.
     */
    @Test
    void testCityConstructorAndGetters() {

        // Instantiate a new City object with sample test data
        City city = new City(
                "Yangon",
                "Myanmar",
                "Yangon",
                5209541
        );

        // Verify that each getter returns the exact value assigned in the constructor
        assertEquals("Yangon", city.getName());
        assertEquals("Myanmar", city.getCountry());
        assertEquals("Yangon", city.getDistrict());
        assertEquals(5209541, city.getPopulation());

        // Display the city information in a table format
        System.out.println("\n========== CITY TEST RESULTS ==========");
        System.out.println("+------------+----------------+");
        System.out.println("| Field      | Value          |");
        System.out.println("+------------+----------------+");
        System.out.printf("| %-10s | %-14s |%n", "Name", city.getName());
        System.out.printf("| %-10s | %-14s |%n", "Country", city.getCountry());
        System.out.printf("| %-10s | %-14s |%n", "District", city.getDistrict());
        System.out.printf("| %-10s | %-14d |%n", "Population", city.getPopulation());
        System.out.println("+------------+----------------+");
        System.out.println("City test passed successfully!");
    }
}
