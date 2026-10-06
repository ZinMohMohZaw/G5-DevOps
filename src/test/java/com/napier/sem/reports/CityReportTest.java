package com.napier.sem.reports;

import com.napier.sem.App;
import com.napier.sem.models.City;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityReportTest {

    private App app;
    private Connection connection;
    private CityReport cityReport;

    @BeforeEach
    void setUp() {

        // Create application and connect to the World database
        app = new App();
        app.connect("localhost:33060", 100);

        connection = app.getConnection();

        // Create CityReport using the same connection
        cityReport = new CityReport(connection);
    }

    @AfterEach
    void tearDown() {

        // Close database connection after each test
        app.disconnect();
    }

    /**
     * US07-T2:
     * Tests the generation of the city report output.
     *
     * The report should display:
     * Name, Country, District and Population.
     */
    @Test
    void generateCityReport() {

        // Check that the database connection was created
        assertNotNull(
                connection,
                "Database connection should not be null"
        );

        // Get cities using the completed US07-T1 method
        List<City> cities =
                cityReport.getAllCitiesSortedByPopulation();

        // Check that city data was retrieved
        assertNotNull(
                cities,
                "City list should not be null"
        );

        assertFalse(
                cities.isEmpty(),
                "City list should contain data"
        );

        // Generate the US07-T2 report
        cityReport.generateCityReport(cities);

        // Check that every city has the required report data
        for (City city : cities) {

            assertNotNull(
                    city.getName(),
                    "City name should not be null"
            );

            assertNotNull(
                    city.getCountry(),
                    "Country should not be null"
            );

            assertNotNull(
                    city.getDistrict(),
                    "District should not be null"
            );

            assertTrue(
                    city.getPopulation() >= 0,
                    "Population should not be negative"
            );
        }
    }
}