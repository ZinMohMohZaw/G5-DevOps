package com.napier.sem.reports;

import com.napier.sem.models.City;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the CityReport class.
 * Verifies report generation, parameter validation,
 * null handling and defensive report branches.
 */
class CityReportTest {

    private CityReport cityReport;
    private List<City> cities;

    /**
     * Creates a CityReport without a database connection
     * and prepares sample city data before each test.
     */
    @BeforeEach
    void setUp() {

        cityReport =
                new CityReport(null);

        cities =
                new ArrayList<>();

        cities.add(
                new City(
                        "Bangkok",
                        "Thailand",
                        "Bangkok",
                        10500000
                )
        );

        cities.add(
                new City(
                        "Chiang Mai",
                        "Thailand",
                        "Chiang Mai",
                        127000
                )
        );
    }

    /**
     * Tests city retrieval when no
     * database connection is available.
     */
    @Test
    void testGetAllCitiesSortedByPopulation() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .getAllCitiesSortedByPopulation()
        );
    }

    /**
     * Tests city report generation using
     * valid sample city data.
     */
    @Test
    void testGenerateCityReport() {

        assertDoesNotThrow(
                () -> cityReport.generateCityReport(cities)
        );
    }

    /**
     * Tests city report generation using
     * empty and null city lists.
     */
    @Test
    void testGenerateCityReportEmptyAndNull() {

        assertAll(
                "Empty and null city report",

                () -> assertDoesNotThrow(
                        () -> cityReport.generateCityReport(
                                new ArrayList<>()
                        )
                ),

                () -> assertDoesNotThrow(
                        () -> cityReport.generateCityReport(null)
                )
        );
    }

    /**
     * Tests that null entries within a city
     * list are handled safely.
     */
    @Test
    void testGenerateCityReportWithNullCity() {

        List<City> testCities =
                new ArrayList<>();

        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(
                () -> cityReport
                        .generateCityReport(testCities)
        );
    }

    /**
     * Tests continent city retrieval without
     * an active database connection.
     */
    @Test
    void testGetCitiesByContinent() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .getCitiesByContinent("Asia")
        );
    }

    /**
     * Tests continent city report generation
     * without an active database connection.
     */
    @Test
    void testGenerateContinentCityReport() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .generateContinentCityReport("Asia")
        );
    }

    /**
     * Tests regional city retrieval without
     * an active database connection.
     */
    @Test
    void testGetCitiesByRegion() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .getCitiesByRegion(
                                "Southeast Asia"
                        )
        );
    }

    /**
     * Tests regional city report generation
     * using valid sample city data.
     */
    @Test
    void testPrintCitiesByRegionReport() {

        assertDoesNotThrow(
                () -> cityReport
                        .printCitiesByRegionReport(
                                cities,
                                "Southeast Asia"
                        )
        );
    }

    /**
     * Tests country city retrieval without
     * an active database connection.
     */
    @Test
    void testGetCitiesByCountry() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .getCitiesByCountry("Thailand")
        );
    }

    /**
     * Tests country city report generation
     * using valid sample city data.
     */
    @Test
    void testPrintCitiesByCountryReport() {

        assertDoesNotThrow(
                () -> cityReport
                        .printCitiesByCountryReport(
                                cities,
                                "Thailand"
                        )
        );
    }

    /**
     * Tests district city retrieval without
     * an active database connection.
     */
    @Test
    void testGetCitiesByDistrict() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .getCitiesByDistrict("Bangkok")
        );
    }

    /**
     * Tests district city report generation
     * using valid sample city data.
     */
    @Test
    void testPrintCitiesByDistrictReport() {

        assertDoesNotThrow(
                () -> cityReport
                        .printCitiesByDistrictReport(
                                cities,
                                "Bangkok"
                        )
        );
    }

    /**
     * Tests district report handling using
     * empty and null city lists.
     */
    @Test
    void testPrintCitiesByDistrictReportEmpty() {

        assertAll(
                "Empty district report",

                () -> assertDoesNotThrow(
                        () -> cityReport
                                .printCitiesByDistrictReport(
                                        new ArrayList<>(),
                                        "Bangkok"
                                )
                ),

                () -> assertDoesNotThrow(
                        () -> cityReport
                                .printCitiesByDistrictReport(
                                        null,
                                        null
                                )
                )
        );
    }

    /**
     * Tests that null entries in a district
     * city list are ignored safely.
     */
    @Test
    void testPrintCitiesByDistrictReportWithNullCity() {

        List<City> testCities =
                new ArrayList<>();

        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(
                () -> cityReport
                        .printCitiesByDistrictReport(
                                testCities,
                                "Bangkok"
                        )
        );
    }

    /**
     * Tests Top N worldwide city retrieval
     * without an active database connection.
     */
    @Test
    void testGetTopNCities() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport.getTopNCities(5)
        );
    }

    /**
     * Tests generation of a valid
     * Top N worldwide city report.
     */
    @Test
    void testPrintTopNCitiesWorldwideReport() {

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesWorldwideReport(
                                cities,
                                2
                        )
        );
    }

    /**
     * Tests invalid Top N values and missing
     * data for worldwide city report output.
     */
    @Test
    void testPrintTopNCitiesWorldwideInvalid() {

        assertAll(
                "Invalid worldwide Top N",

                () -> assertDoesNotThrow(
                        () -> cityReport
                                .printTopNCitiesWorldwideReport(
                                        cities,
                                        0
                                )
                ),

                () -> assertDoesNotThrow(
                        () -> cityReport
                                .printTopNCitiesWorldwideReport(
                                        cities,
                                        -1
                                )
                ),

                () -> assertDoesNotThrow(
                        () -> cityReport
                                .printTopNCitiesWorldwideReport(
                                        new ArrayList<>(),
                                        5
                                )
                ),

                () -> assertDoesNotThrow(
                        () -> cityReport
                                .printTopNCitiesWorldwideReport(
                                        null,
                                        5
                                )
                )
        );
    }

    /**
     * Tests parameter validation and null-connection
     * behaviour for Top N continent city retrieval.
     */
    @Test
    void testGetTopNCitiesByContinent() {

        assertAll(
                "Top N cities by continent",

                () -> assertTrue(
                        cityReport
                                .getTopNCitiesByContinent(
                                        "",
                                        5
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        cityReport
                                .getTopNCitiesByContinent(
                                        null,
                                        5
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        cityReport
                                .getTopNCitiesByContinent(
                                        "Asia",
                                        0
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        cityReport
                                .getTopNCitiesByContinent(
                                        "Asia",
                                        5
                                )
                                .isEmpty()
                )
        );
    }

    /**
     * Tests generation of a valid
     * Top N continent city report.
     */
    @Test
    void testPrintTopNCitiesByContinentReport() {

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByContinentReport(
                                cities,
                                "Asia",
                                2
                        )
        );
    }

    /**
     * Tests Top N continent report output
     * with an empty city list.
     */
    @Test
    void testPrintTopNCitiesByContinentEmpty() {

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByContinentReport(
                                new ArrayList<>(),
                                "Asia",
                                5
                        )
        );
    }

    /**
     * Tests that null city entries are ignored
     * in a Top N continent report.
     */
    @Test
    void testPrintTopNCitiesByContinentWithNullCity() {

        List<City> testCities =
                new ArrayList<>();

        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByContinentReport(
                                testCities,
                                "Asia",
                                2
                        )
        );
    }

    /**
     * Tests parameter validation and null-connection
     * behaviour for Top N regional city retrieval.
     */
    @Test
    void testGetTopNCitiesByRegion() {

        assertAll(
                "Top N cities by region",

                () -> assertTrue(
                        cityReport
                                .getTopNCitiesByRegion(
                                        "",
                                        5
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        cityReport
                                .getTopNCitiesByRegion(
                                        null,
                                        5
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        cityReport
                                .getTopNCitiesByRegion(
                                        "Southeast Asia",
                                        0
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        cityReport
                                .getTopNCitiesByRegion(
                                        "Southeast Asia",
                                        5
                                )
                                .isEmpty()
                )
        );
    }

    /**
     * Tests generation of a valid
     * Top N regional city report.
     */
    @Test
    void testPrintTopNCitiesByRegionReport() {

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByRegionReport(
                                cities,
                                "Southeast Asia",
                                2
                        )
        );
    }

    /**
     * Tests Top N regional report output
     * with an empty city list.
     */
    @Test
    void testPrintTopNCitiesByRegionEmpty() {

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByRegionReport(
                                new ArrayList<>(),
                                "Southeast Asia",
                                5
                        )
        );
    }

    /**
     * Tests that null city entries are ignored
     * in a Top N regional report.
     */
    @Test
    void testPrintTopNCitiesByRegionWithNullCity() {

        List<City> testCities =
                new ArrayList<>();

        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByRegionReport(
                                testCities,
                                "Southeast Asia",
                                2
                        )
        );
    }

    /**
     * Tests Top N country city retrieval
     * without an active database connection.
     */
    @Test
    void testGetTopNCitiesByCountry() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .getTopNCitiesByCountry(
                                "Thailand",
                                5
                        )
        );
    }

    /**
     * Tests generation of a valid
     * Top N country city report.
     */
    @Test
    void testPrintTopNCitiesByCountryReport() {

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByCountryReport(
                                cities,
                                "Thailand",
                                2
                        )
        );
    }

    /**
     * Tests Top N country report output
     * using empty and null inputs.
     */
    @Test
    void testPrintTopNCitiesByCountryEmpty() {

        assertAll(
                "Empty Top N country report",

                () -> assertDoesNotThrow(
                        () -> cityReport
                                .printTopNCitiesByCountryReport(
                                        new ArrayList<>(),
                                        "Thailand",
                                        5
                                )
                ),

                () -> assertDoesNotThrow(
                        () -> cityReport
                                .printTopNCitiesByCountryReport(
                                        null,
                                        null,
                                        5
                                )
                )
        );
    }

    /**
     * Tests that null city entries are ignored
     * in a Top N country report.
     */
    @Test
    void testPrintTopNCitiesByCountryWithNullCity() {

        List<City> testCities =
                new ArrayList<>();

        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByCountryReport(
                                testCities,
                                "Thailand",
                                2
                        )
        );
    }

    /**
     * Tests parameter validation for
     * Top N district city retrieval.
     */
    @Test
    void testGetTopNCitiesByDistrict() {

        assertAll(
                "Top N cities by district",

                () -> assertTrue(
                        cityReport
                                .getTopNCitiesByDistrict(
                                        null,
                                        5
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        cityReport
                                .getTopNCitiesByDistrict(
                                        "",
                                        5
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        cityReport
                                .getTopNCitiesByDistrict(
                                        "Bangkok",
                                        0
                                )
                                .isEmpty()
                )
        );
    }

    /**
     * Tests generation of a valid
     * Top N district city report.
     */
    @Test
    void testPrintTopNCitiesByDistrictReport() {

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByDistrictReport(
                                cities,
                                "Bangkok",
                                2
                        )
        );
    }

    /**
     * Tests Top N district report output
     * using empty and null inputs.
     */
    @Test
    void testPrintTopNCitiesByDistrictEmpty() {

        assertAll(
                "Empty Top N district report",

                () -> assertDoesNotThrow(
                        () -> cityReport
                                .printTopNCitiesByDistrictReport(
                                        new ArrayList<>(),
                                        "Bangkok",
                                        5
                                )
                ),

                () -> assertDoesNotThrow(
                        () -> cityReport
                                .printTopNCitiesByDistrictReport(
                                        null,
                                        null,
                                        5
                                )
                )
        );
    }

    /**
     * Tests that null city entries are ignored
     * in a Top N district report.
     */
    @Test
    void testPrintTopNCitiesByDistrictWithNullCity() {

        List<City> testCities =
                new ArrayList<>();

        testCities.add(null);
        testCities.add(cities.get(0));

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByDistrictReport(
                                testCities,
                                "Bangkok",
                                2
                        )
        );
    }

    /**
     * Tests regional report output when
     * the region name is null.
     */
    @Test
    void testPrintCitiesByRegionReportWithNullRegion() {

        assertDoesNotThrow(
                () -> cityReport
                        .printCitiesByRegionReport(
                                new ArrayList<>(),
                                null
                        )
        );
    }

    /**
     * Tests country report output when
     * the country name is null.
     */
    @Test
    void testPrintCitiesByCountryReportWithNullCountry() {

        assertDoesNotThrow(
                () -> cityReport
                        .printCitiesByCountryReport(
                                new ArrayList<>(),
                                null
                        )
        );
    }

    /**
     * Tests district report output with valid city
     * data when the district name is null.
     */
    @Test
    void testPrintCitiesByDistrictReportWithNullDistrictAndData() {

        assertDoesNotThrow(
                () -> cityReport
                        .printCitiesByDistrictReport(
                                cities,
                                null
                        )
        );
    }

    /**
     * Tests Top N continent report handling when both
     * the city list and continent name are null.
     */
    @Test
    void testPrintTopNCitiesByContinentNullListAndContinent() {

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByContinentReport(
                                null,
                                null,
                                5
                        )
        );
    }

    /**
     * Tests Top N regional report handling when both
     * the city list and region name are null.
     */
    @Test
    void testPrintTopNCitiesByRegionNullListAndRegion() {

        assertDoesNotThrow(
                () -> cityReport
                        .printTopNCitiesByRegionReport(
                                null,
                                null,
                                5
                        )
        );
    }
}