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

/**
 * Integration tests for CapitalCityReport.
 * Covers database retrieval, population sorting, error handling and report generation.
 */
public class CapitalCityReportIntegrationTest {

    private static Connection connection;
    private static CapitalCityReport report;

    @BeforeAll
    static void init() throws SQLException {
        // Connect to World DB before running integration tests.
        String url = System.getenv().getOrDefault(
                "DB_URL",
                "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false"
        );

        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "example");

        connection = DriverManager.getConnection(url, user, password);
        report = new CapitalCityReport(connection);

        System.out.println("Database connection established Passed.");
    }

    @AfterAll
    static void tearDown() throws SQLException {
        // Close DB connection after all integration tests.
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Database connection closed Passed.");
        }
    }

    // Creates a closed database connection to test SQL exception handling.
    private Connection createClosedConnection() throws SQLException {
        String url = System.getenv().getOrDefault(
                "DB_URL",
                "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false"
        );

        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv().getOrDefault("DB_PASSWORD", "example");

        Connection closedConnection = DriverManager.getConnection(url, user, password);
        closedConnection.close();

        return closedConnection;
    }

    // Checks that all capital cities are retrieved from database.
    @Test
    void testGetAllCapitalCitiesByPopulation() {
        List<CapitalCity> capitals = report.getAllCapitalCitiesByPopulation();

        assertNotNull(capitals);
        assertFalse(capitals.isEmpty());

        System.out.println("Retrieves all capital cities Passed.");
    }

    // Checks that capital cities are ordered by population, highest first.
    @Test
    void testCapitalCitiesSortedByPopulation() {
        List<CapitalCity> capitals = report.getAllCapitalCitiesByPopulation();

        assertNotNull(capitals);
        assertFalse(capitals.isEmpty());

        for (int i = 1; i < capitals.size(); i++) {
            assertTrue(
                    capitals.get(i - 1).getPopulation() >= capitals.get(i).getPopulation(),
                    "Capital cities should be sorted by descending population."
            );
        }

        System.out.println("Capital cities sorted by population Passed.");
    }

    // Checks that capital cities belonging to Asia are retrieved.
    @Test
    void testGetCapitalCitiesByContinent() {
        List<CapitalCity> capitals = report.getCapitalCitiesByContinent("Asia");

        assertNotNull(capitals);
        assertFalse(capitals.isEmpty());

        assertTrue(capitals.stream().anyMatch(capital -> capital.getName().equals("Tokyo")),
                "Tokyo should be included in the Asian capital city results.");

        System.out.println("Retrieves capital cities by continent Passed.");
    }

    // Checks that continent capital cities are sorted by population, highest first.
    @Test
    void testCapitalCitiesByContinentSortedByPopulation() {
        List<CapitalCity> capitals = report.getCapitalCitiesByContinent("Asia");

        assertNotNull(capitals);
        assertFalse(capitals.isEmpty());

        for (int i = 1; i < capitals.size(); i++) {
            assertTrue(capitals.get(i - 1).getPopulation() >= capitals.get(i).getPopulation());
        }

        System.out.println("Continent capital cities sorted by population Passed.");
    }

    // Checks that continent report is generated using DB results.
    @Test
    void testPrintCapitalCitiesByContinent() {
        assertDoesNotThrow(() -> report.printCapitalCitiesByContinent("Asia"));

        System.out.println("Generates capital city report by continent Passed.");
    }

    // Checks that capital cities belonging to Southeast Asia are retrieved.
    @Test
    void testGetCapitalCitiesByRegion() {
        List<CapitalCity> capitals = report.getCapitalCitiesByRegion("Southeast Asia");

        assertNotNull(capitals);
        assertFalse(capitals.isEmpty());

        assertTrue(capitals.stream().anyMatch(capital -> capital.getName().equals("Bangkok")),
                "Bangkok should be included in the Southeast Asian capital city results.");

        System.out.println("Retrieves capital cities by region Passed.");
    }

    // Checks that regional capital cities are sorted by population, highest first.
    @Test
    void testCapitalCitiesByRegionSortedByPopulation() {
        List<CapitalCity> capitals = report.getCapitalCitiesByRegion("Southeast Asia");

        assertNotNull(capitals);
        assertFalse(capitals.isEmpty());

        for (int i = 1; i < capitals.size(); i++) {
            assertTrue(capitals.get(i - 1).getPopulation() >= capitals.get(i).getPopulation());
        }

        System.out.println("Region capital cities sorted by population Passed.");
    }

    // Checks that regional report is generated using DB results.
    @Test
    void testPrintCapitalCitiesByRegionReport() {
        List<CapitalCity> capitals = report.getCapitalCitiesByRegion("Southeast Asia");

        assertDoesNotThrow(() -> report.printCapitalCitiesByRegionReport(capitals,
                "Southeast Asia"));

        System.out.println("Generates capital city report by region Passed.");
    }

    // Tests SQL exception handling for worldwide capital city retrieval.
    @Test
    void testGetAllCapitalCitiesByPopulationSQLException() throws SQLException {
        CapitalCityReport failingReport = new CapitalCityReport(createClosedConnection());
        assertTrue(failingReport.getAllCapitalCitiesByPopulation().isEmpty());

        System.out.println("Handles SQL exception for worldwide capital cities Passed.");
    }

    // Tests SQL exception handling for continent capital city retrieval.
    @Test
    void testGetCapitalCitiesByContinentSQLException() throws SQLException {
        CapitalCityReport failingReport = new CapitalCityReport(createClosedConnection());
        assertTrue(failingReport.getCapitalCitiesByContinent("Asia").isEmpty());

        System.out.println("Handles SQL exception for continent retrieval Passed.");
    }

    // Tests SQL exception handling for regional capital city retrieval.
    @Test
    void testGetCapitalCitiesByRegionSQLException() throws SQLException {
        CapitalCityReport failingReport = new CapitalCityReport(createClosedConnection());
        assertTrue(failingReport.getCapitalCitiesByRegion("Southeast Asia").isEmpty());

        System.out.println("Handles SQL exception for region retrieval Passed.");
    }

    // Tests SQL exception handling for Top N worldwide retrieval.
    @Test
    void testGetTopNCapitalCitiesWorldwideSQLException() throws SQLException {
        CapitalCityReport failingReport = new CapitalCityReport(createClosedConnection());
        assertTrue(failingReport.getTopNCapitalCitiesWorldwide(5).isEmpty());

        System.out.println("Handles SQL exception for Top N worldwide retrieval Passed.");
    }

    // Tests SQL exception handling for Top N continent retrieval.
    @Test
    void testGetTopNCapitalCitiesByContinentSQLException() throws SQLException {
        CapitalCityReport failingReport = new CapitalCityReport(createClosedConnection());
        assertTrue(failingReport.getTopNCapitalCitiesByContinent("Asia", 5).isEmpty());

        System.out.println("Handles SQL exception for Top N continent retrieval Passed.");
    }

    // Tests SQL exception handling for Top N capital cities by region.
    @Test
    void testGetTopNCapitalCitiesByRegionSQLException() throws SQLException {
        CapitalCityReport failingReport = new CapitalCityReport(createClosedConnection());
        assertTrue(failingReport.getTopNCapitalCitiesByRegion("Southeast Asia", 5).isEmpty());

        System.out.println("Handles SQL exception for Top N region retrieval Passed.");
    }
}
