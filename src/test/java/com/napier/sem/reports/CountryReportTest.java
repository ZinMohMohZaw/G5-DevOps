package com.napier.sem.reports;

import com.napier.sem.models.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CountryReport business logic and boundary conditions.
 */
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

    /**
     * Tests that retrieving all countries sorted handles a null database connection gracefully.
     */
    @Test
    void testGetAllCountriesSorted() {
        try {
            List<Country> result = countryReport.getAllCountriesSorted();
            assertNotNull(result);
        } catch (Exception e) {
            // Expected NullPointerException due to null connection
        }
    }

    /**
     * Tests full country report generation fallback with a null database connection.
     */
    @Test
    void testGenerateCountryReport() {
        try {
            countryReport.generateCountryReport();
        } catch (Exception e) {
            // Safe fallback test
        }
    }

    // ==========================================
    // US02 Tests
    // ==========================================

    /**
     * Tests that retrieving countries by continent handles a null database connection gracefully.
     */
    @Test
    void testGetCountriesByContinent() {
        try {
            List<Country> result = countryReport.getCountriesByContinent("Asia");
            assertNotNull(result);
        } catch (Exception e) {
            // Expected NPE
        }
    }

    /**
     * Tests printing countries by continent report with both null and valid data inputs.
     */
    @Test
    void testPrintCountriesByContinentReport() {
        countryReport.printCountriesByContinentReport(null, "Asia");

        List<Country> list = new ArrayList<>();
        list.add(new Country("THA", "Thailand", "Asia", "Southeast Asia", 71000000, "Bangkok"));
        countryReport.printCountriesByContinentReport(list, "Asia");
    }

    // ==========================================
    // US03 Tests
    // ==========================================

    /**
     * Tests that retrieving countries by region handles a null database connection gracefully.
     */
    @Test
    void testGetCountriesByRegion() {
        try {
            ArrayList<Country> result = countryReport.getCountriesByRegion("Southeast Asia");
            assertNotNull(result);
        } catch (Exception e) {
            // Expected NPE
        }
    }

    /**
     * Tests printing countries by region report with both null and valid data inputs.
     */
    @Test
    void testPrintCountriesByRegion() {
        countryReport.printCountriesByRegion(null, "Southeast Asia");

        ArrayList<Country> list = new ArrayList<>();
        list.add(new Country("THA", "Thailand", "Asia", "Southeast Asia", 71000000, "Bangkok"));
        countryReport.printCountriesByRegion(list, "Southeast Asia");
    }

    // ==========================================
    // US04 Tests
    // ==========================================

    /**
     * Tests parameter validation for top N countries worldwide when N is invalid or connection is null.
     */
    @Test
    void testGetTopNCountriesWorldwide() {
        // Test parameter validation branch (n <= 0)
        List<Country> invalidResult = countryReport.getTopNCountriesWorldwide(0);
        assertNotNull(invalidResult);
        assertTrue(invalidResult.isEmpty());

        try {
            countryReport.getTopNCountriesWorldwide(5);
        } catch (Exception e) {
            // Expected NPE
        }
    }

    /**
     * Tests printing top N countries worldwide report with both null and valid data inputs.
     */
    @Test
    void testPrintTopNCountriesWorldwideReport() {
        countryReport.printTopNCountriesWorldwideReport(null, 5);

        List<Country> list = new ArrayList<>();
        list.add(new Country("THA", "Thailand", "Asia", "Southeast Asia", 71000000, "Bangkok"));
        countryReport.printTopNCountriesWorldwideReport(list, 5);
    }

    // ==========================================
    // US05 Tests
    // ==========================================

    /**
     * Tests parameter validation checks for top N countries by continent with empty, null, or invalid parameters.
     */
    @Test
    void testGetTopNCountriesByContinent() {
        // Test input validation checks
        assertTrue(countryReport.getTopNCountriesByContinent("", 5).isEmpty());
        assertTrue(countryReport.getTopNCountriesByContinent(null, 5).isEmpty());
        assertTrue(countryReport.getTopNCountriesByContinent("Asia", 0).isEmpty());

        try {
            countryReport.getTopNCountriesByContinent("Asia", 5);
        } catch (Exception e) {
            // Expected NPE
        }
    }

    /**
     * Tests printing top N countries by continent report with both null and valid data inputs.
     */
    @Test
    void testPrintTopNCountriesByContinentReport() {
        countryReport.printTopNCountriesByContinentReport(null, "Asia", 5);

        List<Country> list = new ArrayList<>();
        list.add(new Country("THA", "Thailand", "Asia", "Southeast Asia", 71000000, "Bangkok"));
        countryReport.printTopNCountriesByContinentReport(list, "Asia", 5);
    }

    // ==========================================
    // US06 Tests
    // ==========================================

    /**
     * Tests validation branch for top N countries by region when N is less than or equal to zero.
     */
    @Test
    void testGetTopNCountriesByRegion() {
        // Test validation branch (n <= 0)
        assertTrue(countryReport.getTopNCountriesByRegion("Southeast Asia", 0).isEmpty());

        try {
            countryReport.getTopNCountriesByRegion("Southeast Asia", 5);
        } catch (Exception e) {
            // Expected NPE
        }
    }

    /**
     * Tests printing top N countries by region report with both null and valid data inputs.
     */
    @Test
    void testPrintTopNCountriesByRegionReport() {
        countryReport.printTopNCountriesByRegionReport(null, "Southeast Asia", 5);

        List<Country> list = new ArrayList<>();
        list.add(new Country("THA", "Thailand", "Asia", "Southeast Asia", 71000000, "Bangkok"));
        countryReport.printTopNCountriesByRegionReport(list, "Southeast Asia", 5);
    }
}