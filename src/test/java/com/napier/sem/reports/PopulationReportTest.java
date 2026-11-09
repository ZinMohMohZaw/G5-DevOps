package com.napier.sem.reports;

import com.napier.sem.models.Population;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    /**
     * Tests thatgetWorldPopulation returns null and handles a null database connection gracefully.
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
    void testValidateWorldPopulation() {
        assertFalse(report.validateWorldPopulation(null));
        assertFalse(report.validateWorldPopulation(new Population("", 1000)));
        assertFalse(report.validateWorldPopulation(new Population("World", 0)));
        assertTrue(report.validateWorldPopulation(new Population("World", 5000000)));
    }

    /**
     * Tests printing world population report safely with invalid or null data inputs.
     */
    @Test
    void testPrintWorldPopulationReport() {
        report.printWorldPopulationReport(null);
        report.printWorldPopulationReport(new Population("World", 8000000000L));
    }

    /**
     * Tests getting population by continent with null or blank parameters.
     */
    @Test
    void testGetPopulationByContinentInvalidInput() {
        Population popNull = report.getPopulationByContinent(null);
        assertNotNull(popNull);
        assertEquals(0, popNull.getTotalPopulation());

        Population popBlank = report.getPopulationByContinent("   ");
        assertNotNull(popBlank);
        assertEquals(0, popBlank.getTotalPopulation());
    }

    /**
     * Tests printing continent population report handling null object references safely.
     */
    @Test
    void testPrintPopulationByContinentReport() {
        report.printPopulationByContinentReport(null);
        report.printPopulationByContinentReport(new Population("Asia", 4000000000L));
    }

    /**
     * Tests getting population by region with blank inputs.
     */
    @Test
    void testGetPopulationByRegionInvalidInput() {
        Population pop = report.getPopulationByRegion("");
        assertNotNull(pop);
        assertEquals(0, pop.getTotalPopulation());
    }

    /**
     * Tests printing region population report handling null objects safely.
     */
    @Test
    void testPrintPopulationByRegionReport() {
        report.printPopulationByRegionReport(null);
        report.printPopulationByRegionReport(new Population("Southeast Asia", 650000000L));
    }

    /**
     * Tests getting population by country with null or blank parameters.
     */
    @Test
    void testGetPopulationByCountryInvalidInput() {
        Population pop = report.getPopulationByCountry(null);
        assertNotNull(pop);
        assertEquals(0, pop.getTotalPopulation());
    }

    /**
     * Tests printing country population report handling null objects safely.
     */
    @Test
    void testPrintPopulationByCountryReport() {
        report.printPopulationByCountryReport(null);
        report.printPopulationByCountryReport(new Population("Thailand", 71000000L));
    }

    /**
     * Tests city population by continent when database connection is null.
     */
    @Test
    void testGetCityPopulationByContinentNullConnection() {
        List<Population> list = report.getCityPopulationByContinent();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    /**
     * Tests printing city population by continent report with empty or null list parameters.
     */
    @Test
    void testPrintCityPopulationByContinentReport() {
        report.printCityPopulationByContinentReport(null);
        report.printCityPopulationByContinentReport(List.of());
    }

    /**
     * tests city population by region when database connection is null.
     */
    @Test
    void testGetCityPopulationByRegionNullConnection() {
        List<Population> list = report.getCityPopulationByRegion();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    /**
     * Tests printing city population by region report with empty or null list parameters.
     */
    @Test
    void testPrintCityPopulationByRegionReport() {
        report.printCityPopulationByRegionReport(null);
        report.printCityPopulationByRegionReport(List.of());
    }

    /**
     * Tests city population by country when database connection is null.
     */
    @Test
    void testGetCityPopulationByCountryNullConnection() {
        List<Population> list = report.getCityPopulationByCountry();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    /**
     * Tests printing city population by country report with empty or null list parameters.
     */
    @Test
    void testPrintCityPopulationByCountryReport() {
        report.printCityPopulationByCountryReport(null);
        report.printCityPopulationByCountryReport(List.of());
    }

    /**
     * Tests getting population by district with null or empty district strings.
     */
    @Test
    void testGetPopulationByDistrictInvalidInput() {
        assertNull(report.getPopulationByDistrict(null));
        assertNull(report.getPopulationByDistrict("   "));
    }

    /**
     * Tests printing district population report handling null objects safely.
     */
    @Test
    void testPrintPopulationByDistrictReport() {
        report.printPopulationByDistrictReport(null);
        report.printPopulationByDistrictReport(new Population("Bangkok", 8000000L));
    }

    /**
     * Tests getting population by city with invalid or missing parameters.
     */
    @Test
    void testGetPopulationByCityInvalidInput() {
        assertNull(report.getPopulationByCity(null, "Thailand"));
        assertNull(report.getPopulationByCity("Bangkok", ""));
    }

    /**
     * Tests printing city population report handling null objects safely.
     */
    @Test
    void testPrintPopulationByCityReport() {
        report.printPopulationByCityReport(null);
        report.printPopulationByCityReport(new Population("Bangkok", 8000000L));
    }

}