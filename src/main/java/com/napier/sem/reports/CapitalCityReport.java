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
     * Retrieves the distinct continents available in the database.
     *
     * @return list of distinct continents, or an empty list if none are found
     */
    private List<String> getContinents() {
        List<String> continents = new ArrayList<>();
        if (con == null) {
            return continents;
        }

        String strSelect =
                "SELECT DISTINCT Continent " +
                        "FROM country " +
                        "WHERE Continent IS NOT NULL " +
                        "ORDER BY Continent";

        try {
            PreparedStatement pstmt = con.prepareStatement(strSelect);
            ResultSet rset = pstmt.executeQuery();

            while (rset.next()) {
                String continent = rset.getString("Continent");

                if (continent != null && !continent.isBlank()) {
                    continents.add(continent);
                }
            }

            return continents;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to retrieve continents for US18-T2");
            return continents;
        }
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

    /**
     * US18-T2: Generates capital city reports for all continents
     * available in the database.
     *
     * Continents are retrieved dynamically from the country table.
     * Capital cities are retrieved using the US18-T1 method, which sorts them by population
     * in descending order.
     */
    public void printAllCapitalCitiesByContinent() {
        List<String> continents = getContinents();

        for (String continent : continents) {
            List<CapitalCity> capitalCities = getCapitalCitiesByContinent(continent);
            printCapitalCitiesForContinent(continent, capitalCities);
        }
    }

    /**
     * Prints the capital city report for a single continent.
     *
     * @param continent the continent being reported
     * @param capitalCities capital cities belonging to the continent
     */
    private void printCapitalCitiesForContinent(String continent, List<CapitalCity> capitalCities) {
        System.out.println();
        System.out.println("=============================================================================================");
        System.out.println("CONTINENT: " + continent);
        System.out.println("=============================================================================================");
        if (capitalCities == null || capitalCities.isEmpty()) {
            System.out.println("No capital cities found.");
            return;
        }

        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.printf(
                "%-40s %-40s %12s%n",
                "Capital City", "Country", "Population");
        System.out.println("---------------------------------------------------------------------------------------------");

        for (CapitalCity capital : capitalCities) {
            System.out.printf(
                    "%-40s %-40s %12d%n",
                    capital.getName(), capital.getCountry(), capital.getPopulation());
        }

        System.out.println("---------------------------------------------------------------------------------------------");
    }
}