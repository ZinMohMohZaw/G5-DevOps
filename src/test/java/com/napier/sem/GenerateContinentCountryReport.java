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
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:33060/world?allowPublicKeyRetrieval=true&useSSL=false",
                    "root",
                    "example"
            );

            CountryReport countryReport = new CountryReport(con);
            String targetContinent = "Asia";

            List<Country> countries = countryReport.getCountriesByContinent(targetContinent);

            // US02-T2: Execute report printing method from source class
            countryReport.printCountriesByContinentReport(countries, targetContinent);

            assertNotNull(countries, "Country list should not be null.");
            assertFalse(countries.isEmpty(), "Country list should not be empty.");

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Database test failed: " + e.getMessage());
        }
    }
}