package com.napier.sem;

import com.napier.sem.models.Country;
import com.napier.sem.reports.CountryReport;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GenerateContinentCountryReport {

    @Test
    public void testGenerateContinentCountryReport() {
        try {
            // Establish connection to local Docker MySQL container
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:33060/world?allowPublicKeyRetrieval=true&useSSL=false",
                    "root",
                    "example"
            );
            System.out.println("Connecting to database...");
            System.out.println("Successfully connected\n----------------------------------------");

            CountryReport report = new CountryReport(con);
            String targetContinent = "Europe";

            // Execute US02-T1 query method
            List<Country> countries = report.getCountriesByContinent(targetContinent);

            // Display Formatted Continent Country Report (US02-T2)
            System.out.println("==========================================================================================================");
            System.out.println("                               CONTINENT COUNTRY REPORT: " + targetContinent.toUpperCase());
            System.out.println("==========================================================================================================");
            System.out.printf("%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                    "Code", "Name", "Continent", "Region", "Population", "Capital");
            System.out.println("----------------------------------------------------------------------------------------------------------");

            for (Country c : countries) {
                System.out.printf("%-6s | %-35s | %-15s | %-25s | %,12d | %-20s%n",
                        c.getCode(),
                        c.getName(),
                        c.getContinent(),
                        c.getRegion(),
                        c.getPopulation(),
                        c.getCapital() != null ? c.getCapital() : "N/A"
                );
            }
            System.out.println("==========================================================================================================");
            System.out.println("Total Countries Listed: " + countries.size());
            System.out.println("==========================================================================================================\n");

            // Validations
            assertNotNull(countries, "Country list should not be null.");
            assertFalse(countries.isEmpty(), "Country list for " + targetContinent + " should not be empty.");

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