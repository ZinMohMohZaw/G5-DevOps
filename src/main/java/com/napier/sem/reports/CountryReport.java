package com.napier.sem.reports;

import com.napier.sem.models.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CountryReport {
    /**
     * Retrieves all countries belonging to a selected region
     * and orders them by population from largest to smallest.
     *
     * @param con    database connection
     * @param region selected region
     * @return list of countries in the selected region
     */
    public ArrayList<Country> getCountriesByRegion(Connection con, String region)
    {
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

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, region);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    Country country = new Country();

                    country.setCode(rs.getString("CountryCode"));
                    country.setName(rs.getString("CountryName"));
                    country.setContinent(rs.getString("Continent"));
                    country.setRegion(rs.getString("Region"));
                    country.setPopulation(rs.getInt("Population"));

                    String capital = rs.getString("CapitalName");

                    if (capital != null)
                    {
                        country.setCapital(capital);
                    }
                    else
                    {
                        country.setCapital("N/A");
                    }

                    countries.add(country);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(
                    "Failed to retrieve countries for region: " + region
            );
            e.printStackTrace();
        }

        return countries;
    }
}