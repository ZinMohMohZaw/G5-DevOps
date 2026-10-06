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
            // Connect to local MySQL database container
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:33060/world?allowPublicKeyRetrieval=true&useSSL=false",
                    "root",
                    "example"
            );
            System.out.println("Connecting to database...");
            System.out.println("Successfully connected\n----------------------------------------");

            CityReport report = new CityReport(con);
            String targetCountry = "United States"; // Example target country

            // Execute US10-T1 query method
            List<City> cities = report.getCitiesByCountry(targetCountry);

            // US10-T2: Generate Formatted Country City Report Output
            System.out.println("=========================================================================================");
            System.out.println("                               COUNTRY CITY REPORT: " + targetCountry.toUpperCase());
            System.out.println("=========================================================================================");
            System.out.printf("%-35s | %-25s | %-25s | %-12s%n",
                    "Name", "Country", "District", "Population");
            System.out.println("-----------------------------------------------------------------------------------------");

            for (City c : cities) {
                if (c == null) continue;
                System.out.printf("%-35s | %-25s | %-25s | %,12d%n",
                        c.getName(),
                        c.getCountry(),
                        c.getDistrict(),
                        c.getPopulation()
                );
            }
            System.out.println("=========================================================================================");
            System.out.println("Total Cities Listed: " + cities.size());
            System.out.println("=========================================================================================\n");

            // Assertions for verification
            assertNotNull(cities, "City list should not be null.");
            assertFalse(cities.isEmpty(), "City list for country '" + targetCountry + "' should not be empty.");

            // Verify descending population order
            if (cities.size() > 1) {
                assertTrue(cities.get(0).getPopulation() >= cities.get(1).getPopulation(),
                        "Cities are not properly sorted in descending order by population.");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Database test failed: " + e.getMessage());
        }
    }
}