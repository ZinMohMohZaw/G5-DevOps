package com.napier.sem.reports;

import com.napier.sem.models.CapitalCity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CapitalCityReport.
 * Covers capital city retrieval and report generation.
 */
class CapitalCityReportTest {
    private static CapitalCityReport report;

    @BeforeAll
    static void init() {
        // Initialize the report without a database connection for isolated tests.
        report = new CapitalCityReport(null);
    }

    // Test behavior when DB Connection is null.
    @Test
    void testGetAllCapitalCitiesByPopulationNullConnection() {
        List<CapitalCity> capitals = report.getAllCapitalCitiesByPopulation();
        assertNotNull(capitals);
        assertTrue(capitals.isEmpty());

        System.out.println("Handles null DB connection for worldwide capital cities Passed.");
    }

    // Test an empty list produces expected report output without errors.
    @Test
    void testPrintCapitalCitiesWithEmptyList() {
        assertDoesNotThrow(() -> report.printCapitalCities(new ArrayList<>()));

        System.out.println("Handles empty capital city list Passed.");
    }

    // Test that null list is handled without throwing exception.
    @Test
    void testPrintCapitalCitiesWithNullList() {
        assertDoesNotThrow(() -> report.printCapitalCities(null));

        System.out.println("Handles null capital city list Passed.");
    }

    // Test that valid capital city records can be printed.
    @Test
    void testPrintCapitalCitiesWithValidList() {
        List<CapitalCity> capitals = new ArrayList<>();

        capitals.add(new CapitalCity("Tokyo", "Japan", 13929286));
        capitals.add(new CapitalCity("Bangkok", "Thailand", 10539415));

        assertDoesNotThrow(() -> report.printCapitalCities(capitals));

        System.out.println("[Prints a list of capital cities Successfully Passed.");
    }

    // Test behavior when retrieving capital cities by continent without a database connection.
    @Test
    void testGetCapitalCitiesByContinentNullConnection() {
        List<CapitalCity> capitals = report.getCapitalCitiesByContinent("Asia");

        assertNotNull(capitals);
        assertTrue(capitals.isEmpty());

        System.out.println("Handles null database connection for continent retrieval Passed.");
    }

    // Test that null, empty and blank continent inputs are handled safely.
    @Test
    void testGetCapitalCitiesByContinentInvalidInput() {
        assertTrue(report.getCapitalCitiesByContinent(null).isEmpty());
        assertTrue(report.getCapitalCitiesByContinent("").isEmpty());
        assertTrue(report.getCapitalCitiesByContinent("   ").isEmpty());

        System.out.println("Handles invalid continent inputs Passed.");
    }

    // Tests that continent report method executes without errors.
    @Test
    void testPrintCapitalCitiesByContinent() {
        assertDoesNotThrow(() -> report.printCapitalCitiesByContinent("Asia"));

        System.out.println("Generates capital city report for a continent Passed.");
    }

    // Test behavior when retrieving capital cities by region without DB connection.
    @Test
    void testGetCapitalCitiesByRegionNullConnection() {
        List<CapitalCity> capitals = report.getCapitalCitiesByRegion("Southeast Asia");
        assertNotNull(capitals);
        assertTrue(capitals.isEmpty());

        System.out.println("Handles null DB connection for region retrieval Passed.");
    }

    // Test that null, empty and blank region inputs are handled safely.
    @Test
    void testGetCapitalCitiesByRegionInvalidInput() {
        assertTrue(report.getCapitalCitiesByRegion(null).isEmpty());
        assertTrue(report.getCapitalCitiesByRegion("").isEmpty());
        assertTrue(report.getCapitalCitiesByRegion("   ").isEmpty());

        System.out.println("Handles invalid region inputs Passed.");
    }

    // Test that valid capital city list can be printed in region report.
    @Test
    void testPrintCapitalCitiesByRegionReport() {
        List<CapitalCity> capitals = new ArrayList<>();

        capitals.add(new CapitalCity("Bangkok", "Thailand", 10539415));
        capitals.add(new CapitalCity("Hanoi", "Vietnam", 7784400));

        assertDoesNotThrow(() -> report.printCapitalCitiesByRegionReport(capitals, "Southeast Asia"));

        System.out.println("Generates regional capital city report Passed.");
    }

    // Test that empty list is handled by regional report method.
    @Test
    void testPrintCapitalCitiesByRegionReportWithEmptyList() {
        assertDoesNotThrow(() -> report.printCapitalCitiesByRegionReport(new ArrayList<>(), "Southeast Asia"));

        System.out.println("Handles empty list in regional report Passed.");
    }

    // Tests that null list is handled by regional report method.
    @Test
    void testPrintCapitalCitiesByRegionReportWithNullList() {
        assertDoesNotThrow(() -> report.printCapitalCitiesByRegionReport(null, "Southeast Asia"));

        System.out.println("Handles null list in regional report Passed.");
    }
}