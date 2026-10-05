package com.napier.sem.reports;

import com.napier.sem.models.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CityReport {

    private final Connection connection;

    public CityReport(Connection connection) {
        this.connection = connection;
    }

    /**
     * US07-T1:
     * Retrieves all cities from the database and sorts them
     * by population from largest to smallest.
     *
     * @return list of cities sorted by population descending
     */
    public List<City> getAllCitiesSortedByPopulation() {

        List<City> cities = new ArrayList<>();

        String sql = "SELECT Name, CountryCode, District, Population FROM city";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                City city = new City();

                city.setName(resultSet.getString("Name"));
                city.setCountry(resultSet.getString("CountryCode"));
                city.setDistrict(resultSet.getString("District"));
                city.setPopulation(resultSet.getInt("Population"));

                cities.add(city);
            }

        } catch (SQLException e) {
            System.out.println("Error retrieving cities: " + e.getMessage());
        }

        // Sort cities by population from largest to smallest
        cities.sort(Comparator.comparingInt(City::getPopulation).reversed());

        return cities;
    }
}