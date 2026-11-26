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

/**
 * Integration tests for the CityReport class.
 * Verifies database retrieval, population ordering,
 * report generation and SQL exception handling.
 */
class CityReportIntegrationTest {

    private static Connection con;
    private static CityReport cityReport;

    /**
     * Establishes the World database connection and creates
     * the CityReport instance before all integration tests.
     */
    @BeforeAll
    static void init() {

        try {
            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );
        } catch (ClassNotFoundException e) {
            fail(
                    "Could not load SQL driver: "
                            + e.getMessage()
            );
        }

        int retries = 10;

        for (int i = 0;
             i < retries;
             i++) {

            System.out.println(
                    "Connecting to database..."
            );

            try {

                con =
                        DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false",
                                "root",
                                "example"
                        );

                if (con != null) {

                    System.out.println(
                            "Successfully connected!"
                    );

                    break;
                }

            } catch (SQLException sqle) {

                System.out.println(
                        "Failed to connect to database attempt "
                                + (i + 1)
                );

                try {

                    Thread.sleep(5000);

                } catch (InterruptedException ie) {

                    Thread.currentThread()
                            .interrupt();

                    fail(
                            "Database connection retry interrupted."
                    );
                }
            }
        }

        if (con == null) {

            fail(
                    "Could not establish database connection after multiple retries."
            );
        }

        cityReport =
                new CityReport(con);
    }

    /**
     * Verifies that the supplied cities are ordered
     * by population from highest to lowest.
     *
     * @param cities city records to validate
     */
    private void assertPopulationDescending(
            List<City> cities) {

        for (int i = 1;
             i < cities.size();
             i++) {

            assertTrue(
                    cities
                            .get(i - 1)
                            .getPopulation()
                            >=
                            cities
                                    .get(i)
                                    .getPopulation(),
                    "Cities should be sorted by population in descending order."
            );
        }
    }

    /**
     * Creates and closes a database connection for
     * testing CityReport SQL exception handling.
     *
     * @return a closed database connection
     * @throws SQLException if the connection cannot be created
     */
    private Connection createClosedConnection() throws SQLException {

        Connection closedConnection =
                DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );

        closedConnection.close();

        return closedConnection;
    }

    /**
     * Tests retrieval of all cities and verifies
     * descending population ordering.
     */
    @Test
    void testGetAllCitiesSortedByPopulation() {

        List<City> cities =
                cityReport
                        .getAllCitiesSortedByPopulation();

        assertAll(
                "All cities",

                () -> assertNotNull(cities),

                () -> assertFalse(
                        cities.isEmpty(),
                        "Cities list should not be empty."
                ),

                () -> assertPopulationDescending(cities)
        );
    }

    /**
     * Tests generation of the complete city report
     * using data from the World database.
     */
    @Test
    void testGenerateCityReport() {

        List<City> cities =
                cityReport
                        .getAllCitiesSortedByPopulation();

        assertAll(
                "Generate city report",

                () -> assertNotNull(cities),

                () -> assertFalse(
                        cities.isEmpty()
                )
        );

        cityReport.generateCityReport(cities);
    }

    /**
     * Tests retrieval of cities belonging
     * to the selected continent.
     */
    @Test
    void testGetCitiesByContinent() {

        List<City> asianCities =
                cityReport.getCitiesByContinent(
                        "Asia"
                );

        assertAll(
                "Cities by continent",

                () -> assertNotNull(
                        asianCities
                ),

                () -> assertFalse(
                        asianCities.isEmpty(),
                        "Asian cities list should not be empty."
                ),

                () -> assertPopulationDescending(
                        asianCities
                )
        );
    }

    /**
     * Tests generation of the city report
     * for the selected continent.
     */
    @Test
    void testGenerateContinentCityReport() {

        assertDoesNotThrow(
                () -> cityReport
                        .generateContinentCityReport(
                                "Asia"
                        )
        );
    }

    /**
     * Tests retrieval of cities belonging
     * to the selected region.
     */
    @Test
    void testGetCitiesByRegion() {

        List<City> regionCities =
                cityReport.getCitiesByRegion(
                        "Southeast Asia"
                );

        assertAll(
                "Cities by region",

                () -> assertNotNull(
                        regionCities
                ),

                () -> assertFalse(
                        regionCities.isEmpty(),
                        "Region cities list should not be empty."
                ),

                () -> assertPopulationDescending(
                        regionCities
                )
        );
    }

    /**
     * Tests generation of the city report
     * for the selected region.
     */
    @Test
    void testPrintCitiesByRegionReport() {

        List<City> regionCities =
                cityReport.getCitiesByRegion(
                        "Southeast Asia"
                );

        assertAll(
                "Print cities by region",

                () -> assertNotNull(
                        regionCities
                ),

                () -> assertFalse(
                        regionCities.isEmpty()
                )
        );

        cityReport.printCitiesByRegionReport(
                regionCities,
                "Southeast Asia"
        );
    }

    /**
     * Tests retrieval of cities belonging
     * to the selected country.
     */
    @Test
    void testGetCitiesByCountry() {

        List<City> thaiCities =
                cityReport.getCitiesByCountry(
                        "Thailand"
                );

        assertAll(
                "Cities by country",

                () -> assertNotNull(
                        thaiCities
                ),

                () -> assertFalse(
                        thaiCities.isEmpty(),
                        "Thailand cities list should not be empty."
                ),

                () -> {
                    for (City city : thaiCities) {
                        assertEquals(
                                "Thailand",
                                city.getCountry()
                        );
                    }
                },

                () -> assertPopulationDescending(
                        thaiCities
                )
        );
    }

    /**
     * Tests generation of the city report
     * for the selected country.
     */
    @Test
    void testPrintCitiesByCountryReport() {

        List<City> thaiCities =
                cityReport.getCitiesByCountry(
                        "Thailand"
                );

        assertAll(
                "Print cities by country",

                () -> assertNotNull(
                        thaiCities
                ),

                () -> assertFalse(
                        thaiCities.isEmpty()
                )
        );

        cityReport.printCitiesByCountryReport(
                thaiCities,
                "Thailand"
        );
    }

    /**
     * Tests retrieval of cities belonging
     * to the selected district.
     */
    @Test
    void testGetCitiesByDistrict() {

        List<City> bangkokCities =
                cityReport.getCitiesByDistrict(
                        "Bangkok"
                );

        assertAll(
                "Cities by district",

                () -> assertNotNull(
                        bangkokCities
                ),

                () -> assertFalse(
                        bangkokCities.isEmpty(),
                        "Bangkok district cities should not be empty."
                ),

                () -> {
                    for (City city : bangkokCities) {
                        assertEquals(
                                "Bangkok",
                                city.getDistrict()
                        );
                    }
                },

                () -> assertPopulationDescending(
                        bangkokCities
                )
        );
    }

    /**
     * Tests generation of the city report
     * for the selected district.
     */
    @Test
    void testPrintCitiesByDistrictReport() {

        List<City> bangkokCities =
                cityReport.getCitiesByDistrict(
                        "Bangkok"
                );

        assertAll(
                "Print cities by district",

                () -> assertNotNull(
                        bangkokCities
                ),

                () -> assertFalse(
                        bangkokCities.isEmpty()
                )
        );

        cityReport.printCitiesByDistrictReport(
                bangkokCities,
                "Bangkok"
        );
    }

    /**
     * Tests retrieval and ordering of the
     * requested Top N cities worldwide.
     */
    @Test
    void testGetTopNCities() {

        List<City> topCities =
                cityReport.getTopNCities(5);

        assertAll(
                "Top N cities",

                () -> assertNotNull(
                        topCities
                ),

                () -> assertEquals(
                        5,
                        topCities.size()
                ),

                () -> assertPopulationDescending(
                        topCities
                )
        );
    }

    /**
     * Tests generation of the Top N
     * worldwide city report.
     */
    @Test
    void testPrintTopNCitiesWorldwideReport() {

        List<City> topCities =
                cityReport.getTopNCities(5);

        assertAll(
                "Print top worldwide cities",

                () -> assertNotNull(
                        topCities
                ),

                () -> assertEquals(
                        5,
                        topCities.size()
                )
        );

        System.out.println(
                "\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 Worldwide) ---"
        );

        cityReport.printTopNCitiesWorldwideReport(
                topCities,
                5
        );
    }

    /**
     * Tests retrieval and ordering of the
     * Top N cities within a continent.
     */
    @Test
    void testGetTopNCitiesByContinent() {

        List<City> topAsianCities =
                cityReport
                        .getTopNCitiesByContinent(
                                "Asia",
                                5
                        );

        assertAll(
                "Top cities by continent",

                () -> assertNotNull(
                        topAsianCities
                ),

                () -> assertFalse(
                        topAsianCities.isEmpty(),
                        "Top Asian cities list should not be empty."
                ),

                () -> assertTrue(
                        topAsianCities.size() <= 5
                ),

                () -> assertPopulationDescending(
                        topAsianCities
                )
        );
    }

    /**
     * Tests generation of the Top N
     * continent city report.
     */
    @Test
    void testPrintTopNCitiesByContinentReport() {

        List<City> topAsianCities =
                cityReport
                        .getTopNCitiesByContinent(
                                "Asia",
                                5
                        );

        assertAll(
                "Print top cities by continent",

                () -> assertNotNull(
                        topAsianCities
                ),

                () -> assertFalse(
                        topAsianCities.isEmpty()
                )
        );

        System.out.println(
                "\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 in Asia) ---"
        );

        cityReport
                .printTopNCitiesByContinentReport(
                        topAsianCities,
                        "Asia",
                        5
                );
    }

    /**
     * Tests retrieval and ordering of the
     * Top N cities within a region.
     */
    @Test
    void testGetTopNCitiesByRegion() {

        List<City> topRegionCities =
                cityReport
                        .getTopNCitiesByRegion(
                                "Southeast Asia",
                                5
                        );

        assertAll(
                "Top cities by region",

                () -> assertNotNull(
                        topRegionCities
                ),

                () -> assertFalse(
                        topRegionCities.isEmpty(),
                        "Top region cities list should not be empty."
                ),

                () -> assertTrue(
                        topRegionCities.size() <= 5
                ),

                () -> assertPopulationDescending(
                        topRegionCities
                )
        );
    }

    /**
     * Tests generation of the Top N
     * regional city report.
     */
    @Test
    void testPrintTopNCitiesByRegionReport() {

        List<City> topRegionCities =
                cityReport
                        .getTopNCitiesByRegion(
                                "Southeast Asia",
                                5
                        );

        assertAll(
                "Print top cities by region",

                () -> assertNotNull(
                        topRegionCities
                ),

                () -> assertFalse(
                        topRegionCities.isEmpty()
                )
        );

        System.out.println(
                "\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 in Southeast Asia) ---"
        );

        cityReport
                .printTopNCitiesByRegionReport(
                        topRegionCities,
                        "Southeast Asia",
                        5
                );
    }

    /**
     * Tests retrieval and ordering of the
     * Top N cities within a country.
     */
    @Test
    void testGetTopNCitiesByCountry() {

        List<City> topThaiCities =
                cityReport
                        .getTopNCitiesByCountry(
                                "Thailand",
                                5
                        );

        assertAll(
                "Top cities by country",

                () -> assertNotNull(
                        topThaiCities
                ),

                () -> assertFalse(
                        topThaiCities.isEmpty(),
                        "Top Thailand cities list should not be empty."
                ),

                () -> assertTrue(
                        topThaiCities.size() <= 5
                ),

                () -> {
                    for (City city : topThaiCities) {
                        assertEquals(
                                "Thailand",
                                city.getCountry()
                        );
                    }
                },

                () -> assertPopulationDescending(
                        topThaiCities
                )
        );
    }

    /**
     * Tests generation of the Top N
     * country city report.
     */
    @Test
    void testPrintTopNCitiesByCountryReport() {

        List<City> topThaiCities =
                cityReport
                        .getTopNCitiesByCountry(
                                "Thailand",
                                5
                        );

        assertAll(
                "Print top cities by country",

                () -> assertNotNull(
                        topThaiCities
                ),

                () -> assertFalse(
                        topThaiCities.isEmpty()
                )
        );

        System.out.println(
                "\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 in Thailand) ---"
        );

        cityReport
                .printTopNCitiesByCountryReport(
                        topThaiCities,
                        "Thailand",
                        5
                );
    }

    /**
     * Tests retrieval and ordering of the
     * Top N cities within a district.
     */
    @Test
    void testGetTopNCitiesByDistrict() {

        List<City> topBangkokCities =
                cityReport
                        .getTopNCitiesByDistrict(
                                "Bangkok",
                                5
                        );

        assertAll(
                "Top cities by district",

                () -> assertNotNull(
                        topBangkokCities
                ),

                () -> assertFalse(
                        topBangkokCities.isEmpty(),
                        "Top Bangkok district cities list should not be empty."
                ),

                () -> assertTrue(
                        topBangkokCities.size() <= 5
                ),

                () -> {
                    for (City city : topBangkokCities) {
                        assertEquals(
                                "Bangkok",
                                city.getDistrict()
                        );
                    }
                },

                () -> assertPopulationDescending(
                        topBangkokCities
                )
        );
    }

    /**
     * Tests generation of the Top N
     * district city report.
     */
    @Test
    void testPrintTopNCitiesByDistrictReport() {

        List<City> topBangkokCities =
                cityReport
                        .getTopNCitiesByDistrict(
                                "Bangkok",
                                5
                        );

        assertAll(
                "Print top cities by district",

                () -> assertNotNull(
                        topBangkokCities
                ),

                () -> assertFalse(
                        topBangkokCities.isEmpty()
                )
        );

        System.out.println(
                "\n--- INTEGRATION TEST SAMPLE OUTPUT (Top 5 in Bangkok) ---"
        );

        cityReport
                .printTopNCitiesByDistrictReport(
                        topBangkokCities,
                        "Bangkok",
                        5
                );
    }

    /**
     * Tests the null-continent branch of
     * continent city report generation.
     */
    @Test
    void testGenerateContinentCityReportWithNullContinent() {

        assertDoesNotThrow(
                () -> cityReport
                        .generateContinentCityReport(null)
        );
    }

    /**
     * Tests SQL exception handling while
     * retrieving all cities.
     */
    @Test
    void testGetAllCitiesSortedByPopulationSQLException() throws SQLException {

        CityReport failingReport =
                new CityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getAllCitiesSortedByPopulation()
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for all cities retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling while
     * retrieving cities by continent.
     */
    @Test
    void testGetCitiesByContinentSQLException() throws SQLException {

        CityReport failingReport =
                new CityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getCitiesByContinent(
                                "Asia"
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for continent city retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling while
     * retrieving cities by region.
     */
    @Test
    void testGetCitiesByRegionSQLException() throws SQLException {

        CityReport failingReport =
                new CityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getCitiesByRegion(
                                "Southeast Asia"
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for region city retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling while
     * retrieving cities by country.
     */
    @Test
    void testGetCitiesByCountrySQLException() throws SQLException {

        CityReport failingReport =
                new CityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getCitiesByCountry(
                                "Thailand"
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for country city retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling while
     * retrieving cities by district.
     */
    @Test
    void testGetCitiesByDistrictSQLException() throws SQLException {

        CityReport failingReport =
                new CityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getCitiesByDistrict(
                                "Bangkok"
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for district city retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling during
     * Top N worldwide city retrieval.
     */
    @Test
    void testGetTopNCitiesSQLException() throws SQLException {

        CityReport failingReport =
                new CityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getTopNCities(5)
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for Top N worldwide city retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling during
     * Top N continent city retrieval.
     */
    @Test
    void testGetTopNCitiesByContinentSQLException() throws SQLException {

        CityReport failingReport =
                new CityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getTopNCitiesByContinent(
                                "Asia",
                                5
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for Top N continent city retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling during
     * Top N regional city retrieval.
     */
    @Test
    void testGetTopNCitiesByRegionSQLException() throws SQLException {

        CityReport failingReport =
                new CityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getTopNCitiesByRegion(
                                "Southeast Asia",
                                5
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for Top N region city retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling during
     * Top N country city retrieval.
     */
    @Test
    void testGetTopNCitiesByCountrySQLException() throws SQLException {

        CityReport failingReport =
                new CityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getTopNCitiesByCountry(
                                "Thailand",
                                5
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for Top N country city retrieval Passed."
        );
    }

    /**
     * Tests exception handling during
     * Top N district city retrieval.
     */
    @Test
    void testGetTopNCitiesByDistrictException() throws SQLException {

        CityReport failingReport =
                new CityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getTopNCitiesByDistrict(
                                "Bangkok",
                                5
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles exception for Top N district city retrieval Passed."
        );
    }

    /**
     * Closes the database connection after all
     * CityReport integration tests have completed.
     */
    @AfterAll
    static void tearDown() {

        if (con != null) {

            try {

                con.close();

                System.out.println(
                        "Database connection closed."
                );

            } catch (SQLException e) {

                System.out.println(
                        "Error closing database connection: "
                                + e.getMessage()
                );
            }
        }
    }
}