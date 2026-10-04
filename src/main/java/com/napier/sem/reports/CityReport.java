package com.napier.sem.reports;

import com.napier.sem.models.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityReport {

    private Connection con;

    public CityReport(Connection con) {
        this.con = con;
    }

    /**
     * US09-T1 - Retrieve cities by region.
     *
     * Retrieves the cities belonging to the specified country region.
     * The returned City objects contain:
     * Name, Country, District and Population.
     *
     * @param region the region to search for
     * @return list of cities in the specified region
     */
    public List<City> getCitiesByRegion(String region) {
        List<City> cities = new ArrayList<>();

        String query =
                "SELECT city.Name, country.Name AS Country, " +
                        "city.District, city.Population " +
                        "FROM city " +
                        "JOIN country ON city.CountryCode = country.Code " +
                        "WHERE country.Region = ? " +
                        "ORDER BY city.Population DESC";

        try (PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, region);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    City city = new City();

                    city.setName(rs.getString("Name"));
                    city.setCountry(rs.getString("Country"));
                    city.setDistrict(rs.getString("District"));
                    city.setPopulation(rs.getInt("Population"));

                    cities.add(city);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cities;
    }
}