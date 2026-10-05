package com.napier.sem;

import com.napier.sem.models.City;
import com.napier.sem.reports.CityReport;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RegionCityReportTestRunner {

    @Test
    public void testGenerateRegionCityReport() {
        try {
            // Connect to local MySQL container
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:33060/world?allowPublicKeyRetrieval=true&useSSL=false",
                    "root",
                    "example"
            );
            System.out.println("Connecting to database...");
            System.out.println("Successfully connected\n----------------------------------------");

            CityReport report = new CityReport(con);
            String targetRegion = "Caribbean"; // Example region

            // Execute US09-T1 query method
            List<City> cities = report.getCitiesByRegion(targetRegion);

            // Display Formatted Region City Report (US09-T2)
            System.out.println("=========================================================================================");
            System.out.println("                               REGION CITY REPORT: " + targetRegion.toUpperCase());
            System.out.println("=========================================================================================");
            System.out.printf("%-35s | %-25s | %-25s | %-12s%n",
                    "Name", "Country", "District", "Population");
            System.out.println("-----------------------------------------------------------------------------------------");

            for (City c : cities) {
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

            // Validations
            assertNotNull(cities, "City list should not be null.");
            assertFalse(cities.isEmpty(), "City list for region '" + targetRegion + "' should not be empty.");

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