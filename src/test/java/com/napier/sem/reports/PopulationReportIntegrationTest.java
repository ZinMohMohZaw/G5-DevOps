package com.napier.sem.reports;

import com.napier.sem.models.Population;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the PopulationReport class.
 * Verifies population retrieval, report generation,
 * missing-data handling and validation using the World database.
 */
class PopulationReportIntegrationTest {

    private static Connection con;
    private static PopulationReport report;

    /**
     * Establishes a connection to the World database and creates
     * the PopulationReport instance before all integration tests.
     */
    @BeforeAll
    static void init() {

        try {

            Class.forName(
                    "com.mysql.cj.jdbc.Driver"
            );

        } catch (ClassNotFoundException e) {

            fail(
                    "Could not load SQL driver: "
                            + e.getMessage()
            );
        }

        int retries = 10;

        for (int i = 0;
             i < retries;
             ++i) {

            System.out.println(
                    "Connecting to database for Population tests..."
            );

            try {

                con =
                        DriverManager.getConnection(
                                "jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false",
                                "root",
                                "example"
                        );

                if (con != null) {

                    System.out.println(
                            "Successfully connected!"
                    );

                    break;
                }

            } catch (SQLException sqle) {

                try {

                    Thread.sleep(5000);

                } catch (InterruptedException ie) {

                    Thread.currentThread()
                            .interrupt();
                }
            }
        }

        if (con == null) {

            fail(
                    "Could not establish database connection after multiple retries."
            );
        }

        report =
                new PopulationReport(con);
    }

    /**
     * Tests retrieval of the total world population
     * and verifies that a valid population is returned.
     */
    @Test
    void testGetWorldPopulation() {

        Population pop =
                report.getWorldPopulation();

        assertAll(
                "World population",

                () -> assertNotNull(pop),

                () -> assertTrue(
                        pop.getTotalPopulation() > 0
                )
        );

        report.printWorldPopulationReport(pop);
    }

    /**
     * Tests population retrieval for a continent
     * and verifies the returned continent and population values.
     */
    @Test
    void testGetPopulationByContinent() {

        Population pop =
                report
                        .getPopulationByContinent(
                                "Asia"
                        );

        assertAll(
                "Continent population",

                () -> assertNotNull(pop),

                () -> assertEquals(
                        "Asia",
                        pop.getName()
                ),

                () -> assertTrue(
                        pop.getTotalPopulation() > 0
                )
        );

        report.printPopulationByContinentReport(
                pop
        );
    }

    /**
     * Tests population retrieval for a region
     * and verifies the returned region and population values.
     */
    @Test
    void testGetPopulationByRegion() {

        Population pop =
                report.getPopulationByRegion(
                        "Southeast Asia"
                );

        assertAll(
                "Region population",

                () -> assertNotNull(pop),

                () -> assertEquals(
                        "Southeast Asia",
                        pop.getName()
                ),

                () -> assertTrue(
                        pop.getTotalPopulation() > 0
                )
        );

        report.printPopulationByRegionReport(pop);
    }

    /**
     * Tests population retrieval for a country
     * and verifies the returned country and population values.
     */
    @Test
    void testGetPopulationByCountry() {

        Population pop =
                report.getPopulationByCountry(
                        "Thailand"
                );

        assertAll(
                "Country population",

                () -> assertNotNull(pop),

                () -> assertEquals(
                        "Thailand",
                        pop.getName()
                ),

                () -> assertTrue(
                        pop.getTotalPopulation() > 0
                )
        );

        report.printPopulationByCountryReport(pop);
    }

    /**
     * Tests retrieval of urban and non-urban population
     * statistics grouped by continent.
     */
    @Test
    void testGetCityPopulationByContinent() {

        List<Population> list =
                report
                        .getCityPopulationByContinent();

        assertAll(
                "City population by continent",

                () -> assertNotNull(list),

                () -> assertFalse(
                        list.isEmpty()
                )
        );

        report.printCityPopulationByContinentReport(
                list
        );
    }

    /**
     * Tests retrieval of urban and non-urban population
     * statistics grouped by region.
     */
    @Test
    void testGetCityPopulationByRegion() {

        List<Population> list =
                report
                        .getCityPopulationByRegion();

        assertAll(
                "City population by region",

                () -> assertNotNull(list),

                () -> assertFalse(
                        list.isEmpty()
                )
        );

        report.printCityPopulationByRegionReport(
                list
        );
    }

    /**
     * Tests retrieval of urban and non-urban population
     * statistics grouped by country.
     */
    @Test
    void testGetCityPopulationByCountry() {

        List<Population> list =
                report
                        .getCityPopulationByCountry();

        assertAll(
                "City population by country",

                () -> assertNotNull(list),

                () -> assertFalse(
                        list.isEmpty()
                )
        );

        report.printCityPopulationByCountryReport(
                list
        );
    }

    /**
     * Tests population retrieval for a district
     * and verifies the returned district information.
     */
    @Test
    void testGetPopulationByDistrict() {

        Population pop =
                report.getPopulationByDistrict(
                        "Bangkok"
                );

        assertAll(
                "District population",

                () -> assertNotNull(pop),

                () -> assertEquals(
                        "Bangkok",
                        pop.getName()
                ),

                () -> assertTrue(
                        pop.getTotalPopulation() > 0
                )
        );

        report.printPopulationByDistrictReport(pop);
    }

    /**
     * Tests population retrieval for a specific city
     * within the selected country.
     */
    @Test
    void testGetPopulationByCity() {

        Population pop =
                report.getPopulationByCity(
                        "Bangkok",
                        "Thailand"
                );

        assertAll(
                "City population",

                () -> assertNotNull(pop),

                () -> assertEquals(
                        "Bangkok",
                        pop.getName()
                ),

                () -> assertTrue(
                        pop.getTotalPopulation() > 0
                )
        );

        report.printPopulationByCityReport(pop);
    }

    /**
     * Tests population retrieval for a continent that
     * does not exist in the database.
     */
    @Test
    void testGetPopulationByContinentNotFound() {

        Population pop =
                report.getPopulationByContinent(
                        "NonExistentContinent"
                );

        assertAll(
                "Missing continent",

                () -> assertNotNull(pop),

                () -> assertEquals(
                        0,
                        pop.getTotalPopulation()
                )
        );

        report.printPopulationByContinentReport(
                pop
        );
    }

    /**
     * Tests population retrieval for a region that
     * does not exist in the database.
     */
    @Test
    void testGetPopulationByRegionNotFound() {

        Population pop =
                report.getPopulationByRegion(
                        "NonExistentRegion"
                );

        assertAll(
                "Missing region",

                () -> assertNotNull(pop),

                () -> assertEquals(
                        0,
                        pop.getTotalPopulation()
                )
        );
    }

    /**
     * Tests population retrieval for a country that
     * does not exist in the database.
     */
    @Test
    void testGetPopulationByCountryNotFound() {

        Population pop =
                report.getPopulationByCountry(
                        "Atlantis"
                );

        assertAll(
                "Missing country",

                () -> assertNotNull(pop),

                () -> assertEquals(
                        0,
                        pop.getTotalPopulation()
                )
        );
    }

    /**
     * Tests that an unknown district returns
     * no matching population record.
     */
    @Test
    void testGetPopulationByDistrictNotFound() {

        Population pop =
                report.getPopulationByDistrict(
                        "NonExistentDistrictXYZ"
                );

        assertNull(pop);
    }

    /**
     * Tests that an unknown city returns
     * no matching population record.
     */
    @Test
    void testGetPopulationByCityNotFound() {

        Population pop =
                report.getPopulationByCity(
                        "AtlantisCity",
                        "Thailand"
                );

        assertNull(pop);
    }

    /**
     * Tests that population report output methods safely
     * handle lists containing null Population elements.
     */
    @Test
    void testPrintReportsWithNullElements() {

        List<Population> continentList =
                new ArrayList<>();

        continentList.add(null);

        List<Population> regionList =
                new ArrayList<>();

        regionList.add(null);

        List<Population> countryList =
                new ArrayList<>();

        countryList.add(null);

        assertAll(
                "Null report elements",

                () -> assertDoesNotThrow(
                        () -> report
                                .printCityPopulationByContinentReport(
                                        continentList
                                )
                ),

                () -> assertDoesNotThrow(
                        () -> report
                                .printCityPopulationByRegionReport(
                                        regionList
                                )
                ),

                () -> assertDoesNotThrow(
                        () -> report
                                .printCityPopulationByCountryReport(
                                        countryList
                                )
                )
        );
    }

    /**
     * Tests the validation branches for null, empty and blank
     * city and country parameters.
     */
    @Test
    void testGetPopulationByCityShortCircuitBranches() {

        assertAll(
                "City parameter validation",

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
                                "   ",
                                "Thailand"
                        )
                ),

                () -> assertNull(
                        report.getPopulationByCity(
                                "Bangkok",
                                "   "
                        )
                )
        );
    }

    /**
     * Tests short-circuit validation branches for invalid
     * world population names.
     */
    @Test
    void testValidateWorldPopulationShortCircuitBranches() {

        assertAll(
                "World population validation",

                () -> assertFalse(
                        report.validateWorldPopulation(
                                new Population(
                                        null,
                                        1000L
                                )
                        )
                ),

                () -> assertFalse(
                        report.validateWorldPopulation(
                                new Population(
                                        "",
                                        1000L
                                )
                        )
                ),

                () -> assertFalse(
                        report.validateWorldPopulation(
                                new Population(
                                        "   ",
                                        1000L
                                )
                        )
                )
        );
    }

    /**
     * Closes the database connection after all
     * PopulationReport integration tests have completed.
     */
    @AfterAll
    static void tearDown() {

        if (con != null) {

            try {

                con.close();

            } catch (SQLException e) {

                System.out.println(
                        "Error closing database connection: "
                                + e.getMessage()
                );
            }
        }
    }
}