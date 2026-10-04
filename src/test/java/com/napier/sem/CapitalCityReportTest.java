package com.napier.sem;

import com.napier.sem.models.CapitalCity;
import com.napier.sem.reports.CapitalCityReport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class CapitalCityReportTest {

    static CapitalCityReport report;

    @BeforeAll
    static void init() {
        report = new CapitalCityReport(null);
    }

    @Test
    void testGetAllCapitalCitiesByPopulationNullConnection() {
        List<CapitalCity> cities = report.getAllCapitalCitiesByPopulation();
        assertNotNull(cities);
        assertTrue(cities.isEmpty());
    }

    @Test
    void testPrintCapitalCitiesNull() {
        assertDoesNotThrow(() -> report.printCapitalCities(null));
    }

    @Test
    void testPrintCapitalCitiesEmpty() {
        List<CapitalCity> cities = new ArrayList<>();
        assertDoesNotThrow(() -> report.printCapitalCities(cities));
    }

    @Test
    void testPrintCapitalCitiesNormal() {
        List<CapitalCity> cities = new ArrayList<>();
        CapitalCity c = new CapitalCity();
        c.setName("Seoul");
        c.setCountry("South Korea");
        c.setPopulation(9981619);
        cities.add(c);

        assertDoesNotThrow(() -> report.printCapitalCities(cities));
    }

    @Test
    void testWorldDatabaseCapitalCityReport() {
        App app = new App();
        app.connect("localhost:33060", 0);
        Connection connection = app.getConnection();
        assertNotNull(connection, "Database connection is established");
        CapitalCityReport worldReport = new CapitalCityReport(connection);

        List<CapitalCity> cities = worldReport.getAllCapitalCitiesByPopulation();
        assertNotNull(cities);
        assertFalse(cities.isEmpty(), "No capital cities were retrieved " +
                "from the database");
        System.out.println("Number of capital cities retrieved: " + cities.size());

        worldReport.printCapitalCities(cities);

        //verify descending population order
        for (int i = 0; i < cities.size() - 1; i++) {
            assertTrue(
                    cities.get(i).getPopulation() >= cities.get(i + 1).getPopulation(),
                    "Capital cities are not sorted by population (descending)"
            );
        }

        app.disconnect();
    }
}