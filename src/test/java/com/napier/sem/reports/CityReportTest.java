package com.napier.sem.reports;

import com.napier.sem.models.City;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityReportTest {

    private CityReport cityReport;
    private List<City> cities;

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

    @Test
    void testGetAllCitiesSortedByPopulation() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .getAllCitiesSortedByPopulation()
        );
    }

    @Test
    void testGenerateCityReport() {

        assertDoesNotThrow(
                () -> cityReport.generateCityReport(cities)
        );
    }

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

    @Test
    void testGetCitiesByContinent() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .getCitiesByContinent("Asia")
        );
    }

    @Test
    void testGenerateContinentCityReport() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .generateContinentCityReport("Asia")
        );
    }

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

    @Test
    void testGetCitiesByCountry() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .getCitiesByCountry("Thailand")
        );
    }

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

    @Test
    void testGetCitiesByDistrict() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport
                        .getCitiesByDistrict("Bangkok")
        );
    }

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

    @Test
    void testGetTopNCities() {

        assertThrows(
                NullPointerException.class,
                () -> cityReport.getTopNCities(5)
        );
    }

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
}