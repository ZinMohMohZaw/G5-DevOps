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

    @Test
    void testGetWorldPopulationNullConnection() {
        Population result = report.getWorldPopulation();
        assertNull(result);
    }

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

    @Test
    void testGetPopulationByContinentInvalidInput() {
        assertEquals(0, report.getPopulationByContinent(null).getTotalPopulation());
        assertEquals(0, report.getPopulationByContinent("").getTotalPopulation());
        assertEquals(0, report.getPopulationByContinent("   ").getTotalPopulation());
    }

    @Test
    void testPrintPopulationByContinentReportBranches() {
        report.printPopulationByContinentReport(null);
        report.printPopulationByContinentReport(new Population("Asia", 4000000000L));
    }

    // =========================================================================
    // US28: Region Population Tests
    // =========================================================================

    @Test
    void testGetPopulationByRegionInvalidInput() {
        assertEquals(0, report.getPopulationByRegion(null).getTotalPopulation());
        assertEquals(0, report.getPopulationByRegion("").getTotalPopulation());
        assertEquals(0, report.getPopulationByRegion("   ").getTotalPopulation());
    }

    @Test
    void testPrintPopulationByRegionReportBranches() {
        report.printPopulationByRegionReport(null);
        report.printPopulationByRegionReport(new Population("Southeast Asia", 650000000L));
    }

    // =========================================================================
    // US29: Country Population Tests
    // =========================================================================

    @Test
    void testGetPopulationByCountryInvalidInput() {
        assertEquals(0, report.getPopulationByCountry(null).getTotalPopulation());
        assertEquals(0, report.getPopulationByCountry("").getTotalPopulation());
        assertEquals(0, report.getPopulationByCountry("   ").getTotalPopulation());
    }

    @Test
    void testPrintPopulationByCountryReportBranches() {
        report.printPopulationByCountryReport(null);
        report.printPopulationByCountryReport(new Population("Thailand", 71000000L));
    }

    // =========================================================================
    // US23, US24, US25: City vs Non-City Population Reports
    // =========================================================================

    @Test
    void testGetCityPopulationByContinentNullConnection() {
        assertTrue(report.getCityPopulationByContinent().isEmpty());
    }

    @Test
    void testPrintCityPopulationByContinentReportBranches() {
        report.printCityPopulationByContinentReport(null);
        report.printCityPopulationByContinentReport(List.of());

        List<Population> list = new ArrayList<>();
        list.add(null); // Tests null element skip in loop
        list.add(new Population("Asia", 4000000000L, 1500000000L, 37.5, 2500000000L, 62.5));
        report.printCityPopulationByContinentReport(list);
    }

    @Test
    void testGetCityPopulationByRegionNullConnection() {
        assertTrue(report.getCityPopulationByRegion().isEmpty());
    }

    @Test
    void testPrintCityPopulationByRegionReportBranches() {
        report.printCityPopulationByRegionReport(null);
        report.printCityPopulationByRegionReport(List.of());

        List<Population> list = new ArrayList<>();
        list.add(null);
        list.add(new Population("Southeast Asia", 650000000L, 300000000L, 46.15, 350000000L, 53.85));
        report.printCityPopulationByRegionReport(list);
    }

    @Test
    void testGetCityPopulationByCountryNullConnection() {
        assertTrue(report.getCityPopulationByCountry().isEmpty());
    }

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

    @Test
    void testGetPopulationByDistrictInvalidInput() {
        assertNull(report.getPopulationByDistrict(null));
        assertNull(report.getPopulationByDistrict(""));
        assertNull(report.getPopulationByDistrict("   "));
    }

    @Test
    void testPrintPopulationByDistrictReportBranches() {
        report.printPopulationByDistrictReport(null);
        report.printPopulationByDistrictReport(new Population("Bangkok", 8000000L));
    }

    // =========================================================================
    // US31: Population by City Tests
    // =========================================================================

    @Test
    void testGetPopulationByCityInvalidInput() {
        assertNull(report.getPopulationByCity(null, "Thailand"));
        assertNull(report.getPopulationByCity("Bangkok", null));
        assertNull(report.getPopulationByCity("", "Thailand"));
        assertNull(report.getPopulationByCity("Bangkok", ""));
        assertNull(report.getPopulationByCity("   ", "   "));
    }

    @Test
    void testPrintPopulationByCityReportBranches() {
        report.printPopulationByCityReport(null);
        report.printPopulationByCityReport(new Population("Bangkok", 8000000L));
    }
}