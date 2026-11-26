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

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the CountryReport class.
 * Verifies country retrieval, report generation,
 * branch behaviour and SQL exception handling.
 */
class CountryReportIntegrationTest {

    private static Connection con;
    private static CountryReport countryReport;

    /**
     * Establishes a connection to the World database and creates
     * the CountryReport instance before all integration tests.
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
             ++i) {

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
                }
            }
        }

        if (con == null) {

            fail(
                    "Could not establish database connection after multiple retries."
            );
        }

        countryReport =
                new CountryReport(con);
    }

    /**
     * Creates and immediately closes a database connection
     * for testing SQL exception handling.
     *
     * @return a closed database connection
     * @throws SQLException if the database connection cannot be created
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
     * Tests retrieval of all countries from the database
     * in population order.
     */
    @Test
    void testGetAllCountriesSorted() {

        List<Country> countries =
                countryReport
                        .getAllCountriesSorted();

        assertAll(
                "All countries",

                () -> assertNotNull(
                        countries,
                        "Countries list should not be null."
                ),

                () -> assertFalse(
                        countries.isEmpty(),
                        "Countries list should not be empty from the database."
                )
        );
    }

    /**
     * Tests generation of the complete country report
     * using data from the World database.
     */
    @Test
    void testGenerateCountryReport() {

        assertDoesNotThrow(
                () -> countryReport
                        .generateCountryReport(),
                "Country report generation should complete without throwing an exception."
        );
    }

    /**
     * Tests retrieval of countries belonging
     * to the selected continent.
     */
    @Test
    void testGetCountriesByContinent() {

        List<Country> asianCountries =
                countryReport
                        .getCountriesByContinent(
                                "Asia"
                        );

        assertAll(
                "Countries by continent",

                () -> assertNotNull(
                        asianCountries,
                        "Asian countries list should not be null."
                ),

                () -> assertFalse(
                        asianCountries.isEmpty(),
                        "Asian countries list should not be empty."
                ),

                () -> {
                    for (Country c : asianCountries) {

                        assertEquals(
                                "Asia",
                                c.getContinent(),
                                "Every returned country should belong to Asia."
                        );
                    }
                }
        );
    }

    /**
     * Tests generation of the country report
     * for the selected continent.
     */
    @Test
    void testPrintCountriesByContinentReport() {

        List<Country> asianCountries =
                countryReport
                        .getCountriesByContinent(
                                "Asia"
                        );

        assertAll(
                "Print countries by continent",

                () -> assertNotNull(
                        asianCountries,
                        "Asian countries list should not be null before printing."
                ),

                () -> assertFalse(
                        asianCountries.isEmpty(),
                        "Asian countries list should contain data before printing."
                ),

                () -> assertDoesNotThrow(
                        () -> countryReport
                                .printCountriesByContinentReport(
                                        asianCountries,
                                        "Asia"
                                ),
                        "Printing the continent report should not throw an exception."
                )
        );
    }

    /**
     * Tests retrieval of countries belonging
     * to the selected region.
     */
    @Test
    void testGetCountriesByRegion() {

        List<Country> regionCountries =
                countryReport
                        .getCountriesByRegion(
                                "Southeast Asia"
                        );

        assertAll(
                "Countries by region",

                () -> assertNotNull(
                        regionCountries,
                        "Region countries list should not be null."
                ),

                () -> assertFalse(
                        regionCountries.isEmpty(),
                        "Region countries list should not be empty."
                ),

                () -> {
                    for (Country c : regionCountries) {

                        assertEquals(
                                "Southeast Asia",
                                c.getRegion(),
                                "Every returned country should belong to Southeast Asia."
                        );
                    }
                }
        );
    }

    /**
     * Tests generation of the country report
     * for the selected region.
     */
    @Test
    void testPrintCountriesByRegion() {

        List<Country> regionCountries =
                new ArrayList<>(
                        countryReport
                                .getCountriesByRegion(
                                        "Southeast Asia"
                                )
                );

        assertAll(
                "Print countries by region",

                () -> assertNotNull(
                        regionCountries,
                        "Region countries list should not be null before printing."
                ),

                () -> assertFalse(
                        regionCountries.isEmpty(),
                        "Region countries list should contain data before printing."
                ),

                () -> assertDoesNotThrow(
                        () -> countryReport
                                .printCountriesByRegion(
                                        regionCountries,
                                        "Southeast Asia"
                                ),
                        "Printing countries by region should not throw an exception."
                )
        );
    }

    /**
     * Tests retrieval of the requested Top N
     * populated countries worldwide.
     */
    @Test
    void testGetTopNCountriesWorldwide() {

        List<Country> topCountries =
                countryReport
                        .getTopNCountriesWorldwide(5);

        assertAll(
                "Top worldwide countries",

                () -> assertNotNull(
                        topCountries,
                        "Top countries list should not be null."
                ),

                () -> assertEquals(
                        5,
                        topCountries.size(),
                        "Exactly five countries should be returned."
                )
        );
    }

    /**
     * Tests generation of the Top N
     * worldwide country report.
     */
    @Test
    void testPrintTopNCountriesWorldwideReport() {

        List<Country> topCountries =
                countryReport
                        .getTopNCountriesWorldwide(5);

        assertAll(
                "Print top worldwide countries",

                () -> assertNotNull(
                        topCountries,
                        "Top worldwide countries should not be null before printing."
                ),

                () -> assertEquals(
                        5,
                        topCountries.size(),
                        "Exactly five worldwide countries should be available for printing."
                ),

                () -> assertDoesNotThrow(
                        () -> countryReport
                                .printTopNCountriesWorldwideReport(
                                        topCountries,
                                        5
                                ),
                        "Printing the top worldwide countries should not throw an exception."
                )
        );
    }

    /**
     * Tests retrieval of the Top N populated
     * countries within a continent.
     */
    @Test
    void testGetTopNCountriesByContinent() {

        List<Country> topAsian =
                countryReport
                        .getTopNCountriesByContinent(
                                "Asia",
                                5
                        );

        assertAll(
                "Top countries by continent",

                () -> assertNotNull(
                        topAsian
                ),

                () -> assertEquals(
                        5,
                        topAsian.size()
                ),

                () -> {
                    for (Country c : topAsian) {

                        assertEquals(
                                "Asia",
                                c.getContinent()
                        );
                    }
                }
        );
    }

    /**
     * Tests generation of the Top N
     * continent country report.
     */
    @Test
    void testPrintTopNCountriesByContinentReport() {

        List<Country> topAsian =
                countryReport
                        .getTopNCountriesByContinent(
                                "Asia",
                                5
                        );

        assertAll(
                "Print top countries by continent",

                () -> assertNotNull(
                        topAsian
                ),

                () -> assertEquals(
                        5,
                        topAsian.size()
                ),

                () -> assertDoesNotThrow(
                        () -> countryReport
                                .printTopNCountriesByContinentReport(
                                        topAsian,
                                        "Asia",
                                        5
                                )
                )
        );
    }

    /**
     * Tests retrieval of the Top N populated
     * countries within a region.
     */
    @Test
    void testGetTopNCountriesByRegion() {

        List<Country> topRegion =
                countryReport
                        .getTopNCountriesByRegion(
                                "Southeast Asia",
                                5
                        );

        assertAll(
                "Top countries by region",

                () -> assertNotNull(
                        topRegion
                ),

                () -> assertEquals(
                        5,
                        topRegion.size()
                ),

                () -> {
                    for (Country c : topRegion) {

                        assertEquals(
                                "Southeast Asia",
                                c.getRegion()
                        );
                    }
                }
        );
    }

    /**
     * Tests generation of the Top N
     * regional country report.
     */
    @Test
    void testPrintTopNCountriesByRegionReport() {

        List<Country> topRegion =
                countryReport
                        .getTopNCountriesByRegion(
                                "Southeast Asia",
                                5
                        );

        assertAll(
                "Print top countries by region",

                () -> assertNotNull(
                        topRegion
                ),

                () -> assertEquals(
                        5,
                        topRegion.size()
                ),

                () -> assertDoesNotThrow(
                        () -> countryReport
                                .printTopNCountriesByRegionReport(
                                        topRegion,
                                        "Southeast Asia",
                                        5
                                )
                )
        );
    }

    /**
     * Tests SQL exception handling while
     * retrieving all countries.
     */
    @Test
    void testGetAllCountriesSortedSQLException() throws SQLException {

        CountryReport failingReport =
                new CountryReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getAllCountriesSorted()
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for all countries retrieval Passed."
        );
    }

    /**
     * Tests that country report generation safely handles
     * a database error that produces an empty result.
     */
    @Test
    void testGenerateCountryReportWithClosedConnection() throws SQLException {

        CountryReport failingReport =
                new CountryReport(
                        createClosedConnection()
                );

        assertDoesNotThrow(
                () -> failingReport
                        .generateCountryReport()
        );

        System.out.println(
                "Country report handles empty result after database error Passed."
        );
    }

    /**
     * Tests the null-country-list branch
     * of country report generation.
     */
    @Test
    void testGenerateCountryReportWithNullCountries() {

        CountryReport nullResultReport =
                new CountryReport(con) {

                    @Override
                    public List<Country> getAllCountriesSorted() {
                        return null;
                    }
                };

        assertDoesNotThrow(
                () -> nullResultReport
                        .generateCountryReport()
        );

        System.out.println(
                "Country report handles null country list Passed."
        );
    }

    /**
     * Tests report generation when a country
     * does not have an assigned capital.
     */
    @Test
    void testGenerateCountryReportWithNullCapital() {

        CountryReport nullCapitalReport =
                new CountryReport(con) {

                    @Override
                    public List<Country> getAllCountriesSorted() {

                        return List.of(
                                new Country(
                                        "TST",
                                        "Test Country",
                                        "Asia",
                                        "Test Region",
                                        1,
                                        null
                                )
                        );
                    }
                };

        assertDoesNotThrow(
                () -> nullCapitalReport
                        .generateCountryReport()
        );

        System.out.println(
                "Null capital city branch Passed."
        );
    }

    /**
     * Tests report generation when the supplied
     * capital city ID does not exist.
     */
    @Test
    void testGenerateCountryReportWithUnknownCapital() {

        CountryReport unknownCapitalReport =
                new CountryReport(con) {

                    @Override
                    public List<Country> getAllCountriesSorted() {

                        return List.of(
                                new Country(
                                        "TST",
                                        "Test Country",
                                        "Asia",
                                        "Test Region",
                                        1,
                                        "999999"
                                )
                        );
                    }
                };

        assertDoesNotThrow(
                () -> unknownCapitalReport
                        .generateCountryReport()
        );

        System.out.println(
                "Unknown capital city branch Passed."
        );
    }

    /**
     * Tests SQL exception handling while
     * retrieving a country's capital city name.
     */
    @Test
    void testGenerateCountryReportCapitalLookupSQLException() throws SQLException {

        CountryReport failingCapitalReport =
                new CountryReport(
                        createClosedConnection()
                ) {

                    @Override
                    public List<Country> getAllCountriesSorted() {

                        return List.of(
                                new Country(
                                        "TST",
                                        "Test Country",
                                        "Asia",
                                        "Test Region",
                                        1,
                                        "1"
                                )
                        );
                    }
                };

        assertDoesNotThrow(
                () -> failingCapitalReport
                        .generateCountryReport()
        );

        System.out.println(
                "Handles SQL exception for capital city lookup Passed."
        );
    }

    /**
     * Tests SQL exception handling while
     * retrieving countries by continent.
     */
    @Test
    void testGetCountriesByContinentSQLException() throws SQLException {

        CountryReport failingReport =
                new CountryReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getCountriesByContinent(
                                "Asia"
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for continent country retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling while
     * retrieving countries by region.
     */
    @Test
    void testGetCountriesByRegionSQLException() throws SQLException {

        CountryReport failingReport =
                new CountryReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getCountriesByRegion(
                                "Southeast Asia"
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for region country retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling during
     * Top N worldwide country retrieval.
     */
    @Test
    void testGetTopNCountriesWorldwideSQLException() throws SQLException {

        CountryReport failingReport =
                new CountryReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getTopNCountriesWorldwide(5)
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for Top N worldwide country retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling during
     * Top N continent country retrieval.
     */
    @Test
    void testGetTopNCountriesByContinentSQLException() throws SQLException {

        CountryReport failingReport =
                new CountryReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getTopNCountriesByContinent(
                                "Asia",
                                5
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for Top N continent country retrieval Passed."
        );
    }

    /**
     * Tests SQL exception handling during
     * Top N regional country retrieval.
     */
    @Test
    void testGetTopNCountriesByRegionSQLException() throws SQLException {

        CountryReport failingReport =
                new CountryReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getTopNCountriesByRegion(
                                "Southeast Asia",
                                5
                        )
                        .isEmpty()
        );

        System.out.println(
                "Handles SQL exception for Top N region country retrieval Passed."
        );
    }

    /**
     * Closes the database connection after all
     * CountryReport integration tests have completed.
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