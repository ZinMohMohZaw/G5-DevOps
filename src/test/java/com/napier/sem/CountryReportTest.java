package com.napier.sem;
import com.napier.sem.models.Country;
import com.napier.sem.reports.CountryReport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CountryReportTest {
    @Test
    void testGetCountriesByContinent() {

        // Connect to database
        App app = new App();
        app.connect("localhost:33060", 3000);

        // Create CountryReport using the database connection
        CountryReport report = new CountryReport(app.getConnection());

        // US02-T1: Retrieve countries in Asia
        List<Country> countries = report.getCountriesByContinent("Asia");

        // 1. Make sure results were returned
        assertNotNull(countries);
        assertFalse(countries.isEmpty());

        // 2. Make sure every country belongs to Asia
        for (Country country : countries) {
            assertEquals("Asia", country.getContinent());
        }

        // 3. Make sure countries are sorted by population descending
        for (int i = 0; i < countries.size() - 1; i++) {
            assertTrue(
                    countries.get(i).getPopulation()
                            >= countries.get(i + 1).getPopulation()
            );
        }

        // Disconnect
        app.disconnect();
    }
}
