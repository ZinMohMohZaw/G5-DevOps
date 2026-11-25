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

class CountryReportIntegrationTest {

    private static Connection con;
    private static CountryReport countryReport;

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

    // Creates a closed database connection to test SQL exception handling.
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

    @Test
    void testGenerateCountryReport() {

        assertDoesNotThrow(
                () -> countryReport
                        .generateCountryReport(),
                "Country report generation should complete without throwing an exception."
        );
    }

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

    // Tests SQL exception handling for all country retrieval.
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

    // Tests that report generation handles an empty result after a database error.
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

    // Tests the countries == null branch in generateCountryReport.
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

    // Tests the null capital branch used by country report generation.
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

    // Tests the no-result branch when a capital city ID does not exist.
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

    // Tests SQL exception handling for capital city name lookup.
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

    // Tests SQL exception handling for continent country retrieval.
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

    // Tests SQL exception handling for region country retrieval.
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

    // Tests SQL exception handling for Top N worldwide country retrieval.
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

    // Tests SQL exception handling for Top N continent country retrieval.
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

    // Tests SQL exception handling for Top N region country retrieval.
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