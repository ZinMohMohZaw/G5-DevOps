package com.napier.sem.reports;

import com.napier.sem.models.CapitalCity;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapitalCityReportIntegrationTest {

    private static Connection connection;
    private static CapitalCityReport report;

    @BeforeAll
    static void init() throws SQLException {

        String url =
                System.getenv().getOrDefault(
                        "DB_URL",
                        "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false"
                );

        String user =
                System.getenv().getOrDefault(
                        "DB_USER",
                        "root"
                );

        String password =
                System.getenv().getOrDefault(
                        "DB_PASSWORD",
                        "example"
                );

        connection =
                DriverManager.getConnection(
                        url,
                        user,
                        password
                );

        report =
                new CapitalCityReport(connection);

        System.out.println(
                "Database connection established Passed."
        );
    }

    @AfterAll
    static void tearDown() throws SQLException {

        if (connection != null
                && !connection.isClosed()) {

            connection.close();

            System.out.println(
                    "Database connection closed Passed."
            );
        }
    }

    private Connection createClosedConnection()
            throws SQLException {

        String url =
                System.getenv().getOrDefault(
                        "DB_URL",
                        "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false"
                );

        String user =
                System.getenv().getOrDefault(
                        "DB_USER",
                        "root"
                );

        String password =
                System.getenv().getOrDefault(
                        "DB_PASSWORD",
                        "example"
                );

        Connection closedConnection =
                DriverManager.getConnection(
                        url,
                        user,
                        password
                );

        closedConnection.close();

        return closedConnection;
    }

    @Test
    void testGetAllCapitalCitiesByPopulation() {

        List<CapitalCity> capitals =
                report.getAllCapitalCitiesByPopulation();

        assertAll(
                "All capital cities",

                () -> assertNotNull(capitals),

                () -> assertFalse(
                        capitals.isEmpty()
                )
        );

        System.out.println(
                "Retrieves all capital cities Passed."
        );
    }

    @Test
    void testCapitalCitiesSortedByPopulation() {

        List<CapitalCity> capitals =
                report.getAllCapitalCitiesByPopulation();

        assertAll(
                "Capital cities population ordering",

                () -> assertNotNull(capitals),

                () -> assertFalse(
                        capitals.isEmpty()
                ),

                () -> {
                    for (int i = 1;
                         i < capitals.size();
                         i++) {

                        assertTrue(
                                capitals
                                        .get(i - 1)
                                        .getPopulation()
                                        >=
                                        capitals
                                                .get(i)
                                                .getPopulation(),
                                "Capital cities should be sorted by descending population."
                        );
                    }
                }
        );

        System.out.println(
                "Capital cities sorted by population Passed."
        );
    }

    @Test
    void testGetCapitalCitiesByContinent() {

        List<CapitalCity> capitals =
                report.getCapitalCitiesByContinent(
                        "Asia"
                );

        assertAll(
                "Capital cities by continent",

                () -> assertNotNull(capitals),

                () -> assertFalse(
                        capitals.isEmpty()
                ),

                () -> assertTrue(
                        capitals.stream()
                                .anyMatch(
                                        capital ->
                                                "Tokyo".equals(
                                                        capital.getName()
                                                )
                                ),
                        "Tokyo should be included in the Asian capital city results."
                )
        );

        System.out.println(
                "Retrieves capital cities by continent Passed."
        );
    }

    @Test
    void testCapitalCitiesByContinentSortedByPopulation() {

        List<CapitalCity> capitals =
                report.getCapitalCitiesByContinent(
                        "Asia"
                );

        assertAll(
                "Continent capital city ordering",

                () -> assertNotNull(capitals),

                () -> assertFalse(
                        capitals.isEmpty()
                ),

                () -> {
                    for (int i = 1;
                         i < capitals.size();
                         i++) {

                        assertTrue(
                                capitals
                                        .get(i - 1)
                                        .getPopulation()
                                        >=
                                        capitals
                                                .get(i)
                                                .getPopulation()
                        );
                    }
                }
        );

        System.out.println(
                "Continent capital cities sorted by population Passed."
        );
    }

    @Test
    void testPrintCapitalCitiesByContinent() {

        assertDoesNotThrow(
                () -> report
                        .printCapitalCitiesByContinent(
                                "Asia"
                        )
        );

        System.out.println(
                "Generates capital city report by continent Passed."
        );
    }

    @Test
    void testGetCapitalCitiesByRegion() {

        List<CapitalCity> capitals =
                report.getCapitalCitiesByRegion(
                        "Southeast Asia"
                );

        assertAll(
                "Capital cities by region",

                () -> assertNotNull(capitals),

                () -> assertFalse(
                        capitals.isEmpty()
                ),

                () -> assertTrue(
                        capitals.stream()
                                .anyMatch(
                                        capital ->
                                                "Bangkok".equals(
                                                        capital.getName()
                                                )
                                ),
                        "Bangkok should be included in the Southeast Asian capital city results."
                )
        );

        System.out.println(
                "Retrieves capital cities by region Passed."
        );
    }

    @Test
    void testCapitalCitiesByRegionSortedByPopulation() {

        List<CapitalCity> capitals =
                report.getCapitalCitiesByRegion(
                        "Southeast Asia"
                );

        assertAll(
                "Region capital city ordering",

                () -> assertNotNull(capitals),

                () -> assertFalse(
                        capitals.isEmpty()
                ),

                () -> {
                    for (int i = 1;
                         i < capitals.size();
                         i++) {

                        assertTrue(
                                capitals
                                        .get(i - 1)
                                        .getPopulation()
                                        >=
                                        capitals
                                                .get(i)
                                                .getPopulation()
                        );
                    }
                }
        );

        System.out.println(
                "Region capital cities sorted by population Passed."
        );
    }

    @Test
    void testPrintCapitalCitiesByRegionReport() {

        List<CapitalCity> capitals =
                report.getCapitalCitiesByRegion(
                        "Southeast Asia"
                );

        assertDoesNotThrow(
                () -> report
                        .printCapitalCitiesByRegionReport(
                                capitals,
                                "Southeast Asia"
                        )
        );

        System.out.println(
                "Generates capital city report by region Passed."
        );
    }

    @Test
    void testGetAllCapitalCitiesByPopulationSQLException()
            throws SQLException {

        CapitalCityReport failingReport =
                new CapitalCityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getAllCapitalCitiesByPopulation()
                        .isEmpty()
        );
    }

    @Test
    void testGetCapitalCitiesByContinentSQLException()
            throws SQLException {

        CapitalCityReport failingReport =
                new CapitalCityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getCapitalCitiesByContinent(
                                "Asia"
                        )
                        .isEmpty()
        );
    }

    @Test
    void testGetCapitalCitiesByRegionSQLException()
            throws SQLException {

        CapitalCityReport failingReport =
                new CapitalCityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getCapitalCitiesByRegion(
                                "Southeast Asia"
                        )
                        .isEmpty()
        );
    }

    @Test
    void testGetTopNCapitalCitiesWorldwideSQLException()
            throws SQLException {

        CapitalCityReport failingReport =
                new CapitalCityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getTopNCapitalCitiesWorldwide(5)
                        .isEmpty()
        );
    }

    @Test
    void testGetTopNCapitalCitiesByContinentSQLException()
            throws SQLException {

        CapitalCityReport failingReport =
                new CapitalCityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getTopNCapitalCitiesByContinent(
                                "Asia",
                                5
                        )
                        .isEmpty()
        );
    }

    @Test
    void testGetTopNCapitalCitiesByRegionSQLException()
            throws SQLException {

        CapitalCityReport failingReport =
                new CapitalCityReport(
                        createClosedConnection()
                );

        assertTrue(
                failingReport
                        .getTopNCapitalCitiesByRegion(
                                "Southeast Asia",
                                5
                        )
                        .isEmpty()
        );
    }
}