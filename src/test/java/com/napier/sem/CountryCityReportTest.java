package com.napier.sem;

import com.napier.sem.models.City;
import com.napier.sem.reports.CityReport;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CountryCityReportTest {

    @Test
    public void testGenerateCountryCityReport() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:33060/world?allowPublicKeyRetrieval=true&useSSL=false",
                    "root",
                    "example"
            );

            CityReport cityReport = new CityReport(con);
            String targetCountry = "United Kingdom";

            List<City> cities = cityReport.getCitiesByCountry(targetCountry);

            // Execute US10-T2 display method from source class
            cityReport.printCitiesByCountryReport(cities, targetCountry);

            assertNotNull(cities, "City list should not be null.");
            assertFalse(cities.isEmpty(), "City list should not be empty.");

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Database test failed: " + e.getMessage());
        }
    }
}