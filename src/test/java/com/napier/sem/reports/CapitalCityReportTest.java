
package com.napier.sem.reports;

import com.napier.sem.models.CapitalCity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CapitalCityReport.
 */
class CapitalCityReportTest {
    private static CapitalCityReport report;

    @BeforeAll
    static void init() {
        // Initialize report without DB connection.
        report = new CapitalCityReport(null);
    }

    // Creates sample capital city records for report printing tests.
    private List<CapitalCity> createSampleCapitalCities() {
        List<CapitalCity> capitals = new ArrayList<>();
        capitals.add(new CapitalCity("Tokyo", "Japan", 13929286));
        capitals.add(new CapitalCity("Bangkok", "Thailand", 10539415));
        capitals.add(new CapitalCity("Hanoi", "Vietnam", 7784400));
        return capitals;
    }

    // =========================================================================
    // US17: Worldwide Capital Cities
    // =========================================================================

    // Tests worldwide capital city retrieval with null DB connection.
    @Test
    void testGetAllCapitalCitiesByPopulationNullConnection() {
        List<CapitalCity> capitals = report.getAllCapitalCitiesByPopulation();

        assertNotNull(capitals);
        assertTrue(capitals.isEmpty());

        System.out.println("Handles null DB connection for worldwide capital cities Passed.");
    }

    // Tests printing an empty capital city list.
    @Test
    void testPrintCapitalCitiesWithEmptyList() {
        assertDoesNotThrow(() -> report.printCapitalCities(new ArrayList<>()));

        System.out.println("Handles empty capital city list Passed.");
    }

    // Tests printing a null capital city list.
    @Test
    void testPrintCapitalCitiesWithNullList() {
        assertDoesNotThrow(() -> report.printCapitalCities(null));

        System.out.println("Handles null capital city list Passed.");
    }

    // Tests printing valid capital city records.
    @Test
    void testPrintCapitalCitiesWithValidList() {
        assertDoesNotThrow(() -> report.printCapitalCities(createSampleCapitalCities()));

        System.out.println("Prints valid capital city records Passed.");
    }

    // =========================================================================
    // US18: Capital Cities by Continent
    // =========================================================================

    // Tests continent retrieval with a null database connection.
    @Test
    void testGetCapitalCitiesByContinentNullConnection() {
        List<CapitalCity> capitals = report.getCapitalCitiesByContinent("Asia");

        assertNotNull(capitals);
        assertTrue(capitals.isEmpty());

        System.out.println("Handles null DB connection for continent retrieval Passed.");
    }

    // Tests null, empty and blank continent inputs.
    @Test
    void testGetCapitalCitiesByContinentInvalidInput() {
        assertTrue(report.getCapitalCitiesByContinent(null).isEmpty());
        assertTrue(report.getCapitalCitiesByContinent("").isEmpty());
        assertTrue(report.getCapitalCitiesByContinent("   ").isEmpty());

        System.out.println("Handles invalid continent inputs Passed.");
    }

    // Tests the empty-result branch in the continent report.
    @Test
    void testPrintCapitalCitiesByContinent() {
        assertDoesNotThrow(() -> report.printCapitalCitiesByContinent("Asia"));

        System.out.println("Handles empty continent capital city report Passed.");
    }

    // Tests the null continent input in the continent report.
    @Test
    void testPrintCapitalCitiesByContinentNullInput() {
        assertDoesNotThrow(() -> report.printCapitalCitiesByContinent(null));

        System.out.println("Handles null continent in capital city report Passed.");
    }

    // =========================================================================
    // US19: Capital Cities by Region
    // =========================================================================

    // Tests region retrieval with a null database connection.
    @Test
    void testGetCapitalCitiesByRegionNullConnection() {
        List<CapitalCity> capitals = report.getCapitalCitiesByRegion("Southeast Asia");

        assertNotNull(capitals);
        assertTrue(capitals.isEmpty());

        System.out.println("Handles null DB connection for region retrieval Passed.");
    }

    // Tests null, empty and blank region inputs.
    @Test
    void testGetCapitalCitiesByRegionInvalidInput() {
        assertTrue(report.getCapitalCitiesByRegion(null).isEmpty());
        assertTrue(report.getCapitalCitiesByRegion("").isEmpty());
        assertTrue(report.getCapitalCitiesByRegion("   ").isEmpty());

        System.out.println("Handles invalid region inputs Passed.");
    }

    // Tests printing a regional report with valid records.
    @Test
    void testPrintCapitalCitiesByRegionReport() {
        assertDoesNotThrow(() -> report.printCapitalCitiesByRegionReport(createSampleCapitalCities(),
                "Southeast Asia"));

        System.out.println("Generates regional capital city report Passed.");
    }

    // Tests printing a regional report with an empty list.
    @Test
    void testPrintCapitalCitiesByRegionReportWithEmptyList() {
        assertDoesNotThrow(() -> report.printCapitalCitiesByRegionReport(new ArrayList<>(),
                "Southeast Asia"));

        System.out.println("Handles empty list in regional report Passed.");
    }

    // Tests printing a regional report with a null list.
    @Test
    void testPrintCapitalCitiesByRegionReportWithNullList() {
        assertDoesNotThrow(() -> report.printCapitalCitiesByRegionReport
                (null, "Southeast Asia"));

        System.out.println("Handles null list in regional report Passed.");
    }

    // =========================================================================
    // US20: Top N Capital Cities Worldwide
    // =========================================================================

    // Tests worldwide Top N retrieval with a null DB connection.
    @Test
    void testGetTopNCapitalCitiesWorldwideNullConnection() {
        assertTrue(report.getTopNCapitalCitiesWorldwide(5).isEmpty());

        System.out.println("Handles null DB connection for Top N worldwide retrieval Passed.");
    }

    // Tests printing a worldwide Top N report with valid records.
    @Test
    void testPrintTopNCapitalCitiesWorldwideReport() {
        assertDoesNotThrow(() -> report.printTopNCapitalCitiesWorldwideReport(createSampleCapitalCities(),
                3));

        System.out.println("Prints Top N worldwide capital city report Passed.");
    }

    // Tests empty-list branch in worldwide Top N report.
    @Test
    void testPrintTopNCapitalCitiesWorldwideReportEmptyList() {
        assertDoesNotThrow(() -> report.printTopNCapitalCitiesWorldwideReport(new ArrayList<>(),
                5));

        System.out.println("Handles empty list in Top N worldwide report Passed.");
    }

    // Tests null-list branch in the worldwide Top N report.
    @Test
    void testPrintTopNCapitalCitiesWorldwideReportNullList() {
        assertDoesNotThrow(() -> report.printTopNCapitalCitiesWorldwideReport(null,
                5));

        System.out.println("Handles null list in Top N worldwide report Passed.");
    }

    // =========================================================================
    // US21: Top N Capital Cities by Continent
    // =========================================================================

    // Tests Top N continent retrieval with a null DB connection.
    @Test
    void testGetTopNCapitalCitiesByContinentNullConnection() {
        assertTrue(report.getTopNCapitalCitiesByContinent("Asia", 5).isEmpty());

        System.out.println("Handles null DB connection for Top N continent retrieval Passed.");
    }

    // Tests printing a Top N continent report with valid records.
    @Test
    void testPrintTopNCapitalCitiesByContinentReport() {
        assertDoesNotThrow(() -> report.printTopNCapitalCitiesByContinentReport(createSampleCapitalCities(),
                "Asia", 3));

        System.out.println("Prints Top N continent capital city report Passed.");
    }

    // Tests the empty-list branch in the Top N continent report.
    @Test
    void testPrintTopNCapitalCitiesByContinentReportEmptyList() {
        assertDoesNotThrow(() -> report.printTopNCapitalCitiesByContinentReport(new ArrayList<>(),
                "Asia", 5));

        System.out.println("Handles empty list in Top N continent report Passed.");
    }

    // Tests the null-list and null-continent branches in the Top N continent report.
    @Test
    void testPrintTopNCapitalCitiesByContinentReportNullList() {
        assertDoesNotThrow(() -> report.printTopNCapitalCitiesByContinentReport
                (null, null, 5));

        System.out.println("Handles null list and continent in Top N continent report Passed.");
    }

    // =========================================================================
    // US22: Top N Capital Cities by Region
    // =========================================================================

    // Tests Top N region retrieval with a null database connection.
    @Test
    void testGetTopNCapitalCitiesByRegionNullConnection() {
        assertTrue(report.getTopNCapitalCitiesByRegion(
                "Southeast Asia", 5).isEmpty());

        System.out.println("Handles null DB connection for Top N region retrieval Passed.");
    }

    // Tests printing a Top N region report with valid records.
    @Test
    void testPrintTopNCapitalCitiesByRegionReport() {
        assertDoesNotThrow(() -> report.printTopNCapitalCitiesByRegionReport(createSampleCapitalCities(),
                "Southeast Asia", 3));

        System.out.println("Prints Top N region capital city report Passed.");
    }

    // Tests the empty-list branch in the Top N region report.
    @Test
    void testPrintTopNCapitalCitiesByRegionReportEmptyList() {
        assertDoesNotThrow(() -> report.printTopNCapitalCitiesByRegionReport(new ArrayList<>(),
                "Southeast Asia", 5));

        System.out.println("Handles empty list in Top N region report Passed.");
    }

    // Tests the null-list and null-region branches in the Top N region report.
    @Test
    void testPrintTopNCapitalCitiesByRegionReportNullList() {
        assertDoesNotThrow(() -> report.printTopNCapitalCitiesByRegionReport(
                null, null, 5));

        System.out.println("Handles null list and region in Top N region report Passed.");
    }
}