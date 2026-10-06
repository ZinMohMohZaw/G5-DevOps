package com.napier.sem.reports;

import com.napier.sem.models.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CountryReport {

    private final Connection connection;

    public CountryReport(Connection connection) {
        this.connection = connection;
    }

    /**
     * US03-T1:
     * Retrieves all countries belonging to a selected region
     * and orders them by population from largest to smallest.
     *
     * @param region selected region
     * @return list of countries in the selected region
     */
    public ArrayList<Country> getCountriesByRegion(String region) {

        ArrayList<Country> countries = new ArrayList<>();

        String sql =
                "SELECT co.Code AS CountryCode, " +
                        "co.Name AS CountryName, " +
                        "co.Continent, " +
                        "co.Region, " +
                        "co.Population, " +
                        "cap.Name AS CapitalName " +
                        "FROM country co " +
                        "LEFT JOIN city cap ON co.Capital = cap.ID " +
                        "WHERE co.Region = ? " +
                        "ORDER BY co.Population DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, region);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Country country = new Country();

                    country.setCode(rs.getString("CountryCode"));
                    country.setName(rs.getString("CountryName"));
                    country.setContinent(rs.getString("Continent"));
                    country.setRegion(rs.getString("Region"));
                    country.setPopulation(rs.getInt("Population"));

                    String capital = rs.getString("CapitalName");

                    if (capital != null) {
                        country.setCapital(capital);
                    } else {
                        country.setCapital("N/A");
                    }

                    countries.add(country);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Failed to retrieve countries for region: " + region
            );
            e.printStackTrace();
        }

        return countries;
    }

    /**
     * US03-T2: Generate Region Country Report
     * Outputs a formatted table of all countries in a specific region.
     *
     * @param countries  List of Country objects retrieved from getCountriesByRegion
     * @param regionName Target region name for report header
     */
    public void printCountriesByRegion(
            ArrayList<Country> countries,
            String regionName) {

        if (countries == null || countries.isEmpty()) {
            System.out.println(
                    "No countries found for region: " + regionName
            );
            return;
        }

        System.out.println(
                "=========================================================================================================="
        );
        System.out.println(
                "                                         REGION COUNTRY REPORT: "
                        + regionName.toUpperCase()
        );
        System.out.println(
                "=========================================================================================================="
        );

        System.out.printf(
                "%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                "Code",
                "Name",
                "Continent",
                "Region",
                "Population",
                "Capital"
        );

        System.out.println(
                "----------------------------------------------------------------------------------------------------------"
        );

        for (Country c : countries) {

            if (c == null) {
                continue;
            }

            System.out.printf(
                    "%-6s | %-35s | %-15s | %-25s | %,12d | %-20s%n",
                    c.getCode(),
                    c.getName(),
                    c.getContinent(),
                    c.getRegion(),
                    c.getPopulation(),
                    c.getCapital() != null ? c.getCapital() : "N/A"
            );
        }

        System.out.println(
                "=========================================================================================================="
        );
        System.out.println(
                "Total Countries Listed: " + countries.size()
        );
        System.out.println(
                "=========================================================================================================="
        );
    }
}