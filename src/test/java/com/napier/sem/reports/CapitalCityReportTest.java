package com.napier.sem.reports;

import com.napier.sem.models.CapitalCity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CapitalCityReport class.
 * Verifies validation, null handling and formatted
 * capital city report generation.
 */
class CapitalCityReportTest {

    private static CapitalCityReport report;

    /**
     * Creates a CapitalCityReport without an active
     * database connection before the unit tests.
     */
    @BeforeAll
    static void init() {

        report =
                new CapitalCityReport(null);
    }

    /**
     * Creates sample capital city records used
     * by the report output tests.
     *
     * @return sample capital city list
     */
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

    /**
     * Tests worldwide capital city retrieval
     * without an active database connection.
     */
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

    /**
     * Tests capital city report output
     * using an empty list.
     */
    @Test
    void testPrintCapitalCitiesWithEmptyList() {

        assertDoesNotThrow(
                () -> report.printCapitalCities(
                        new ArrayList<>()
                )
        );
    }

    /**
     * Tests capital city report output
     * using a null list.
     */
    @Test
    void testPrintCapitalCitiesWithNullList() {

        assertDoesNotThrow(
                () -> report.printCapitalCities(null)
        );
    }

    /**
     * Tests capital city report output
     * using valid sample data.
     */
    @Test
    void testPrintCapitalCitiesWithValidList() {

        assertDoesNotThrow(
                () -> report.printCapitalCities(
                        createSampleCapitalCities()
                )
        );
    }

    /**
     * Tests continent capital city retrieval
     * without an active database connection.
     */
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

    /**
     * Tests null, empty and blank
     * continent parameter validation.
     */
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

    /**
     * Tests generation of a capital city
     * report for a continent.
     */
    @Test
    void testPrintCapitalCitiesByContinent() {

        assertDoesNotThrow(
                () -> report
                        .printCapitalCitiesByContinent(
                                "Asia"
                        )
        );
    }

    /**
     * Tests continent report generation
     * when the continent value is null.
     */
    @Test
    void testPrintCapitalCitiesByContinentNullInput() {

        assertDoesNotThrow(
                () -> report
                        .printCapitalCitiesByContinent(null)
        );
    }

    /**
     * Tests continent report handling when
     * capital city retrieval returns null.
     */
    @Test
    void testPrintCapitalCitiesByContinentWithNullResult() {

        CapitalCityReport nullResultReport =
                new CapitalCityReport(null) {
                    @Override
                    public List<CapitalCity> getCapitalCitiesByContinent(String continent) {
                        return null;
                    }
                };

        assertDoesNotThrow(
                () -> nullResultReport
                        .printCapitalCitiesByContinent(
                                "Asia"
                        )
        );
    }

    /**
     * Tests regional capital city retrieval
     * without an active database connection.
     */
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

    /**
     * Tests null, empty and blank
     * region parameter validation.
     */
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

    /**
     * Tests generation of a regional capital
     * city report using valid data.
     */
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

    /**
     * Tests regional capital city report
     * output using an empty list.
     */
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

    /**
     * Tests regional capital city report
     * output using a null list.
     */
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

    /**
     * Tests regional report handling when
     * both the list and region are null.
     */
    @Test
    void testPrintCapitalCitiesByRegionReportWithNullRegion() {

        assertDoesNotThrow(
                () -> report
                        .printCapitalCitiesByRegionReport(
                                null,
                                null
                        )
        );
    }

    /**
     * Tests Top N worldwide capital city retrieval
     * without an active database connection.
     */
    @Test
    void testGetTopNCapitalCitiesWorldwideNullConnection() {

        assertTrue(
                report
                        .getTopNCapitalCitiesWorldwide(5)
                        .isEmpty()
        );
    }

    /**
     * Tests generation of a valid
     * Top N worldwide capital city report.
     */
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

    /**
     * Tests Top N worldwide report output
     * using an empty list.
     */
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

    /**
     * Tests Top N worldwide report output
     * using a null list.
     */
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

    /**
     * Tests Top N continent capital city retrieval
     * without an active database connection.
     */
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

    /**
     * Tests generation of a valid
     * Top N continent capital city report.
     */
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

    /**
     * Tests Top N continent report output
     * using an empty list.
     */
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

    /**
     * Tests Top N continent report output
     * using null input values.
     */
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

    /**
     * Tests Top N regional capital city retrieval
     * without an active database connection.
     */
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

    /**
     * Tests generation of a valid
     * Top N regional capital city report.
     */
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

    /**
     * Tests Top N regional report output
     * using an empty list.
     */
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

    /**
     * Tests Top N regional report output
     * using null input values.
     */
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