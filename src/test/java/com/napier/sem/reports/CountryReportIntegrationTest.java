package com.napier.sem.reports;

import com.napier.sem.models.Country;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class CountryReportIntegrationTest {
    private static Connection con;
    private static CountryReport countryReport;

    @BeforeAll
    static void init() {
        // Connect to the Docker MySQL database container using standard test credentials/port
        try {
            // Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            fail("Could not load SQL driver: " + e.getMessage());
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                // Database connection string mapping to local Docker port 3306
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/world?allowPublicKeyRetrieval=true&useSSL=false", "root", "example");
                if (con != null) {
                    System.out.println("Successfully connected!");
                    break;
                }
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + (i + 1));
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (con == null) {
            fail("Could not establish database connection after multiple retries.");
        }

        countryReport = new CountryReport(con);
    }

    @Test
    void testGetAllCountriesSorted() {
        List<Country> countries = countryReport.getAllCountriesSorted();
        assertNotNull(countries);
        assertFalse(countries.isEmpty(), "Countries list should not be empty from the database");
    }

    @Test
    void testGetCountriesByContinent() {
        List<Country> asianCountries = countryReport.getCountriesByContinent("Asia");
        assertNotNull(asianCountries);
        assertFalse(asianCountries.isEmpty(), "Asian countries list should not be empty");

        // Verify that the retrieved countries actually belong to Asia
        for (Country c : asianCountries) {
            assertEquals("Asia", c.getContinent());
        }
    }

    @AfterAll
    static void tearDown() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
}
