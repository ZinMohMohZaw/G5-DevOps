// CapitalCityReport.java
package com.napier.sem.reports;
import com.napier.sem.models.CapitalCity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles Database Retrieval for Capital City data.
 * Supports Capital City Reports user stories
 */

public class CapitalCityReport {
    private final Connection con;

    public CapitalCityReport(Connection con) {
        this.con = con;
    }


    /**
     * US18-T1: Retrieves all capital cities within a specified continent,
     * sorted by population (descending).
     *
     * @param continent the continent to retrieve capital cities from
     * @return List of CapitalCity objects sorted by population (descending) or empty list
     * if no results are found or error occurs.
     */
    public List<CapitalCity> getCapitalCitiesByContinent(String continent) {
        List<CapitalCity> capitalCities = new ArrayList<>();

        if (con == null || continent == null || continent.isBlank()) {
            return capitalCities;
        }

        String strSelect = "SELECT ci.Name AS Capital, c.Name AS Country, ci.Population " +
                "FROM country c " +
                "JOIN city ci ON c.Capital = ci.ID " +
                "WHERE c.Continent = ? " +
                "ORDER BY ci.Population DESC";

        try {
            PreparedStatement pstmt = con.prepareStatement(strSelect);
            pstmt.setString(1, continent);
            ResultSet rset = pstmt.executeQuery();

            while (rset.next()) {
                CapitalCity capital = new CapitalCity();
                capital.setName(rset.getString("Capital"));
                capital.setCountry(rset.getString("Country"));
                capital.setPopulation(rset.getInt("Population"));

                capitalCities.add(capital);
            }
            return capitalCities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital cities by continent for US18-T1");
            return capitalCities;
        }
    }
}