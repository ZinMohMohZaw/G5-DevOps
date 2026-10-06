package com.napier.sem.reports;

import com.napier.sem.models.Country;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval and reporting for Country data.
 * Task Reference: US02-T1, US02-T2
 */
public class CountryReport {

    private final Connection connection;

    /**
     * Constructs the report generator with an active database connection.
     * @param connection Active MySQL database connection
     */
    public CountryReport(Connection connection) {
        this.connection = connection;
    }

    /**
     * US02-T1: Retrieve all countries in a specific continent sorted by population (descending).
     * @param continent Name of the continent to filter by (e.g., "Asia", "Europe")
     * @return List of Country objects sorted by population descending, or empty list on error.
     */
    public List<Country> getCountriesByContinent(String continent) {
        List<Country> countries = new ArrayList<>();

        // US02-T1: JOIN country with city to retrieve the capital city's name as a String
        String strSelect =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital " +
                        "FROM country c " +
                        "LEFT JOIN city ci ON c.Capital = ci.ID " +
                        "WHERE c.Continent = ? " +
                        "ORDER BY c.Population DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(strSelect)) {
            // US02-T1: Use PreparedStatement to safely bind the continent parameter
            pstmt.setString(1, continent);

            try (ResultSet rset = pstmt.executeQuery()) {
                // US02-T1: Map result set rows to Country model objects
                while (rset.next()) {
                    Country country = new Country();
                    country.setCode(rset.getString("Code"));
                    country.setName(rset.getString("Name"));
                    country.setContinent(rset.getString("Continent"));
                    country.setRegion(rset.getString("Region"));
                    country.setPopulation(rset.getInt("Population"));
                    country.setCapital(rset.getString("Capital")); // Retrieves the capital city name as String
                    countries.add(country);
                }
            }
            return countries;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country report by continent for US02-T1");
            return countries;
        }
    }

    /**
     * US02-T2: Output formatted table of countries by continent.
     * @param countries List of Country objects retrieved from getCountriesByContinent
     * @param continent Target continent name for report header
     */
    public void printCountriesByContinentReport(List<Country> countries, String continent) {
        if (countries == null || countries.isEmpty()) {
            System.out.println("No countries found for continent: " + continent);
            return;
        }

        // US02-T2: Header layout
        System.out.println("==========================================================================================================");
        System.out.println("                                CONTINENT COUNTRY REPORT: " + continent.toUpperCase());
        System.out.println("==========================================================================================================");
        System.out.printf("%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                "Code", "Name", "Continent", "Region", "Population", "Capital");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        // US02-T2: Formatted row output
        for (Country c : countries) {
            if (c == null) continue;
            System.out.printf("%-6s | %-35s | %-15s | %-25s | %,12d | %-20s%n",
                    c.getCode(),
                    c.getName(),
                    c.getContinent(),
                    c.getRegion(),
                    c.getPopulation(),
                    c.getCapital() != null ? c.getCapital() : "N/A"
            );
        }

        // US02-T2: Footer summary
        System.out.println("==========================================================================================================");
        System.out.println("Total Countries Listed: " + countries.size());
        System.out.println("==========================================================================================================\n");
    }
}