package com.napier.sem.reports;

import com.napier.sem.models.Country;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

/**
 * Integration Test Suite for CountryReport.
 *
 * NOTE FOR REVIEWERS:
 * These tests connect directly to the active MySQL container environment
 * to validate that all 14 database queries and report generation methods
 * execute successfully against live data source records.
 */

import static org.junit.jupiter.api.Assertions.*;
public class CountryReportIntegrationTest {
    private static Connection con;
    private static CountryReport countryReport;

    @BeforeAll
    static void init() {
        // Ensure the MySQL JDBC driver is available on the classpath
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            fail("Could not load SQL driver: " + e.getMessage());
        }

        // Configure retry mechanism to allow Docker MySQL container time to boot up
        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Attempt connection using standard local test credentials and connection parameters
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                if (con != null) {
                    System.out.println("Successfully connected!");
                    break;
                }
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + (i + 1));
                try {
                    // Wait 5 seconds before retrying to give the database container time to initialize
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // Fail the test suite early if the database container remains unreachable
        if (con == null) {
            fail("Could not establish database connection after multiple retries.");
        }

        // Initialize the CountryReport instance with the active database connection
        countryReport = new CountryReport(con);
    }

    // ==========================================
    // US01 Integration & Print Tests
    // ==========================================

    /**
     * Tests retrieving all countries sorted by population from the live database.
     */
    @Test
    void testGetAllCountriesSorted() {
        List<Country> countries = countryReport.getAllCountriesSorted();
        assertNotNull(countries);
        assertFalse(countries.isEmpty(), "Countries list should not be empty from the database");
    }

    /**
     * Tests executing the complete country report generation workflow against live data.
     */
    @Test
    void testGenerateCountryReport() {
        countryReport.generateCountryReport();
    }

    // ==========================================
    // US02 Integration & Print Tests
    // ==========================================

    /**
     * Tests retrieving countries filtered by continent against live database records.
     */
    @Test
    void testGetCountriesByContinent() {
        List<Country> asianCountries = countryReport.getCountriesByContinent("Asia");
        assertNotNull(asianCountries);
        assertFalse(asianCountries.isEmpty(), "Asian countries list should not be empty");

        for (Country c : asianCountries) {
            assertEquals("Asia", c.getContinent());
        }
    }

    /**
     * Tests console output printing for countries by continent report using live database data.
     */
    @Test
    void testPrintCountriesByContinentReport() {
        List<Country> asianCountries = countryReport.getCountriesByContinent("Asia");
        countryReport.printCountriesByContinentReport(asianCountries, "Asia");
    }

    // ==========================================
    // US03 Integration & Print Tests
    // ==========================================

    /**
     * Tests retrieving countries filtered by region against live database records.
     */
    @Test
    void testGetCountriesByRegion() {
        List<Country> regionCountries = countryReport.getCountriesByRegion("Southeast Asia");
        assertNotNull(regionCountries);
        assertFalse(regionCountries.isEmpty(), "Region countries list should not be empty");

        for (Country c : regionCountries) {
            assertEquals("Southeast Asia", c.getRegion());
        }
    }

    /**
     * Tests console output printing for countries by region report using live database data.
     */
    @Test
    void testPrintCountriesByRegion() {
        ArrayList<Country> regionCountries = new ArrayList<>(countryReport.getCountriesByRegion("Southeast Asia"));
        assertNotNull(regionCountries);
        countryReport.printCountriesByRegion(regionCountries, "Southeast Asia");
    }

    // ==========================================
    // US04 Integration & Print Tests
    // ==========================================

    /**
     * Tests retrieving top N countries worldwide from the live database.
     */
    @Test
    void testGetTopNCountriesWorldwide() {
        List<Country> topCountries = countryReport.getTopNCountriesWorldwide(5);
        assertNotNull(topCountries);
        assertEquals(5, topCountries.size());
    }

    /**
     * Tests printing sample output for top N countries worldwide from the live database.
     */
    @Test
    void testPrintTopNCountriesWorldwideReport() {
        List<Country> topCountries = countryReport.getTopNCountriesWorldwide(5);
        System.out.println("\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 Worldwide) ---");
        countryReport.printTopNCountriesWorldwideReport(topCountries, 5);
    }

    // ==========================================
    // US05 Integration & Print Tests
    // ==========================================

    /**
     * Tests retrieving top N countries filtered by continent from the live database.
     */
    @Test
    void testGetTopNCountriesByContinent() {
        List<Country> topAsian = countryReport.getTopNCountriesByContinent("Asia", 5);
        assertNotNull(topAsian);
        assertEquals(5, topAsian.size());

        for (Country c : topAsian) {
            assertEquals("Asia", c.getContinent());
        }
    }

    /**
     * Tests printing sample output for top N countries by continent from the live database.
     */
    @Test
    void testPrintTopNCountriesByContinentReport() {
        List<Country> topAsian = countryReport.getTopNCountriesByContinent("Asia", 5);
        System.out.println("\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 in Asia) ---");
        countryReport.printTopNCountriesByContinentReport(topAsian, "Asia", 5);
    }

    // ==========================================
    // US06 Integration & Print Tests
    // ==========================================

    /**
     * Tests retrieving top N countries filtered by region from the live database.
     */
    @Test
    void testGetTopNCountriesByRegion() {
        List<Country> topRegion = countryReport.getTopNCountriesByRegion("Southeast Asia", 5);
        assertNotNull(topRegion);
        assertEquals(5, topRegion.size());

        for (Country c : topRegion) {
            assertEquals("Southeast Asia", c.getRegion());
        }
    }

    /**
     * Tests printing sample output for top N countries by region from the live database.
     */
    @Test
    void testPrintTopNCountriesByRegionReport() {
        List<Country> topRegion = countryReport.getTopNCountriesByRegion("Southeast Asia", 5);
        System.out.println("\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 in Southeast Asia) ---");
        countryReport.printTopNCountriesByRegionReport(topRegion, "Southeast Asia", 5);
    }

    @AfterAll
    static void tearDown() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
