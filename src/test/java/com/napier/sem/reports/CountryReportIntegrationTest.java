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