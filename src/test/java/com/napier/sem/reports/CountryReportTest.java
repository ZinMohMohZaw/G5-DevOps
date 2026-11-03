package com.napier.sem.reports;

import com.napier.sem.models.Country;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryReportTest {
    static CountryReport countryReport;

    @Test
    void testPrintTopNCountriesWorldwideEmptyList() {
        CountryReport report = new CountryReport(null);
        List<Country> emptyList = new ArrayList<Country>();

        // Should handle empty lists gracefully without throwing exceptions
        assertDoesNotThrow(() -> report.printTopNCountriesWorldwideReport(emptyList, 5));
    }

    @Test
    void testPrintTopNCountriesWorldwideNullList() {
        CountryReport report = new CountryReport(null);

        // Should handle null safely
        assertDoesNotThrow(() -> report.printTopNCountriesWorldwideReport(null, 5));
    }

    @Test
    void testGetTopNCountriesWorldwideInvalidN() {
        CountryReport report = new CountryReport(null);

        // Passing an invalid N (<= 0) should return an empty list immediately without querying
        List<Country> result = report.getTopNCountriesWorldwide(0);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}