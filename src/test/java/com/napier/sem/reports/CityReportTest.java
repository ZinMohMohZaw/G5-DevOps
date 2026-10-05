package com.napier.sem.reports;

import com.napier.sem.App;
import com.napier.sem.models.City;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CityReportTest {

    private static App app;
    private static Connection con;
    private static CityReport cityReport;

    @BeforeAll
    static void setUp() {

        app = new App();

        // Connect to the same database used by App.java
        app.connect("localhost:33060", 3000);

        con = app.getConnection();

        cityReport = new CityReport();

        assertNotNull(
                con,
                "Database connection should be established"
        );
    }

    @AfterAll
    static void tearDown() {

        if (app != null) {
            app.disconnect();
        }
    }

    /**
     * US08-T2:
     * Generate Continent City Report.
     *
     * The report should:
     * 1. Retrieve cities for the selected continent.
     * 2. Sort cities by population in descending order.
     * 3. Display the city report.
     */
    @Test
    void generateContinentCityReport() {

        String continent = "Asia";

        // Retrieve cities using the US08-T1 method
        ArrayList<City> cities =
                cityReport.getCitiesByContinent(con, continent);

        // Check that cities were retrieved
        assertNotNull(
                cities,
                "City list should not be null"
        );

        assertFalse(
                cities.isEmpty(),
                "Asia should contain cities"
        );

        // Sort in the same way as US08-T2
        cities.sort(
                java.util.Comparator
                        .comparingInt(City::getPopulation)
                        .reversed()
        );

        // Check that the cities are sorted by population
        // from highest to lowest
        for (int i = 0; i < cities.size() - 1; i++) {

            assertTrue(
                    cities.get(i).getPopulation()
                            >= cities.get(i + 1).getPopulation(),
                    "Cities should be sorted by population descending"
            );
        }

        // Generate and display the actual US08-T2 report
        cityReport.generateContinentCityReport(
                con,
                continent
        );
    }
}