package com.napier.sem.reports;

import com.napier.sem.models.Country;
import com.napier.sem.reports.CountryReport;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class RegionCountryReport {

    @Test
    public void testGenerateRegionCountryReport() {
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

            CountryReport countryReport = new CountryReport();
            String targetRegion = "Southern and Central Asia"; // Target region for US03

            // Execute Developer 1's US03-T1 query method
            ArrayList<Country> countries = countryReport.getCountriesByRegion(con, targetRegion);

            // US03-T2: Generate Formatted Region Country Report Output
            System.out.println("========================================================================================================================");
            System.out.println("                                         REGION COUNTRY REPORT: " + targetRegion.toUpperCase());
            System.out.println("========================================================================================================================");
            System.out.printf("%-5s | %-25s | %-15s | %-25s | %-12s | %-20s%n",
                    "Code", "Name", "Continent", "Region", "Population", "Capital");
            System.out.println("------------------------------------------------------------------------------------------------------------------------");

            for (Country c : countries) {
                if (c == null) continue;
                System.out.printf("%-5s | %-25s | %-15s | %-25s | %,12d | %-20s%n",
                        c.getCode(),
                        c.getName(),
                        c.getContinent(),
                        c.getRegion(),
                        c.getPopulation(),
                        c.getCapital()
                );
            }
            System.out.println("========================================================================================================================");
            System.out.println("Total Countries Listed: " + countries.size());
            System.out.println("========================================================================================================================\n");

            // Assertions for verification
            assertNotNull(countries, "Country list should not be null.");
            assertFalse(countries.isEmpty(), "Country list for region '" + targetRegion + "' should not be empty.");

            // Verify descending population order
            if (countries.size() > 1) {
                assertTrue(countries.get(0).getPopulation() >= countries.get(1).getPopulation(),
                        "Countries are not properly sorted in descending order by population.");
            }

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Database test failed: " + e.getMessage());
        }
    }
}