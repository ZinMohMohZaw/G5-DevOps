package com.napier.sem.reports;

import com.napier.sem.models.CapitalCity;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval and reporting for Capital Cities.
 * Task Reference: US17-T1
 */
public class CapitalCityReport {

    private final Connection con;

    /**
     * Constructs the report generator with an active database connection.
     * @param con Active MySQL database connection
     */
    public CapitalCityReport(Connection con) {
        this.con = con;
    }

    /**
     * US17-T1: Retrieve and sort all capital cities in the world by population (descending).
     *
     * @return List of CapitalCity objects sorted by population descending, or empty list on error.
     */
    public List<CapitalCity> getWorldCapitalCities() {
        List<CapitalCity> capitalCities = new ArrayList<>();

        // US17-T1: SQL query to join city and country tables where city ID matches country capital ID
        String strSelect =
                "SELECT city.Name AS CityName, country.Name AS CountryName, city.Population " +
                        "FROM city " +
                        "JOIN country ON city.ID = country.Capital " +
                        "ORDER BY city.Population DESC";

        try {
            // US17-T1: Execute query using JDBC connection
            Statement stmt = con.createStatement();
            ResultSet rset = stmt.executeQuery(strSelect);

            // US17-T1: Extract database rows into CapitalCity model objects
            while (rset.next()) {
                CapitalCity capCity = new CapitalCity();
                capCity.setName(rset.getString("CityName"));
                capCity.setCountry(rset.getString("CountryName"));
                capCity.setPopulation(rset.getInt("Population"));
                capitalCities.add(capCity);
            }
            return capitalCities;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get capital city report for US17-T1");
            return capitalCities;
        }
    }
}