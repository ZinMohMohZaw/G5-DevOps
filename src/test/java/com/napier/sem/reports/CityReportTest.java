package com.napier.sem.reports;

import com.napier.sem.models.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityReportTest {

    // CityReport object used to test city report methods.
    private CityReport cityReport;

    // Sample city data used for testing report generation.
    private List<City> cities;

    // Runs before each test to initialize the report object and sample data.
    @BeforeEach
    void setUp() {
        cityReport = new CityReport(null);

        cities = new ArrayList<>();
        cities.add(new City("Bangkok", "Thailand", "Bangkok", 10500000));
        cities.add(new City("Chiang Mai", "Thailand", "Chiang Mai", 127000));
    }

    // US07: All Cities Sorted by Population

    // Tests that retrieving all cities throws an exception when the database connection is null.
    @Test
    void testGetAllCitiesSortedByPopulation() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getAllCitiesSortedByPopulation());
    }

    // Tests that a city report can be generated without throwing an exception.
    @Test
    void testGenerateCityReport() {
        assertDoesNotThrow(() -> cityReport.generateCityReport(cities));
    }

    // US08: Cities by Continent

    // Tests retrieving cities by continent when the database connection is null.
    @Test
    void testGetCitiesByContinent() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByContinent("Asia"));
    }

    // Tests that the continent city report handles the given continent.
    @Test
    void testGenerateContinentCityReport() {
        assertThrows(NullPointerException.class,
                () -> cityReport.generateContinentCityReport("Asia"));
    }

    // US09: Cities by Region

    // Tests retrieving cities by region when the database connection is null.
    @Test
    void testGetCitiesByRegion() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByRegion("Southeast Asia"));
    }

    // Tests that a city report for a specific region can be printed successfully.
    @Test
    void testPrintCitiesByRegionReport() {
        assertDoesNotThrow(() ->
                cityReport.printCitiesByRegionReport(cities, "Southeast Asia"));
    }

    // US10: Cities by Country

    // Tests retrieving cities by country when the database connection is null.
    @Test
    void testGetCitiesByCountry() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByCountry("Thailand"));
    }

    // Tests that a city report for a specific country can be printed successfully.
    @Test
    void testPrintCitiesByCountryReport() {
        assertDoesNotThrow(() ->
                cityReport.printCitiesByCountryReport(cities, "Thailand"));
    }

    // US11: Cities by District

    // Tests retrieving cities by district when the database connection is null.
    @Test
    void testGetCitiesByDistrict() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByDistrict("Bangkok"));
    }

    // Tests that a city report for a specific district can be printed successfully.
    @Test
    void testPrintCitiesByDistrictReport() {
        assertDoesNotThrow(() ->
                cityReport.printCitiesByDistrictReport(cities, "Bangkok"));
    }

    // US12: Top N Populated Cities Worldwide

    // Tests retrieving the top N cities when the database connection is null.
    @Test
    void testGetTopNCities() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getTopNCities(5));
    }

    // Tests that a report containing the top populated cities can be printed.
    @Test
    void testPrintTopNCitiesWorldwideReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesWorldwideReport(cities, 2));
    }

    // US13: Top N Populated Cities by Continent

    // Tests that invalid continent names and non-positive limits return an empty list.
    @Test
    void testGetTopNCitiesByContinent() {
        assertTrue(cityReport.getTopNCitiesByContinent("", 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByContinent(null, 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByContinent("Asia", 0).isEmpty());
        assertTrue(cityReport.getTopNCitiesByContinent("Asia", 5).isEmpty());
    }

    // Tests that the top populated cities report for a continent can be printed.
    @Test
    void testPrintTopNCitiesByContinentReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByContinentReport(cities, "Asia", 2));
    }

    // US14: Top N Populated Cities by Region

    // Tests that invalid region names and non-positive limits return an empty list.
    @Test
    void testGetTopNCitiesByRegion() {
        assertTrue(cityReport.getTopNCitiesByRegion("", 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByRegion(null, 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByRegion("Southeast Asia", 0).isEmpty());
        assertTrue(cityReport.getTopNCitiesByRegion("Southeast Asia", 5).isEmpty());
    }

    // Tests that the top populated cities report for a region can be printed.
    @Test
    void testPrintTopNCitiesByRegionReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByRegionReport(cities, "Southeast Asia", 2));
    }

    // US15: Top N Populated Cities by Country

    // Tests retrieving the top cities by country when the database connection is null.
    @Test
    void testGetTopNCitiesByCountry() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getTopNCitiesByCountry("Thailand", 5));
    }

    // Tests that the top populated cities report for a country can be printed.
    @Test
    void testPrintTopNCitiesByCountryReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByCountryReport(cities, "Thailand", 2));
    }

    // US16: Top N Populated Cities by District

    // Tests that invalid district names and non-positive limits return an empty list.
    @Test
    void testGetTopNCitiesByDistrict() {
        assertTrue(cityReport.getTopNCitiesByDistrict(null, 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByDistrict("", 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByDistrict("Bangkok", 0).isEmpty());
    }

    // Tests that the top populated cities report for a district can be printed.
    @Test
    void testPrintTopNCitiesByDistrictReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByDistrictReport(cities, "Bangkok", 2));
    }
}