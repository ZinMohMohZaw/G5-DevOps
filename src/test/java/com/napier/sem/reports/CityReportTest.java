
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

    // Initialize the report object and sample cities before each test.
    @BeforeEach
    void setUp() {
        cityReport = new CityReport(null);

        cities = new ArrayList<>();
        cities.add(new City("Bangkok", "Thailand", "Bangkok", 10500000));
        cities.add(new City("Chiang Mai", "Thailand", "Chiang Mai", 127000));
    }

    // US07: All Cities Sorted by Population

    // Test database retrieval when the connection is unavailable.
    @Test
    void testGetAllCitiesSortedByPopulation() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getAllCitiesSortedByPopulation());
    }

    // Test report generation using valid city data.
    @Test
    void testGenerateCityReport() {
        assertDoesNotThrow(() -> cityReport.generateCityReport(cities));
    }

    // Test report generation with empty and null lists.
    @Test
    void testGenerateCityReportEmptyAndNull() {
        assertDoesNotThrow(() -> cityReport.generateCityReport(new ArrayList<>()));
        assertDoesNotThrow(() -> cityReport.generateCityReport(null));
    }

    // Test report generation when the list contains a null city.
    @Test
    void testGenerateCityReportWithNullCity() {
        List<City> testCities = new ArrayList<>();
        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(() -> cityReport.generateCityReport(testCities));
    }

    // US08: Cities by Continent

    // Test continent-based retrieval when the database connection is unavailable.
    @Test
    void testGetCitiesByContinent() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByContinent("Asia"));
    }

    // Test continent report generation when the database connection is unavailable.
    @Test
    void testGenerateContinentCityReport() {
        assertThrows(NullPointerException.class,
                () -> cityReport.generateContinentCityReport("Asia"));
    }

    // US09: Cities by Region

    // Test region-based retrieval when the database connection is unavailable.
    @Test
    void testGetCitiesByRegion() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByRegion("Southeast Asia"));
    }

    // Test region report generation using sample city data.
    @Test
    void testPrintCitiesByRegionReport() {
        assertDoesNotThrow(() ->
                cityReport.printCitiesByRegionReport(cities, "Southeast Asia"));
    }

    // US10: Cities by Country

    // Test country-based retrieval when the database connection is unavailable.
    @Test
    void testGetCitiesByCountry() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByCountry("Thailand"));
    }

    // Test country report generation using sample city data.
    @Test
    void testPrintCitiesByCountryReport() {
        assertDoesNotThrow(() ->
                cityReport.printCitiesByCountryReport(cities, "Thailand"));
    }

    // US11: Cities by District

    // Test district-based retrieval when the database connection is unavailable.
    @Test
    void testGetCitiesByDistrict() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getCitiesByDistrict("Bangkok"));
    }

    // Test district report generation using sample city data.
    @Test
    void testPrintCitiesByDistrictReport() {
        assertDoesNotThrow(() ->
                cityReport.printCitiesByDistrictReport(cities, "Bangkok"));
    }

    // Test district report generation with empty and null lists.
    @Test
    void testPrintCitiesByDistrictReportEmpty() {
        assertDoesNotThrow(() ->
                cityReport.printCitiesByDistrictReport(new ArrayList<>(), "Bangkok"));

        assertDoesNotThrow(() ->
                cityReport.printCitiesByDistrictReport(null, null));
    }

    // Test district report generation when the list contains a null city.
    @Test
    void testPrintCitiesByDistrictReportWithNullCity() {
        List<City> testCities = new ArrayList<>();
        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(() ->
                cityReport.printCitiesByDistrictReport(testCities, "Bangkok"));
    }

    // US12: Top N Populated Cities Worldwide

    // Test top N city retrieval when the database connection is unavailable.
    @Test
    void testGetTopNCities() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getTopNCities(5));
    }

    // Test worldwide top N report generation using sample city data.
    @Test
    void testPrintTopNCitiesWorldwideReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesWorldwideReport(cities, 2));
    }

    // Test worldwide top N report with invalid numbers and lists.
    @Test
    void testPrintTopNCitiesWorldwideInvalid() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesWorldwideReport(cities, 0));

        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesWorldwideReport(cities, -1));

        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesWorldwideReport(new ArrayList<>(), 5));

        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesWorldwideReport(null, 5));
    }

    // US13: Top N Populated Cities by Continent

    // Test continent retrieval with invalid inputs and a null database connection.
    @Test
    void testGetTopNCitiesByContinent() {
        assertTrue(cityReport.getTopNCitiesByContinent("", 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByContinent(null, 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByContinent("Asia", 0).isEmpty());
        assertTrue(cityReport.getTopNCitiesByContinent("Asia", 5).isEmpty());
    }

    // Test continent top N report generation using sample city data.
    @Test
    void testPrintTopNCitiesByContinentReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByContinentReport(cities, "Asia", 2));
    }

    // Test continent report generation with an empty list.
    @Test
    void testPrintTopNCitiesByContinentEmpty() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByContinentReport(
                        new ArrayList<>(), "Asia", 5));
    }

    // Test continent report generation when the list contains a null city.
    @Test
    void testPrintTopNCitiesByContinentWithNullCity() {
        List<City> testCities = new ArrayList<>();
        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByContinentReport(
                        testCities, "Asia", 2));
    }

    // US14: Top N Populated Cities by Region

    // Test region retrieval with invalid inputs and a null database connection.
    @Test
    void testGetTopNCitiesByRegion() {
        assertTrue(cityReport.getTopNCitiesByRegion("", 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByRegion(null, 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByRegion("Southeast Asia", 0).isEmpty());
        assertTrue(cityReport.getTopNCitiesByRegion("Southeast Asia", 5).isEmpty());
    }

    // Test region top N report generation using sample city data.
    @Test
    void testPrintTopNCitiesByRegionReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByRegionReport(cities, "Southeast Asia", 2));
    }

    // Test region report generation with an empty list.
    @Test
    void testPrintTopNCitiesByRegionEmpty() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByRegionReport(
                        new ArrayList<>(), "Southeast Asia", 5));
    }

    // Test region report generation when the list contains a null city.
    @Test
    void testPrintTopNCitiesByRegionWithNullCity() {
        List<City> testCities = new ArrayList<>();
        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByRegionReport(
                        testCities, "Southeast Asia", 2));
    }

    // US15: Top N Populated Cities by Country

    // Test country-based top N retrieval when the database connection is unavailable.
    @Test
    void testGetTopNCitiesByCountry() {
        assertThrows(NullPointerException.class,
                () -> cityReport.getTopNCitiesByCountry("Thailand", 5));
    }

    // Test country top N report generation using sample city data.
    @Test
    void testPrintTopNCitiesByCountryReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByCountryReport(cities, "Thailand", 2));
    }

    // Test country report generation with empty and null lists.
    @Test
    void testPrintTopNCitiesByCountryEmpty() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByCountryReport(
                        new ArrayList<>(), "Thailand", 5));

        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByCountryReport(null, null, 5));
    }

    // Test country report generation when the list contains a null city.
    @Test
    void testPrintTopNCitiesByCountryWithNullCity() {
        List<City> testCities = new ArrayList<>();
        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByCountryReport(
                        testCities, "Thailand", 2));
    }

    // US16: Top N Populated Cities by District

    // Test district retrieval with invalid inputs and a null database connection.
    @Test
    void testGetTopNCitiesByDistrict() {
        assertTrue(cityReport.getTopNCitiesByDistrict(null, 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByDistrict("", 5).isEmpty());
        assertTrue(cityReport.getTopNCitiesByDistrict("Bangkok", 0).isEmpty());
    }

    // Test district top N report generation using sample city data.
    @Test
    void testPrintTopNCitiesByDistrictReport() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByDistrictReport(cities, "Bangkok", 2));
    }

    // Test district report generation with empty and null lists.
    @Test
    void testPrintTopNCitiesByDistrictEmpty() {
        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByDistrictReport(
                        new ArrayList<>(), "Bangkok", 5));

        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByDistrictReport(null, null, 5));
    }

    // Test district report generation when the list contains a null city.
    @Test
    void testPrintTopNCitiesByDistrictWithNullCity() {
        List<City> testCities = new ArrayList<>();
        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(() ->
                cityReport.printTopNCitiesByDistrictReport(
                        testCities, "Bangkok", 2));
    }
}
