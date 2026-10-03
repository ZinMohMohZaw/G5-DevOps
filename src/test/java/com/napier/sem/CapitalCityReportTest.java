package com.napier.sem;

import com.napier.sem.models.CapitalCity;
import com.napier.sem.reports.CapitalCityReport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
}