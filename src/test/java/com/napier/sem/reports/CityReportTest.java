
package com.napier.sem.reports;

import com.napier.sem.models.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityReportTest {

    private CityReport cityReport;
    private List<City> cities;

    @BeforeEach
    void setUp() {
        cityReport = new CityReport(null);

        cities = new ArrayList<>();
        cities.add(new City("Bangkok", "Thailand", "Bangkok", 10500000));
        cities.add(new City("Chiang Mai", "Thailand", "Chiang Mai", 127000));
    }

    // US07: All Cities Sorted by Population

    @Test
    void testGetAllCitiesSortedByPopulation() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getAllCitiesSortedByPopulation());
    }

    @Test
    void testGenerateCityReport() {
        assertDoesNotThrow(() -> cityReport.generateCityReport(cities));
    }

    // US08: Cities by Continent

    @Test
    void testGetCitiesByContinent() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByContinent("Asia"));
    }

    @Test
    void testGenerateContinentCityReport() {
        assertThrows(NullPointerException.class,
                () -> cityReport.generateContinentCityReport("Asia"));
    }

    // US09: Cities by Region

    @Test
    void testGetCitiesByRegion() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByRegion("Southeast Asia"));
    }

    @Test
    void testPrintCitiesByRegionReport() {
        assertDoesNotThrow(() ->
                cityReport.printCitiesByRegionReport(cities, "Southeast Asia"));
    }

    // US10: Cities by Country

    @Test
    void testGetCitiesByCountry() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByCountry("Thailand"));
    }

    @Test
    void testPrintCitiesByCountryReport() {
        assertDoesNotThrow(() ->
                cityReport.printCitiesByCountryReport(cities, "Thailand"));
    }

    // US11: Cities by District

    @Test
    void testGetCitiesByDistrict() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByDistrict("Bangkok"));
    }

    @Test
    void testPrintCitiesByDistrictReport() {
        assertDoesNotThrow(() ->
                cityReport.printCitiesByDistrictReport(cities, "Bangkok"));
    }

    // US12: Top N Populated Cities Worldwide

    @Test
    void testGetTopNCities() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getTopNCities(5));
    }

    @Test
    void testPrintTopNCitiesWorldwideReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesWorldwideReport(cities, 2));
    }

    // US13: Top N Populated Cities by Continent

    @Test
    void testGetTopNCitiesByContinent() {
        assertTrue(cityReport.getTopNCitiesByContinent("", 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByContinent(null, 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByContinent("Asia", 0).isEmpty());
        assertTrue(cityReport.getTopNCitiesByContinent("Asia", 5).isEmpty());
    }

    @Test
    void testPrintTopNCitiesByContinentReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByContinentReport(cities, "Asia", 2));
    }

    // US14: Top N Populated Cities by Region

    @Test
    void testGetTopNCitiesByRegion() {
        assertTrue(cityReport.getTopNCitiesByRegion("", 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByRegion(null, 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByRegion("Southeast Asia", 0).isEmpty());
        assertTrue(cityReport.getTopNCitiesByRegion("Southeast Asia", 5).isEmpty());
    }

    @Test
    void testPrintTopNCitiesByRegionReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByRegionReport(cities, "Southeast Asia", 2));
    }

    // US15: Top N Populated Cities by Country

    @Test
    void testGetTopNCitiesByCountry() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getTopNCitiesByCountry("Thailand", 5));
    }

    @Test
    void testPrintTopNCitiesByCountryReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByCountryReport(cities, "Thailand", 2));
    }

    // US16: Top N Populated Cities by District

    @Test
    void testGetTopNCitiesByDistrict() {
        assertTrue(cityReport.getTopNCitiesByDistrict(null, 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByDistrict("", 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByDistrict("Bangkok", 0).isEmpty());
    }

    @Test
    void testPrintTopNCitiesByDistrictReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByDistrictReport(cities, "Bangkok", 2));
    }
}
