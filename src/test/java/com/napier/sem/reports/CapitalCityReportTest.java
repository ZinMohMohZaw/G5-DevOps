package com.napier.sem.reports;

import com.napier.sem.models.CapitalCity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapitalCityReportTest {

    private static CapitalCityReport report;

    @BeforeAll
    static void init() {

        report =
                new CapitalCityReport(null);
    }

    private List<CapitalCity> createSampleCapitalCities() {

        List<CapitalCity> capitals =
                new ArrayList<>();

        capitals.add(
                new CapitalCity(
                        "Tokyo",
                        "Japan",
                        13929286
                )
        );

        capitals.add(
                new CapitalCity(
                        "Bangkok",
                        "Thailand",
                        10539415
                )
        );

        capitals.add(
                new CapitalCity(
                        "Hanoi",
                        "Vietnam",
                        7784400
                )
        );

        return capitals;
    }

    @Test
    void testGetAllCapitalCitiesByPopulationNullConnection() {

        List<CapitalCity> capitals =
                report.getAllCapitalCitiesByPopulation();

        assertAll(
                "Null DB worldwide capital cities",

                () -> assertNotNull(capitals),

                () -> assertTrue(
                        capitals.isEmpty()
                )
        );
    }

    @Test
    void testPrintCapitalCitiesWithEmptyList() {

        assertDoesNotThrow(
                () -> report.printCapitalCities(
                        new ArrayList<>()
                )
        );
    }

    @Test
    void testPrintCapitalCitiesWithNullList() {

        assertDoesNotThrow(
                () -> report.printCapitalCities(null)
        );
    }

    @Test
    void testPrintCapitalCitiesWithValidList() {

        assertDoesNotThrow(
                () -> report.printCapitalCities(
                        createSampleCapitalCities()
                )
        );
    }

    @Test
    void testGetCapitalCitiesByContinentNullConnection() {

        List<CapitalCity> capitals =
                report.getCapitalCitiesByContinent(
                        "Asia"
                );

        assertAll(
                "Null DB continent capital cities",

                () -> assertNotNull(capitals),

                () -> assertTrue(
                        capitals.isEmpty()
                )
        );
    }

    @Test
    void testGetCapitalCitiesByContinentInvalidInput() {

        assertAll(
                "Invalid continent inputs",

                () -> assertTrue(
                        report
                                .getCapitalCitiesByContinent(null)
                                .isEmpty()
                ),

                () -> assertTrue(
                        report
                                .getCapitalCitiesByContinent("")
                                .isEmpty()
                ),

                () -> assertTrue(
                        report
                                .getCapitalCitiesByContinent("   ")
                                .isEmpty()
                )
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
    }

    @Test
    void testPrintCapitalCitiesByContinentNullInput() {

        assertDoesNotThrow(
                () -> report
                        .printCapitalCitiesByContinent(null)
        );
    }

    @Test
    void testGetCapitalCitiesByRegionNullConnection() {

        List<CapitalCity> capitals =
                report.getCapitalCitiesByRegion(
                        "Southeast Asia"
                );

        assertAll(
                "Null DB region capital cities",

                () -> assertNotNull(capitals),

                () -> assertTrue(
                        capitals.isEmpty()
                )
        );
    }

    @Test
    void testGetCapitalCitiesByRegionInvalidInput() {

        assertAll(
                "Invalid region inputs",

                () -> assertTrue(
                        report
                                .getCapitalCitiesByRegion(null)
                                .isEmpty()
                ),

                () -> assertTrue(
                        report
                                .getCapitalCitiesByRegion("")
                                .isEmpty()
                ),

                () -> assertTrue(
                        report
                                .getCapitalCitiesByRegion("   ")
                                .isEmpty()
                )
        );
    }

    @Test
    void testPrintCapitalCitiesByRegionReport() {

        assertDoesNotThrow(
                () -> report
                        .printCapitalCitiesByRegionReport(
                                createSampleCapitalCities(),
                                "Southeast Asia"
                        )
        );
    }

    @Test
    void testPrintCapitalCitiesByRegionReportWithEmptyList() {

        assertDoesNotThrow(
                () -> report
                        .printCapitalCitiesByRegionReport(
                                new ArrayList<>(),
                                "Southeast Asia"
                        )
        );
    }

    @Test
    void testPrintCapitalCitiesByRegionReportWithNullList() {

        assertDoesNotThrow(
                () -> report
                        .printCapitalCitiesByRegionReport(
                                null,
                                "Southeast Asia"
                        )
        );
    }

    @Test
    void testGetTopNCapitalCitiesWorldwideNullConnection() {

        assertTrue(
                report
                        .getTopNCapitalCitiesWorldwide(5)
                        .isEmpty()
        );
    }

    @Test
    void testPrintTopNCapitalCitiesWorldwideReport() {

        assertDoesNotThrow(
                () -> report
                        .printTopNCapitalCitiesWorldwideReport(
                                createSampleCapitalCities(),
                                3
                        )
        );
    }

    @Test
    void testPrintTopNCapitalCitiesWorldwideReportEmptyList() {

        assertDoesNotThrow(
                () -> report
                        .printTopNCapitalCitiesWorldwideReport(
                                new ArrayList<>(),
                                5
                        )
        );
    }

    @Test
    void testPrintTopNCapitalCitiesWorldwideReportNullList() {

        assertDoesNotThrow(
                () -> report
                        .printTopNCapitalCitiesWorldwideReport(
                                null,
                                5
                        )
        );
    }

    @Test
    void testGetTopNCapitalCitiesByContinentNullConnection() {

        assertTrue(
                report
                        .getTopNCapitalCitiesByContinent(
                                "Asia",
                                5
                        )
                        .isEmpty()
        );
    }

    @Test
    void testPrintTopNCapitalCitiesByContinentReport() {

        assertDoesNotThrow(
                () -> report
                        .printTopNCapitalCitiesByContinentReport(
                                createSampleCapitalCities(),
                                "Asia",
                                3
                        )
        );
    }

    @Test
    void testPrintTopNCapitalCitiesByContinentReportEmptyList() {

        assertDoesNotThrow(
                () -> report
                        .printTopNCapitalCitiesByContinentReport(
                                new ArrayList<>(),
                                "Asia",
                                5
                        )
        );
    }

    @Test
    void testPrintTopNCapitalCitiesByContinentReportNullList() {

        assertDoesNotThrow(
                () -> report
                        .printTopNCapitalCitiesByContinentReport(
                                null,
                                null,
                                5
                        )
        );
    }

    @Test
    void testGetTopNCapitalCitiesByRegionNullConnection() {

        assertTrue(
                report
                        .getTopNCapitalCitiesByRegion(
                                "Southeast Asia",
                                5
                        )
                        .isEmpty()
        );
    }

    @Test
    void testPrintTopNCapitalCitiesByRegionReport() {

        assertDoesNotThrow(
                () -> report
                        .printTopNCapitalCitiesByRegionReport(
                                createSampleCapitalCities(),
                                "Southeast Asia",
                                3
                        )
        );
    }

    @Test
    void testPrintTopNCapitalCitiesByRegionReportEmptyList() {

        assertDoesNotThrow(
                () -> report
                        .printTopNCapitalCitiesByRegionReport(
                                new ArrayList<>(),
                                "Southeast Asia",
                                5
                        )
        );
    }

    @Test
    void testPrintTopNCapitalCitiesByRegionReportNullList() {

        assertDoesNotThrow(
                () -> report
                        .printTopNCapitalCitiesByRegionReport(
                                null,
                                null,
                                5
                        )
        );
    }
}