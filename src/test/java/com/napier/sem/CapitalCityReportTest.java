package com.napier.sem;

import com.napier.sem.models.CapitalCity;
import com.napier.sem.reports.CapitalCityReport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CapitalCityReportTest {
    static CapitalCityReport report;
    static App app;

    @BeforeAll
    static void init() {
        report = new CapitalCityReport(null);
    }

    @AfterAll
    static void cleanup() {
        if (app != null) {
            app.disconnect();
        }
    }

    @Test
    void testGetCapitalCitiesByContinentNullConnection() {
        List<CapitalCity> cities = report.getCapitalCitiesByContinent("Asia");
        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testGetCapitalCitiesByContinentEmptyContinent() {
        List<CapitalCity> cities = report.getCapitalCitiesByContinent("");
        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testGetCapitalCitiesByContinentNullContinent() {
        List<CapitalCity> cities = report.getCapitalCitiesByContinent(null);
        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testWorldDBGetCapitalCitiesByContinent() {
        app = new App();
        app.connect("localhost:33060", 0);
        assertNotNull(app.getConnection(), "Database connection should be established");
        CapitalCityReport dbReport = new CapitalCityReport(app.getConnection());

        List<CapitalCity> cities = dbReport.getCapitalCitiesByContinent("Asia");

        //verify results are retrieved
        assertNotNull(cities);
        assertFalse(cities.isEmpty(), "Asia should contain capital cities");

        //verify capital city information is retrieved
        for (CapitalCity city : cities) {
            assertNotNull(city.getName());
            assertNotNull(city.getCountry());
        }

        //verify population is sorted (descending)
        for (int i = 0; i < cities.size() - 1; i++) {
            assertTrue(cities.get(i).getPopulation() >= cities.get(i + 1).getPopulation(),
                    "Capital cities should be sorted by population descending");
        }
    }

}