package com.napier.sem.reports;

import com.napier.sem.models.CapitalCity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval and reporting for Capital City data.
 * Task Reference: US17-T1
 */
public class CapitalCityReport {

    private final Connection con;

    public CapitalCityReport(Connection con) {
        this.con = con;
    }

    /**
     * US17-T1: Retrieve all capital cities in the world sorted by population (descending).
     *
     * @return List of CapitalCity objects sorted by population descending, or empty list on error.
     */
    public List<CapitalCity> getAllCapitalCitiesByPopulation() {
        List<CapitalCity> capitalCities = new ArrayList<>();

        // SQL joins country with city on Capital ID and orders by city population descending
        String strSelect =
                "SELECT ci.Name AS Capital, c.Name AS Country, ci.Population " +
                        "FROM country c " +
                        "JOIN city ci ON c.Capital = ci.ID " +
                        "ORDER BY ci.Population DESC";

        try {
            PreparedStatement pstmt = con.prepareStatement(strSelect);
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
            System.out.println("Failed to get capital city report for US17-T1");
            return capitalCities;
        }
    }

    /**
     * Prints the list of capital cities to console.
     */
    public void printCapitalCities(List<CapitalCity> capitalCities) {
        if (capitalCities == null || capitalCities.isEmpty()) {
            System.out.println("No capital cities found.");
            return;
        }
        System.out.printf("%-35s %-35s %-12s%n", "Capital Name", "Country", "Population");
        System.out.println("-".repeat(85));
        for (CapitalCity c : capitalCities) {
            System.out.printf("%-35s %-35s %-12d%n",
                    c.getName(), c.getCountry(), c.getPopulation());
        }
    }
}