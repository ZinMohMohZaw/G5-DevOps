package com.napier.sem.reports;

import com.napier.sem.models.Population;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

import java.lang.reflect.Proxy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for PopulationReport business logic and null safety handling.
 */
class PopulationReportTest {
    private PopulationReport report;

    @BeforeEach
    void setUp() {
        // Pass null connection to test error handling and fallback branches
        report = new PopulationReport(null);
    }

    // =========================================================================
    // US26: World Population & Validation Tests
    // =========================================================================
    /**
     * Tests that getWorldPopulation returns null and handles a null database connection gracefully.
     */
    @Test
    void testGetWorldPopulationNullConnection() {
        Population result = report.getWorldPopulation();
        assertNull(result);
    }
    /**
     * Tests world population data validation rules for null or invalid population objects.
     */
    @Test
    void testValidateWorldPopulationEdgeCases() {
        // Test null object
        assertFalse(report.validateWorldPopulation(null));

        // Test missing or blank name
        assertFalse(report.validateWorldPopulation(new Population(null, 5000000)));
        assertFalse(report.validateWorldPopulation(new Population("", 5000000)));
        assertFalse(report.validateWorldPopulation(new Population("   ", 5000000)));

        // Test invalid population values (<= 0)
        assertFalse(report.validateWorldPopulation(new Population("World", 0)));
        assertFalse(report.validateWorldPopulation(new Population("World", -100)));

        // Test valid population object
        assertTrue(report.validateWorldPopulation(new Population("World", 8000000000L)));
    }
    /**
     * Tests printing world population report safely with invalid or null data inputs.
     */
    @Test
    void testPrintWorldPopulationReportBranches() {
        // Invalid inputs triggering guard clauses
        report.printWorldPopulationReport(null);
        report.printWorldPopulationReport(new Population("", 1000));
        report.printWorldPopulationReport(new Population("World", 0));

        // Valid input triggering report output
        report.printWorldPopulationReport(new Population("World", 8000000000L));
    }

    // =========================================================================
    // US27: Continent Population Tests
    // =========================================================================
    /**
     * Tests getting population by continent with null or blank parameters.
     */
    @Test
    void testGetPopulationByContinentInvalidInput() {
        assertEquals(0, report.getPopulationByContinent(null).getTotalPopulation());
        assertEquals(0, report.getPopulationByContinent("").getTotalPopulation());
        assertEquals(0, report.getPopulationByContinent("   ").getTotalPopulation());
    }
    /**
     * Tests printing continent population report handling null object references safely.
     */
    @Test
    void testPrintPopulationByContinentReportBranches() {
        report.printPopulationByContinentReport(null);
        report.printPopulationByContinentReport(new Population("Asia", 4000000000L));
    }

    // =========================================================================
    // US28: Region Population Tests
    // =========================================================================
    /**
     * Tests getting population by region with blank inputs.
     */
    @Test
    void testGetPopulationByRegionInvalidInput() {
        assertEquals(0, report.getPopulationByRegion(null).getTotalPopulation());
        assertEquals(0, report.getPopulationByRegion("").getTotalPopulation());
        assertEquals(0, report.getPopulationByRegion("   ").getTotalPopulation());
    }
    /**
     * Tests printing region population report handling null objects safely.
     */
    @Test
    void testPrintPopulationByRegionReportBranches() {
        report.printPopulationByRegionReport(null);
        report.printPopulationByRegionReport(new Population("Southeast Asia", 650000000L));
    }

    // =========================================================================
    // US29: Country Population Tests
    // =========================================================================
    /**
     * Tests getting population by country with null or blank parameters.
     */
    @Test
    void testGetPopulationByCountryInvalidInput() {
        assertEquals(0, report.getPopulationByCountry(null).getTotalPopulation());
        assertEquals(0, report.getPopulationByCountry("").getTotalPopulation());
        assertEquals(0, report.getPopulationByCountry("   ").getTotalPopulation());
    }
    /**
     * Tests printing country population report handling null objects safely.
     */
    @Test
    void testPrintPopulationByCountryReportBranches() {
        report.printPopulationByCountryReport(null);
        report.printPopulationByCountryReport(new Population("Thailand", 71000000L));
    }

    // =========================================================================
    // US23, US24, US25: City vs Non-City Population Reports
    // =========================================================================
    /**
     * Tests city population by continent when database connection is null.
     */
    @Test
    void testGetCityPopulationByContinentNullConnection() {
        assertTrue(report.getCityPopulationByContinent().isEmpty());
    }
    /**
     * Tests printing city population by continent report with empty or null list parameters.
     */
    @Test
    void testPrintCityPopulationByContinentReportBranches() {
        report.printCityPopulationByContinentReport(null);
        report.printCityPopulationByContinentReport(List.of());

        List<Population> list = new ArrayList<>();
        list.add(null); // Tests null element skip in loop
        list.add(new Population("Asia", 4000000000L, 1500000000L, 37.5, 2500000000L, 62.5));
        report.printCityPopulationByContinentReport(list);
    }
    /**
     * tests city population by region when database connection is null.
     */
    @Test
    void testGetCityPopulationByRegionNullConnection() {
        assertTrue(report.getCityPopulationByRegion().isEmpty());
    }
    /**
     * Tests printing city population by region report with empty or null list parameters.
     */
    @Test
    void testPrintCityPopulationByRegionReportBranches() {
        report.printCityPopulationByRegionReport(null);
        report.printCityPopulationByRegionReport(List.of());

        List<Population> list = new ArrayList<>();
        list.add(null);
        list.add(new Population("Southeast Asia", 650000000L, 300000000L, 46.15, 350000000L, 53.85));
        report.printCityPopulationByRegionReport(list);
    }
    /**
     * Tests city population by country when database connection is null.
     */
    @Test
    void testGetCityPopulationByCountryNullConnection() {
        assertTrue(report.getCityPopulationByCountry().isEmpty());
    }
    /**
     * Tests printing city population by country report with empty or null list parameters.
     */
    @Test
    void testPrintCityPopulationByCountryReportBranches() {
        report.printCityPopulationByCountryReport(null);
        report.printCityPopulationByCountryReport(List.of());

        List<Population> list = new ArrayList<>();
        list.add(null);
        list.add(new Population("Thailand", 71000000L, 10000000L, 14.08, 61000000L, 85.92));
        report.printCityPopulationByCountryReport(list);
    }

    // =========================================================================
    // US30: Population by District Tests
    // =========================================================================
    /**
     * Tests getting population by district with null or empty district strings.
     */
    @Test
    void testGetPopulationByDistrictInvalidInput() {
        assertNull(report.getPopulationByDistrict(null));
        assertNull(report.getPopulationByDistrict(""));
        assertNull(report.getPopulationByDistrict("   "));
    }

    /**
     * Tests printing district population report handling null objects safely.
     */
    @Test
    void testPrintPopulationByDistrictReportBranches() {
        report.printPopulationByDistrictReport(null);
        report.printPopulationByDistrictReport(new Population("Bangkok", 8000000L));
    }

    // =========================================================================
    // US31: Population by City Tests
    // =========================================================================
    /**
     * Tests getting population by city with invalid or missing parameters.
     */
    @Test
    void testGetPopulationByCityInvalidInput() {
        assertNull(report.getPopulationByCity(null, "Thailand"));
        assertNull(report.getPopulationByCity("Bangkok", null));
        assertNull(report.getPopulationByCity("", "Thailand"));
        assertNull(report.getPopulationByCity("Bangkok", ""));
        assertNull(report.getPopulationByCity("   ", "   "));
    }

    /**
     * Tests printing city population report handling null objects safely.
     */
    @Test
    void testPrintPopulationByCityReportBranches() {
        report.printPopulationByCityReport(null);
        report.printPopulationByCityReport(new Population("Bangkok", 8000000L));
    }
}