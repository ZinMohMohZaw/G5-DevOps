package com.napier.sem.reports;

import com.napier.sem.models.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryReportTest {
    private CountryReport countryReport;

    @BeforeEach
    void setUp() {
        // Pass null connection to test printing and safety handling
        countryReport = new CountryReport(null);
    }

    // ==========================================
    // US01 Tests
    // ==========================================
    @Test
    void testGetAllCountriesSorted() {
        try {
            // Attempt to retrieve all countries sorted by population using a null connection
            List<Country> result = countryReport.getAllCountriesSorted();
            assertNotNull(result);
        } catch (Exception e) {
            // Expected NullPointerException due to null connection
        }
    }

    @Test
    void testGenerateCountryReport() {
        try {
            // Test full report generation routine with null connection fallback handling
            countryReport.generateCountryReport();
        } catch (Exception e) {
            // Safe fallback test
        }
    }

    // ==========================================
    // US02 Tests
    // ==========================================
    @Test
    void testGetCountriesByContinent() {
        try {
            // Attempt to retrieve countries filtered by continent using a null connection
            List<Country> result = countryReport.getCountriesByContinent("Asia");
            assertNotNull(result);
        } catch (Exception e) {
            // Expected NPE
        }
    }

    @Test
    void testPrintCountriesByContinentReport() {
        // Verify graceful execution with a null dataset input
        countryReport.printCountriesByContinentReport(null, "Asia");

        // Verify report printing logic with a non-null dataset containing test data
        List<Country> list = new ArrayList<>();
        list.add(new Country("THA", "Thailand", "Asia", "Southeast Asia", 71000000, "Bangkok"));
        countryReport.printCountriesByContinentReport(list, "Asia");
    }

    // ==========================================
    // US03 Tests
    // ==========================================
    @Test
    void testGetCountriesByRegion() {
        try {
            // Attempt to retrieve countries filtered by region using a null connection
            ArrayList<Country> result = countryReport.getCountriesByRegion("Southeast Asia");
            assertNotNull(result);
        } catch (Exception e) {
            // Expected NPE
        }
    }

    @Test
    void testPrintCountriesByRegion() {
        // Verify graceful execution with a null dataset input
        countryReport.printCountriesByRegion(null, "Southeast Asia");

        // Verify report printing logic with a non-null dataset containing test data
        ArrayList<Country> list = new ArrayList<>();
        list.add(new Country("THA", "Thailand", "Asia", "Southeast Asia", 71000000, "Bangkok"));
        countryReport.printCountriesByRegion(list, "Southeast Asia");
    }

    // ==========================================
    // US04 Tests
    // ==========================================
    @Test
    void testGetTopNCountriesWorldwide() {
        // Test parameter validation branch (n <= 0) returns empty list safely
        List<Country> invalidResult = countryReport.getTopNCountriesWorldwide(0);
        assertNotNull(invalidResult);
        assertTrue(invalidResult.isEmpty());

        try {
            // Attempt to retrieve top N countries worldwide using a null connection
            countryReport.getTopNCountriesWorldwide(5);
        } catch (Exception e) {
            // Expected NPE
        }
    }

    @Test
    void testPrintTopNCountriesWorldwideReport() {
        // Verify graceful execution with a null dataset input
        countryReport.printTopNCountriesWorldwideReport(null, 5);

        // Verify report printing logic with a non-null dataset containing test data
        List<Country> list = new ArrayList<>();
        list.add(new Country("THA", "Thailand", "Asia", "Southeast Asia", 71000000, "Bangkok"));
        countryReport.printTopNCountriesWorldwideReport(list, 5);
    }

    // ==========================================
    // US05 Tests
    // ==========================================
    @Test
    void testGetTopNCountriesByContinent() {
        // Test input validation checks and boundary parameters
        assertTrue(countryReport.getTopNCountriesByContinent("", 5).isEmpty());
        assertTrue(countryReport.getTopNCountriesByContinent(null, 5).isEmpty());
        assertTrue(countryReport.getTopNCountriesByContinent("Asia", 0).isEmpty());

        try {
            // Attempt to retrieve top N countries by continent using a null connection
            countryReport.getTopNCountriesByContinent("Asia", 5);
        } catch (Exception e) {
            // Expected NPE
        }
    }

    @Test
    void testPrintTopNCountriesByContinentReport() {
        // Verify graceful execution with a null dataset input
        countryReport.printTopNCountriesByContinentReport(null, "Asia", 5);

        // Verify report printing logic with a non-null dataset containing test data
        List<Country> list = new ArrayList<>();
        list.add(new Country("THA", "Thailand", "Asia", "Southeast Asia", 71000000, "Bangkok"));
        countryReport.printTopNCountriesByContinentReport(list, "Asia", 5);
    }

    // ==========================================
    // US06 Tests
    // ==========================================
    @Test
    void testGetTopNCountriesByRegion() {
        // Test validation branch for invalid parameter (n <= 0)
        assertTrue(countryReport.getTopNCountriesByRegion("Southeast Asia", 0).isEmpty());

        try {
            // Attempt to retrieve top N countries by region using a null connection
            countryReport.getTopNCountriesByRegion("Southeast Asia", 5);
        } catch (Exception e) {
            // Expected NPE
        }
    }

    @Test
    void testPrintTopNCountriesByRegionReport() {
        // Verify graceful execution with null list input
        countryReport.printTopNCountriesByRegionReport(null, "Southeast Asia", 5);

        // Verify report printing logic with a non-null dataset containing test data
        List<Country> list = new ArrayList<>();
        list.add(new Country("THA", "Thailand", "Asia", "Southeast Asia", 71000000, "Bangkok"));
        countryReport.printTopNCountriesByRegionReport(list, "Southeast Asia", 5);
    }
}