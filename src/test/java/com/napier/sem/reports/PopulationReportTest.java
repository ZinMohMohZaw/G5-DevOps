package com.napier.sem.reports;

import com.napier.sem.models.Population;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PopulationReport class.
 * Verifies validation, null-connection behaviour
 * and population report output branches.
 */
class PopulationReportTest {

    private PopulationReport report;

    /**
     * Creates a PopulationReport without an active
     * database connection before each test.
     */
    @BeforeEach
    void setUp() {

        report =
                new PopulationReport(null);
    }

    /**
     * Tests world population retrieval when
     * no database connection is available.
     */
    @Test
    void testGetWorldPopulationNullConnection() {

        Population result =
                report.getWorldPopulation();

        assertNull(result);
    }

    /**
     * Tests world population validation using null,
     * invalid and valid Population objects.
     */
    @Test
    void testValidateWorldPopulationEdgeCases() {

        assertAll(
                "World population validation",

                () -> assertFalse(
                        report.validateWorldPopulation(null)
                ),

                () -> assertFalse(
                        report.validateWorldPopulation(
                                new Population(
                                        null,
                                        5000000
                                )
                        )
                ),

                () -> assertFalse(
                        report.validateWorldPopulation(
                                new Population(
                                        "",
                                        5000000
                                )
                        )
                ),

                () -> assertFalse(
                        report.validateWorldPopulation(
                                new Population(
                                        "   ",
                                        5000000
                                )
                        )
                ),

                () -> assertFalse(
                        report.validateWorldPopulation(
                                new Population(
                                        "World",
                                        0
                                )
                        )
                ),

                () -> assertFalse(
                        report.validateWorldPopulation(
                                new Population(
                                        "World",
                                        -100
                                )
                        )
                ),

                () -> assertTrue(
                        report.validateWorldPopulation(
                                new Population(
                                        "World",
                                        8000000000L
                                )
                        )
                )
        );
    }

    /**
     * Tests world population report output using
     * null, invalid and valid population data.
     */
    @Test
    void testPrintWorldPopulationReportBranches() {

        assertDoesNotThrow(
                () -> {
                    report.printWorldPopulationReport(
                            null
                    );

                    report.printWorldPopulationReport(
                            new Population(
                                    "",
                                    1000
                            )
                    );

                    report.printWorldPopulationReport(
                            new Population(
                                    "World",
                                    0
                            )
                    );

                    report.printWorldPopulationReport(
                            new Population(
                                    "World",
                                    8000000000L
                            )
                    );
                }
        );
    }

    /**
     * Tests validation of null, empty and blank
     * continent parameters.
     */
    @Test
    void testGetPopulationByContinentInvalidInput() {

        assertAll(
                "Invalid continent population input",

                () -> assertEquals(
                        0,
                        report
                                .getPopulationByContinent(null)
                                .getTotalPopulation()
                ),

                () -> assertEquals(
                        0,
                        report
                                .getPopulationByContinent("")
                                .getTotalPopulation()
                ),

                () -> assertEquals(
                        0,
                        report
                                .getPopulationByContinent("   ")
                                .getTotalPopulation()
                )
        );
    }

    /**
     * Tests continent population report output
     * using null and valid population data.
     */
    @Test
    void testPrintPopulationByContinentReportBranches() {

        assertDoesNotThrow(
                () -> {
                    report.printPopulationByContinentReport(
                            null
                    );

                    report.printPopulationByContinentReport(
                            new Population(
                                    "Asia",
                                    4000000000L
                            )
                    );
                }
        );
    }

    /**
     * Tests validation of null, empty and blank
     * region parameters.
     */
    @Test
    void testGetPopulationByRegionInvalidInput() {

        assertAll(
                "Invalid region population input",

                () -> assertEquals(
                        0,
                        report
                                .getPopulationByRegion(null)
                                .getTotalPopulation()
                ),

                () -> assertEquals(
                        0,
                        report
                                .getPopulationByRegion("")
                                .getTotalPopulation()
                ),

                () -> assertEquals(
                        0,
                        report
                                .getPopulationByRegion("   ")
                                .getTotalPopulation()
                )
        );
    }

    /**
     * Tests regional population report output
     * using null and valid population data.
     */
    @Test
    void testPrintPopulationByRegionReportBranches() {

        assertDoesNotThrow(
                () -> {
                    report.printPopulationByRegionReport(
                            null
                    );

                    report.printPopulationByRegionReport(
                            new Population(
                                    "Southeast Asia",
                                    650000000L
                            )
                    );
                }
        );
    }

    /**
     * Tests validation of null, empty and blank
     * country parameters.
     */
    @Test
    void testGetPopulationByCountryInvalidInput() {

        assertAll(
                "Invalid country population input",

                () -> assertEquals(
                        0,
                        report
                                .getPopulationByCountry(null)
                                .getTotalPopulation()
                ),

                () -> assertEquals(
                        0,
                        report
                                .getPopulationByCountry("")
                                .getTotalPopulation()
                ),

                () -> assertEquals(
                        0,
                        report
                                .getPopulationByCountry("   ")
                                .getTotalPopulation()
                )
        );
    }

    /**
     * Tests country population report output
     * using null and valid population data.
     */
    @Test
    void testPrintPopulationByCountryReportBranches() {

        assertDoesNotThrow(
                () -> {
                    report.printPopulationByCountryReport(
                            null
                    );

                    report.printPopulationByCountryReport(
                            new Population(
                                    "Thailand",
                                    71000000L
                            )
                    );
                }
        );
    }

    /**
     * Tests city population by continent retrieval
     * without an active database connection.
     */
    @Test
    void testGetCityPopulationByContinentNullConnection() {

        assertTrue(
                report
                        .getCityPopulationByContinent()
                        .isEmpty()
        );
    }

    /**
     * Tests city population by continent output using
     * null, empty, null-entry and valid data.
     */
    @Test
    void testPrintCityPopulationByContinentReportBranches() {

        List<Population> list =
                new ArrayList<>();

        list.add(null);

        list.add(
                new Population(
                        "Asia",
                        4000000000L,
                        1500000000L,
                        37.5,
                        2500000000L,
                        62.5
                )
        );

        assertDoesNotThrow(
                () -> {
                    report
                            .printCityPopulationByContinentReport(
                                    null
                            );

                    report
                            .printCityPopulationByContinentReport(
                                    List.of()
                            );

                    report
                            .printCityPopulationByContinentReport(
                                    list
                            );
                }
        );
    }

    /**
     * Tests city population by region retrieval
     * without an active database connection.
     */
    @Test
    void testGetCityPopulationByRegionNullConnection() {

        assertTrue(
                report
                        .getCityPopulationByRegion()
                        .isEmpty()
        );
    }

    /**
     * Tests city population by region output using
     * null, empty, null-entry and valid data.
     */
    @Test
    void testPrintCityPopulationByRegionReportBranches() {

        List<Population> list =
                new ArrayList<>();

        list.add(null);

        list.add(
                new Population(
                        "Southeast Asia",
                        650000000L,
                        300000000L,
                        46.15,
                        350000000L,
                        53.85
                )
        );

        assertDoesNotThrow(
                () -> {
                    report
                            .printCityPopulationByRegionReport(
                                    null
                            );

                    report
                            .printCityPopulationByRegionReport(
                                    List.of()
                            );

                    report
                            .printCityPopulationByRegionReport(
                                    list
                            );
                }
        );
    }

    /**
     * Tests city population by country retrieval
     * without an active database connection.
     */
    @Test
    void testGetCityPopulationByCountryNullConnection() {

        assertTrue(
                report
                        .getCityPopulationByCountry()
                        .isEmpty()
        );
    }

    /**
     * Tests city population by country output using
     * null, empty, null-entry and valid data.
     */
    @Test
    void testPrintCityPopulationByCountryReportBranches() {

        List<Population> list =
                new ArrayList<>();

        list.add(null);

        list.add(
                new Population(
                        "Thailand",
                        71000000L,
                        10000000L,
                        14.08,
                        61000000L,
                        85.92
                )
        );

        assertDoesNotThrow(
                () -> {
                    report
                            .printCityPopulationByCountryReport(
                                    null
                            );

                    report
                            .printCityPopulationByCountryReport(
                                    List.of()
                            );

                    report
                            .printCityPopulationByCountryReport(
                                    list
                            );
                }
        );
    }

    /**
     * Tests validation of null, empty and blank
     * district parameters.
     */
    @Test
    void testGetPopulationByDistrictInvalidInput() {

        assertAll(
                "Invalid district population input",

                () -> assertNull(
                        report.getPopulationByDistrict(
                                null
                        )
                ),

                () -> assertNull(
                        report.getPopulationByDistrict(
                                ""
                        )
                ),

                () -> assertNull(
                        report.getPopulationByDistrict(
                                "   "
                        )
                )
        );
    }

    /**
     * Tests district population report output
     * using null and valid population data.
     */
    @Test
    void testPrintPopulationByDistrictReportBranches() {

        assertDoesNotThrow(
                () -> {
                    report
                            .printPopulationByDistrictReport(
                                    null
                            );

                    report
                            .printPopulationByDistrictReport(
                                    new Population(
                                            "Bangkok",
                                            8000000L
                                    )
                            );
                }
        );
    }

    /**
     * Tests invalid city and country parameter
     * combinations for city population retrieval.
     */
    @Test
    void testGetPopulationByCityInvalidInput() {

        assertAll(
                "Invalid city population input",

                () -> assertNull(
                        report.getPopulationByCity(
                                null,
                                "Thailand"
                        )
                ),

                () -> assertNull(
                        report.getPopulationByCity(
                                "Bangkok",
                                null
                        )
                ),

                () -> assertNull(
                        report.getPopulationByCity(
                                "",
                                "Thailand"
                        )
                ),

                () -> assertNull(
                        report.getPopulationByCity(
                                "Bangkok",
                                ""
                        )
                ),

                () -> assertNull(
                        report.getPopulationByCity(
                                "   ",
                                "   "
                        )
                )
        );
    }

    /**
     * Tests city population report output
     * using null and valid population data.
     */
    @Test
    void testPrintPopulationByCityReportBranches() {

        assertDoesNotThrow(
                () -> {
                    report
                            .printPopulationByCityReport(
                                    null
                            );

                    report
                            .printPopulationByCityReport(
                                    new Population(
                                            "Bangkok",
                                            8000000L
                                    )
                            );
                }
        );
    }
}