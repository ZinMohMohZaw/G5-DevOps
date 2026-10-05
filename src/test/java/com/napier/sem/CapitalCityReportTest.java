package com.napier.sem;

import com.napier.sem.models.CapitalCity;
import com.napier.sem.reports.CapitalCityReport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
    void testPrintAllCapitalCitiesByContinentNullConnection() {
        assertDoesNotThrow(() ->
                report.printAllCapitalCitiesByContinent()
        );
    }
    //Integration DB testing for US18-T1
    @Test
    void testWorldDBGetCapitalCitiesByContinent() {
        app = new App();
        app.connect("localhost:33060", 0);

        assertNotNull(
                app.getConnection(),
                "Database connection should be established"
        );

        CapitalCityReport dbReport =
                new CapitalCityReport(app.getConnection());

        List<CapitalCity> cities =
                dbReport.getCapitalCitiesByContinent("Asia");

        assertNotNull(cities);
        assertFalse(
                cities.isEmpty(),
                "Asia should contain capital cities"
        );

        for (CapitalCity city : cities) {
            assertNotNull(city.getName());
            assertNotNull(city.getCountry());
        }

        for (int i = 0; i < cities.size() - 1; i++) {
            assertTrue(
                    cities.get(i).getPopulation()
                            >= cities.get(i + 1).getPopulation(),
                    "Capital cities should be sorted by population descending"
            );
        }
    }


    //Integration DB testing for US18-T2
    @Test
    void testPrintAllCapitalCitiesByContinentWorldDB() {
        app = new App();
        app.connect("localhost:33060", 0);

        assertNotNull(
                app.getConnection(),
                "Database connection should be established"
        );

        CapitalCityReport dbReport =
                new CapitalCityReport(app.getConnection());

        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        System.setOut(new PrintStream(output));

        try {
            dbReport.printAllCapitalCitiesByContinent();
        } finally {
            System.setOut(originalOut);
        }

        String reportOutput =
                output.toString(StandardCharsets.UTF_8);
        System.out.println(reportOutput);

        assertFalse(
                reportOutput.isBlank(),
                "Capital city report should not be empty"
        );

        assertTrue(
                reportOutput.contains("Capital City"),
                "Report should contain Capital City heading"
        );

        assertTrue(
                reportOutput.contains("Country"),
                "Report should contain Country heading"
        );

        assertTrue(
                reportOutput.contains("Population"),
                "Report should contain Population heading"
        );

        // Get the actual continents from the database
        List<String> continents = new ArrayList<>();

        String sql =
                "SELECT DISTINCT Continent " +
                        "FROM country " +
                        "WHERE Continent IS NOT NULL " +
                        "ORDER BY Continent";

        try (
                PreparedStatement pstmt =
                        app.getConnection().prepareStatement(sql);
                ResultSet rset = pstmt.executeQuery()
        ) {
            while (rset.next()) {
                String continent = rset.getString("Continent");

                if (continent != null && !continent.isBlank()) {
                    continents.add(continent);
                }
            }
        } catch (Exception e) {
            fail("Failed to retrieve continents from database: "
                    + e.getMessage());
        }

        assertFalse(
                continents.isEmpty(),
                "Database should contain continents"
        );

        // Verify every database continent has a report section
        for (String continent : continents) {
            assertTrue(
                    reportOutput.contains("CONTINENT: " + continent),
                    "Report should contain section for " + continent
            );
        }
    }

}