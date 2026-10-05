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
     * US10-T1 - Retrieve cities by country.
     *
     * Retrieves all cities belonging to the specified country
     * and sorts them by population from largest to smallest.
     *
     * The returned City objects contain:
     * Name, Country, District and Population.
     *
     * @param country the country to search for
     * @return list of cities in the specified country,
     *         sorted by population descending
     */
    public List<City> getCitiesByCountry(String country) {

        List<City> cities = new ArrayList<>();

        String query =
                "SELECT city.Name, country.Name AS Country, " +
                        "city.District, city.Population " +
                        "FROM city " +
                        "JOIN country ON city.CountryCode = country.Code " +
                        "WHERE country.Name = ? " +
                        "ORDER BY city.Population DESC";

        try (PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, country);

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