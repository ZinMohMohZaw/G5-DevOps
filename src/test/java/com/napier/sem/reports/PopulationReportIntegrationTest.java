package com.napier.sem.reports;

import com.napier.sem.models.Population;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration Test Suite for PopulationReport against a live MySQL database container.
 */
public class PopulationReportIntegrationTest {
    private static Connection con;
    private static PopulationReport report;

    @BeforeAll
    static void init() {
        // Load the MySQL JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            fail("Could not load SQL driver: " + e.getMessage());
        }

        // Attempt connection with retries to allow the database container time to start up
        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database for Population tests...");
            try {
                // Connect to the world database using standard test credentials
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                if (con != null) {
                    System.out.println("Successfully connected!");
                    break;
                }
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + (i + 1));
                try {
                    // Wait 5 seconds before retrying connection
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        // Fail if all connection retries are exhausted
        if (con == null) {
            fail("Could not establish database connection after multiple retries.");
        }

        // Initialize the report handler with the active connection
        report = new PopulationReport(con);
    }

    /**
     * Tests calculating world population from the live database.
     */
    @Test
    void testGetWorldPopulation() {
        Population pop = report.getWorldPopulation();
        assertNotNull(pop);
        assertTrue(pop.getTotalPopulation() > 0);
        report.printWorldPopulationReport(pop);
    }

    /**
     * Tests retrieving continent population from the live database.
     */
    @Test
    void testGetPopulationByContinent() {
        Population pop = report.getPopulationByContinent("Asia");
        assertNotNull(pop);
        assertEquals("Asia", pop.getName());
        assertTrue(pop.getTotalPopulation() > 0);
        report.printPopulationByContinentReport(pop);
    }

    /**
     * Tests retrieving region population from the live database.
     */
    @Test
    void testGetPopulationByRegion() {
        Population pop = report.getPopulationByRegion("Southeast Asia");
        assertNotNull(pop);
        assertEquals("Southeast Asia", pop.getName());
        assertTrue(pop.getTotalPopulation() > 0);
        report.printPopulationByRegionReport(pop);
    }

    /**
     * Tests retrieving country population from the live database.
     */
    @Test
    void testGetPopulationByCountry() {
        Population pop = report.getPopulationByCountry("Thailand");
        assertNotNull(pop);
        assertEquals("Thailand", pop.getName());
        assertTrue(pop.getTotalPopulation() > 0);
        report.printPopulationByCountryReport(pop);
    }

    /**
     * Tests retrieving city and non-city population statistics by continent from the live database.
     */
    @Test
    void testGetCityPopulationByContinent() {
        List<Population> list = report.getCityPopulationByContinent();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        report.printCityPopulationByContinentReport(list);
    }

    /**
     * Tests retrieving city and non-city population statistics by region from the live database.
     */
    @Test
    void testGetCityPopulationByRegion() {
        List<Population> list = report.getCityPopulationByRegion();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        report.printCityPopulationByRegionReport(list);
    }

    /**
     * Tests retrieving city and non-city population statistics by country from the live database.
     */
    @Test
    void testGetCityPopulationByCountry() {
        List<Population> list = report.getCityPopulationByCountry();
        assertNotNull(list);
        assertFalse(list.isEmpty());
        report.printCityPopulationByCountryReport(list);
    }

    /**
     * Tests retrieving district population from the live database.
     */
    @Test
    void testGetPopulationByDistrict() {
        Population pop = report.getPopulationByDistrict("Bangkok");
        assertNotNull(pop);
        assertEquals("Bangkok", pop.getName());
        assertTrue(pop.getTotalPopulation() > 0);
        report.printPopulationByDistrictReport(pop);
    }

    /**
     * Tests retrieving city population within a specific country from the live database.
     */
    @Test
    void testGetPopulationByCity() {
        Population pop = report.getPopulationByCity("Bangkok", "Thailand");
        assertNotNull(pop);
        assertEquals("Bangkok", pop.getName());
        assertTrue(pop.getTotalPopulation() > 0);
        report.printPopulationByCityReport(pop);
    }

    @AfterAll
    static void tearDown() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Population test database connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    // =========================================================================
    // Additional Branch Coverage Tests (Not-Found & Edge Cases)
    // =========================================================================
    /**
     * Tests the fallback branch when querying a continent that does not exist in the database.
     */
    @Test
    void testGetPopulationByContinentNotFound() {
        // Tests fallback branch when a continent does not exist in the database
        Population pop = report.getPopulationByContinent("NonExistentContinent");
        assertNotNull(pop);
        assertEquals(0, pop.getTotalPopulation());
        report.printPopulationByContinentReport(pop);
    }
    /**
     * Tests the fallback branch when querying a region that does not exist in the database.
     */
    @Test
    void testGetPopulationByRegionNotFound() {
        // Tests fallback branch when a region does not exist
        Population pop = report.getPopulationByRegion("NonExistentRegion");
        assertNotNull(pop);
        assertEquals(0, pop.getTotalPopulation());
        report.printPopulationByRegionReport(pop);
    }
    /**
     * Tests the fallback branch when querying a country that does not exist in the database.
     */
    @Test
    void testGetPopulationByCountryNotFound() {
        // Tests fallback branch when a country does not exist
        Population pop = report.getPopulationByCountry("Atlantis");
        assertNotNull(pop);
        assertEquals(0, pop.getTotalPopulation());
        report.printPopulationByCountryReport(pop);
    }
    /**
     * Tests the null return branch when querying a non-existent district name.
     */
    @Test
    void testGetPopulationByDistrictNotFound() {
        // Tests null return branch when district is not found
        Population pop = report.getPopulationByDistrict("NonExistentDistrictXYZ");
        assertNull(pop);
        report.printPopulationByDistrictReport(pop);
    }
    /**
     * Tests the null return branch when querying a non-existent city and country combination.
     */
    @Test
    void testGetPopulationByCityNotFound() {
        // Tests null return branch when city/country combination is not found
        Population pop = report.getPopulationByCity("AtlantisCity", "Thailand");
        assertNull(pop);
        report.printPopulationByCityReport(pop);
    }
    /**
     * Tests the null-safety loop skip branch (if population == null continue) in report print loops.
     */
    @Test
    void testPrintReportsWithNullElements() {
        // Tests the `if (population == null) continue;` branch inside report printing loops
        List<Population> continentList = new java.util.ArrayList<>();
        continentList.add(null);
        assertDoesNotThrow(() -> report.printCityPopulationByContinentReport(continentList));

        List<Population> regionList = new java.util.ArrayList<>();
        regionList.add(null);
        assertDoesNotThrow(() -> report.printCityPopulationByRegionReport(regionList));

        List<Population> countryList = new java.util.ArrayList<>();
        countryList.add(null);
        assertDoesNotThrow(() -> report.printCityPopulationByCountryReport(countryList));
    }
    /**
     * Exhaustively tests compound boolean short-circuit branches in getPopulationByCity validation logic.
     */
    @Test
    void testGetPopulationByCityShortCircuitBranches() {
        // Exhaustively test every boolean combination of the compound `||` condition
        // in getPopulationByCity(city, country) to cover all generated jump branches.
        assertNull(report.getPopulationByCity("", "Thailand"));        // city empty, country valid
        assertNull(report.getPopulationByCity("Bangkok", ""));        // city valid, country empty
        assertNull(report.getPopulationByCity(null, "Thailand"));      // city null (short-circuits), country valid
        assertNull(report.getPopulationByCity("Bangkok", null));      // city valid, country null
        assertNull(report.getPopulationByCity("   ", "Thailand"));    // city blank, country valid
        assertNull(report.getPopulationByCity("Bangkok", "   "));     // city valid, country blank
    }
    /**
     * Exhaustively tests short-circuit validation branches for world population input parameters.
     */
    @Test
    void testValidateWorldPopulationShortCircuitBranches() {
        // Exhaustively test the short-circuit branches in validateWorldPopulation
        assertFalse(report.validateWorldPopulation(new Population(null, 1000L))); // getName() == null is true
        assertFalse(report.validateWorldPopulation(new Population("", 1000L)));   // getName() is empty
        assertFalse(report.validateWorldPopulation(new Population("   ", 1000L))); // getName() is blank
    }
}
