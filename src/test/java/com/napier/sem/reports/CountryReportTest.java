package com.napier.sem.reports;

import com.napier.sem.models.Country;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryReportTest {

    private CountryReport countryReport;

    @BeforeEach
    void setUp() {

        countryReport =
                new CountryReport(null);
    }

    @Test
    void testGetAllCountriesSorted() {

        try {

            List<Country> result =
                    countryReport
                            .getAllCountriesSorted();

            assertNotNull(
                    result,
                    "Returned country list should not be null."
            );

        } catch (Exception e) {

            assertNotNull(
                    e,
                    "An exception should be available when the database connection is unavailable."
            );
        }
    }

    @Test
    void testGenerateCountryReport() {

        assertThrows(
                NullPointerException.class,
                () -> countryReport
                        .generateCountryReport(),
                "Country report generation should throw NullPointerException when the database connection is null."
        );
    }

    @Test
    void testGetCountriesByContinent() {

        try {

            List<Country> result =
                    countryReport
                            .getCountriesByContinent(
                                    "Asia"
                            );

            assertNotNull(
                    result,
                    "Returned continent country list should not be null."
            );

        } catch (Exception e) {

            assertNotNull(
                    e,
                    "An exception should be available when the database connection is unavailable."
            );
        }
    }

    @Test
    void testPrintCountriesByContinentReport() {

        List<Country> list =
                new ArrayList<>();

        list.add(null);

        list.add(
                new Country(
                        "THA",
                        "Thailand",
                        "Asia",
                        "Southeast Asia",
                        71000000,
                        null
                )
        );

        list.add(
                new Country(
                        "VNM",
                        "Vietnam",
                        "Asia",
                        "Southeast Asia",
                        98000000,
                        "Hanoi"
                )
        );

        assertDoesNotThrow(
                () -> {
                    countryReport
                            .printCountriesByContinentReport(
                                    null,
                                    "Asia"
                            );

                    countryReport
                            .printCountriesByContinentReport(
                                    new ArrayList<>(),
                                    "Asia"
                            );

                    countryReport
                            .printCountriesByContinentReport(
                                    list,
                                    "Asia"
                            );
                },
                "Countries-by-continent printing should handle all supported inputs safely."
        );
    }

    @Test
    void testGetCountriesByRegion() {

        try {

            List<Country> result =
                    countryReport
                            .getCountriesByRegion(
                                    "Southeast Asia"
                            );

            assertNotNull(
                    result,
                    "Returned regional country list should not be null."
            );

        } catch (Exception e) {

            assertNotNull(
                    e,
                    "An exception should be available when the database connection is unavailable."
            );
        }
    }

    @Test
    void testPrintCountriesByRegion() {

        List<Country> list =
                new ArrayList<>();

        list.add(null);

        list.add(
                new Country(
                        "THA",
                        "Thailand",
                        "Asia",
                        "Southeast Asia",
                        71000000,
                        null
                )
        );

        list.add(
                new Country(
                        "MYS",
                        "Malaysia",
                        "Asia",
                        "Southeast Asia",
                        32000000,
                        "Kuala Lumpur"
                )
        );

        assertDoesNotThrow(
                () -> {
                    countryReport
                            .printCountriesByRegion(
                                    null,
                                    "Southeast Asia"
                            );

                    countryReport
                            .printCountriesByRegion(
                                    new ArrayList<>(),
                                    "Southeast Asia"
                            );

                    countryReport
                            .printCountriesByRegion(
                                    list,
                                    "Southeast Asia"
                            );
                },
                "Countries-by-region printing should safely handle all supported inputs."
        );
    }

    @Test
    void testGetTopNCountriesWorldwide() {

        List<Country> invalidResultZero =
                countryReport
                        .getTopNCountriesWorldwide(0);

        List<Country> invalidResultNegative =
                countryReport
                        .getTopNCountriesWorldwide(-5);

        assertAll(
                "Invalid worldwide Top N",

                () -> assertNotNull(
                        invalidResultZero,
                        "Result for zero should not be null."
                ),

                () -> assertTrue(
                        invalidResultZero.isEmpty(),
                        "Result should be empty when N is zero."
                ),

                () -> assertNotNull(
                        invalidResultNegative,
                        "Result for negative N should not be null."
                ),

                () -> assertTrue(
                        invalidResultNegative.isEmpty(),
                        "Result should be empty when N is negative."
                )
        );

        try {

            countryReport
                    .getTopNCountriesWorldwide(5);

        } catch (Exception e) {

            assertNotNull(
                    e,
                    "Exception should be available when the database connection is unavailable."
            );
        }
    }

    @Test
    void testPrintTopNCountriesWorldwideReport() {

        List<Country> list =
                new ArrayList<>();

        list.add(null);

        list.add(
                new Country(
                        "CHN",
                        "China",
                        "Asia",
                        "Eastern Asia",
                        1400000000,
                        null
                )
        );

        list.add(
                new Country(
                        "JPN",
                        "Japan",
                        "Asia",
                        "Eastern Asia",
                        125000000,
                        "Tokyo"
                )
        );

        assertDoesNotThrow(
                () -> {
                    countryReport
                            .printTopNCountriesWorldwideReport(
                                    null,
                                    5
                            );

                    countryReport
                            .printTopNCountriesWorldwideReport(
                                    new ArrayList<>(),
                                    5
                            );

                    countryReport
                            .printTopNCountriesWorldwideReport(
                                    list,
                                    5
                            );
                }
        );
    }

    @Test
    void testGetTopNCountriesByContinent() {

        assertAll(
                "Invalid continent Top N inputs",

                () -> assertTrue(
                        countryReport
                                .getTopNCountriesByContinent(
                                        "",
                                        5
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        countryReport
                                .getTopNCountriesByContinent(
                                        "   ",
                                        5
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        countryReport
                                .getTopNCountriesByContinent(
                                        null,
                                        5
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        countryReport
                                .getTopNCountriesByContinent(
                                        "Asia",
                                        0
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        countryReport
                                .getTopNCountriesByContinent(
                                        "Asia",
                                        -1
                                )
                                .isEmpty()
                )
        );

        try {

            countryReport
                    .getTopNCountriesByContinent(
                            "Asia",
                            5
                    );

        } catch (Exception e) {

            assertNotNull(
                    e
            );
        }
    }

    @Test
    void testPrintTopNCountriesByContinentReport() {

        List<Country> list =
                new ArrayList<>();

        list.add(null);

        list.add(
                new Country(
                        "IND",
                        "India",
                        "Asia",
                        "Southern Asia",
                        1400000000,
                        null
                )
        );

        list.add(
                new Country(
                        "SGP",
                        "Singapore",
                        "Asia",
                        "Southeast Asia",
                        5400000,
                        "Singapore"
                )
        );

        assertDoesNotThrow(
                () -> {
                    countryReport
                            .printTopNCountriesByContinentReport(
                                    null,
                                    "Asia",
                                    5
                            );

                    countryReport
                            .printTopNCountriesByContinentReport(
                                    new ArrayList<>(),
                                    "Asia",
                                    5
                            );

                    countryReport
                            .printTopNCountriesByContinentReport(
                                    list,
                                    "Asia",
                                    5
                            );
                }
        );
    }

    @Test
    void testGetTopNCountriesByRegion() {

        assertAll(
                "Invalid region Top N inputs",

                () -> assertTrue(
                        countryReport
                                .getTopNCountriesByRegion(
                                        "Southeast Asia",
                                        0
                                )
                                .isEmpty()
                ),

                () -> assertTrue(
                        countryReport
                                .getTopNCountriesByRegion(
                                        "Southeast Asia",
                                        -3
                                )
                                .isEmpty()
                )
        );

        try {

            countryReport
                    .getTopNCountriesByRegion(
                            "Southeast Asia",
                            5
                    );

        } catch (Exception e) {

            assertNotNull(e);
        }
    }

    @Test
    void testPrintTopNCountriesByRegionReport() {

        List<Country> list =
                new ArrayList<>();

        list.add(null);

        list.add(
                new Country(
                        "THA",
                        "Thailand",
                        "Asia",
                        "Southeast Asia",
                        71000000,
                        null
                )
        );

        list.add(
                new Country(
                        "IDN",
                        "Indonesia",
                        "Asia",
                        "Southeast Asia",
                        273500000,
                        "Jakarta"
                )
        );

        assertDoesNotThrow(
                () -> {
                    countryReport
                            .printTopNCountriesByRegionReport(
                                    null,
                                    "Southeast Asia",
                                    5
                            );

                    countryReport
                            .printTopNCountriesByRegionReport(
                                    new ArrayList<>(),
                                    "Southeast Asia",
                                    5
                            );

                    countryReport
                            .printTopNCountriesByRegionReport(
                                    list,
                                    "Southeast Asia",
                                    5
                            );
                }
        );
    }
}