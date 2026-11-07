package com.napier.sem.reports;

import com.napier.sem.models.City;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CityReportIntegrationTest {

    private static Connection con;
    private static CityReport cityReport;

    @BeforeAll
    static void init() {

        // Load the MySQL JDBC driver.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            fail("Could not load SQL driver: " + e.getMessage());
        }

        // Retry database connection while the Docker container starts.
        int retries = 10;

        for (int i = 0; i < retries; i++) {
            System.out.println("Connecting to database...");

            try {
                con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );

                if (con != null) {
                    System.out.println("Successfully connected!");
                    break;
                }

            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + (i + 1));

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    fail("Database connection retry interrupted.");
                }
            }
        }

        // Stop the tests if the database is unavailable.
        if (con == null) {
            fail("Could not establish database connection after multiple retries.");
        }

        cityReport = new CityReport(con);
    }

    // Helper method to verify descending population order.
    private void assertPopulationDescending(List<City> cities) {
        for (int i = 1; i < cities.size(); i++) {
            assertTrue(
                    cities.get(i - 1).getPopulation() >= cities.get(i).getPopulation(),
                    "Cities should be sorted by population in descending order."
            );
        }
    }

    // ==========================================
    // US07: All Cities Integration Tests
    // ==========================================

    /**
     * Tests retrieving all cities sorted by population from the live database.
     */
    @Test
    void testGetAllCitiesSortedByPopulation() {
        List<City> cities = cityReport.getAllCitiesSortedByPopulation();

        assertNotNull(cities);
        assertFalse(cities.isEmpty(), "Cities list should not be empty.");
        assertPopulationDescending(cities);
    }

    /**
     * Tests generating the complete city report using live database records.
     */
    @Test
    void testGenerateCityReport() {
        List<City> cities = cityReport.getAllCitiesSortedByPopulation();

        assertNotNull(cities);
        assertFalse(cities.isEmpty());

        cityReport.generateCityReport(cities);
    }

    // ==========================================
    // US08: Cities by Continent Integration Tests
    // ==========================================

    /**
     * Tests retrieving cities belonging to countries in Asia.
     */
    @Test
    void testGetCitiesByContinent() {
        List<City> asianCities = cityReport.getCitiesByContinent("Asia");

        assertNotNull(asianCities);
        assertFalse(asianCities.isEmpty(), "Asian cities list should not be empty.");
        assertPopulationDescending(asianCities);
    }

    /**
     * Tests generating the continent city report using live database data.
     */
    @Test
    void testGenerateContinentCityReport() {
        assertDoesNotThrow(() ->
                cityReport.generateContinentCityReport("Asia"));
    }

    // ==========================================
    // US09: Cities by Region Integration Tests
    // ==========================================

    /**
     * Tests retrieving cities belonging to Southeast Asia.
     */
    @Test
    void testGetCitiesByRegion() {
        List<City> regionCities = cityReport.getCitiesByRegion("Southeast Asia");

        assertNotNull(regionCities);
        assertFalse(regionCities.isEmpty(), "Region cities list should not be empty.");
        assertPopulationDescending(regionCities);
    }

    /**
     * Tests printing the region city report using live database records.
     */
    @Test
    void testPrintCitiesByRegionReport() {
        List<City> regionCities = cityReport.getCitiesByRegion("Southeast Asia");

        assertNotNull(regionCities);
        assertFalse(regionCities.isEmpty());

        cityReport.printCitiesByRegionReport(regionCities, "Southeast Asia");
    }

    // ==========================================
    // US10: Cities by Country Integration Tests
    // ==========================================

    /**
     * Tests retrieving cities belonging to Thailand.
     */
    @Test
    void testGetCitiesByCountry() {
        List<City> thaiCities = cityReport.getCitiesByCountry("Thailand");

        assertNotNull(thaiCities);
        assertFalse(thaiCities.isEmpty(), "Thailand cities list should not be empty.");

        for (City city : thaiCities) {
            assertEquals("Thailand", city.getCountry());
        }

        assertPopulationDescending(thaiCities);
    }

    /**
     * Tests printing the country city report using live database records.
     */
    @Test
    void testPrintCitiesByCountryReport() {
        List<City> thaiCities = cityReport.getCitiesByCountry("Thailand");

        assertNotNull(thaiCities);
        assertFalse(thaiCities.isEmpty());

        cityReport.printCitiesByCountryReport(thaiCities, "Thailand");
    }

    // ==========================================
    // US11: Cities by District Integration Tests
    // ==========================================

    /**
     * Tests retrieving cities belonging to the Bangkok district.
     */
    @Test
    void testGetCitiesByDistrict() {
        List<City> bangkokCities = cityReport.getCitiesByDistrict("Bangkok");

        assertNotNull(bangkokCities);
        assertFalse(bangkokCities.isEmpty(), "Bangkok district cities should not be empty.");

        for (City city : bangkokCities) {
            assertEquals("Bangkok", city.getDistrict());
        }

        assertPopulationDescending(bangkokCities);
    }

    /**
     * Tests printing the district city report using live database records.
     */
    @Test
    void testPrintCitiesByDistrictReport() {
        List<City> bangkokCities = cityReport.getCitiesByDistrict("Bangkok");

        assertNotNull(bangkokCities);
        assertFalse(bangkokCities.isEmpty());

        cityReport.printCitiesByDistrictReport(bangkokCities, "Bangkok");
    }

    // ==========================================
    // US12: Top N Cities Worldwide Integration Tests
    // ==========================================

    /**
     * Tests retrieving the top five populated cities worldwide.
     */
    @Test
    void testGetTopNCities() {
        List<City> topCities = cityReport.getTopNCities(5);

        assertNotNull(topCities);
        assertEquals(5, topCities.size());
        assertPopulationDescending(topCities);
    }

    /**
     * Tests printing the top five populated cities worldwide.
     */
    @Test
    void testPrintTopNCitiesWorldwideReport() {
        List<City> topCities = cityReport.getTopNCities(5);

        assertNotNull(topCities);
        assertEquals(5, topCities.size());

        System.out.println("\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 Worldwide) ---");
        cityReport.printTopNCitiesWorldwideReport(topCities, 5);
    }

    // ==========================================
    // US13: Top N Cities by Continent Integration Tests
    // ==========================================

    /**
     * Tests retrieving the top five populated cities in Asia.
     */
    @Test
    void testGetTopNCitiesByContinent() {
        List<City> topAsianCities =
                cityReport.getTopNCitiesByContinent("Asia", 5);

        assertNotNull(topAsianCities);
        assertFalse(topAsianCities.isEmpty(), "Top Asian cities list should not be empty.");
        assertTrue(topAsianCities.size() <= 5);
        assertPopulationDescending(topAsianCities);
    }

    /**
     * Tests printing the top five populated cities in Asia.
     */
    @Test
    void testPrintTopNCitiesByContinentReport() {
        List<City> topAsianCities =
                cityReport.getTopNCitiesByContinent("Asia", 5);

        assertNotNull(topAsianCities);
        assertFalse(topAsianCities.isEmpty());

        System.out.println("\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 in Asia) ---");
        cityReport.printTopNCitiesByContinentReport(topAsianCities, "Asia", 5);
    }

    // ==========================================
    // US14: Top N Cities by Region Integration Tests
    // ==========================================

    /**
     * Tests retrieving the top five populated cities in Southeast Asia.
     */
    @Test
    void testGetTopNCitiesByRegion() {
        List<City> topRegionCities =
                cityReport.getTopNCitiesByRegion("Southeast Asia", 5);

        assertNotNull(topRegionCities);
        assertFalse(topRegionCities.isEmpty(), "Top region cities list should not be empty.");
        assertTrue(topRegionCities.size() <= 5);
        assertPopulationDescending(topRegionCities);
    }

    /**
     * Tests printing the top five populated cities in Southeast Asia.
     */
    @Test
    void testPrintTopNCitiesByRegionReport() {
        List<City> topRegionCities =
                cityReport.getTopNCitiesByRegion("Southeast Asia", 5);

        assertNotNull(topRegionCities);
        assertFalse(topRegionCities.isEmpty());

        System.out.println("\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 in Southeast Asia) ---");
        cityReport.printTopNCitiesByRegionReport(
                topRegionCities, "Southeast Asia", 5
        );
    }

    // ==========================================
    // US15: Top N Cities by Country Integration Tests
    // ==========================================

    /**
     * Tests retrieving the top five populated cities in Thailand.
     */
    @Test
    void testGetTopNCitiesByCountry() {
        List<City> topThaiCities =
                cityReport.getTopNCitiesByCountry("Thailand", 5);

        assertNotNull(topThaiCities);
        assertFalse(topThaiCities.isEmpty(), "Top Thailand cities list should not be empty.");
        assertTrue(topThaiCities.size() <= 5);

        for (City city : topThaiCities) {
            assertEquals("Thailand", city.getCountry());
        }

        assertPopulationDescending(topThaiCities);
    }

    /**
     * Tests printing the top five populated cities in Thailand.
     */
    @Test
    void testPrintTopNCitiesByCountryReport() {
        List<City> topThaiCities =
                cityReport.getTopNCitiesByCountry("Thailand", 5);

        assertNotNull(topThaiCities);
        assertFalse(topThaiCities.isEmpty());

        System.out.println("\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 in Thailand) ---");
        cityReport.printTopNCitiesByCountryReport(
                topThaiCities, "Thailand", 5
        );
    }

    // ==========================================
    // US16: Top N Cities by District Integration Tests
    // ==========================================

    /**
     * Tests retrieving the top populated cities in Bangkok district.
     */
    @Test
    void testGetTopNCitiesByDistrict() {
        List<City> topBangkokCities =
                cityReport.getTopNCitiesByDistrict("Bangkok", 5);

        assertNotNull(topBangkokCities);
        assertFalse(topBangkokCities.isEmpty(), "Top Bangkok district cities list should not be empty.");
        assertTrue(topBangkokCities.size() <= 5);

        for (City city : topBangkokCities) {
            assertEquals("Bangkok", city.getDistrict());
        }

        assertPopulationDescending(topBangkokCities);
    }

    /**
     * Tests printing the top populated cities in Bangkok district.
     */
    @Test
    void testPrintTopNCitiesByDistrictReport() {
        List<City> topBangkokCities =
                cityReport.getTopNCitiesByDistrict("Bangkok", 5);

        assertNotNull(topBangkokCities);
        assertFalse(topBangkokCities.isEmpty());

        System.out.println("\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 in Bangkok) ---");
        cityReport.printTopNCitiesByDistrictReport(
                topBangkokCities, "Bangkok", 5
        );
    }

    // ==========================================
    // Close Database Connection
    // ==========================================

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
