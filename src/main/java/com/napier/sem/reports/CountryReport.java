package com.napier.sem.reports;

import com.napier.sem.models.Country;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval and reporting for Country data.
 * Task Reference: US02-T1
 */
public class CountryReport {

    private final Connection con;

    /**
     * Constructs the report generator with an active database connection.
     * @param con Active MySQL database connection
     */
    public CountryReport(Connection con) {
        this.con = con;
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

        try {
            // US02-T1: Use PreparedStatement to safely bind the continent parameter
            PreparedStatement pstmt = con.prepareStatement(strSelect);
            pstmt.setString(1, continent);
            ResultSet rset = pstmt.executeQuery();

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
            return countries;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country report by continent for US02-T1");
            return countries;
        }
    }
}