package com.napier.sem.reports;

import com.napier.sem.models.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CityReport {

    /**
     * Retrieves all cities belonging to countries
     * within the selected continent.
     *
     * @param con       database connection
     * @param continent selected continent
     * @return list of cities in the continent
     */
    public ArrayList<City> getCitiesByContinent(Connection con, String continent) {

        ArrayList<City> cities = new ArrayList<>();

        String sql =
                "SELECT city.Name AS CityName, " +
                        "country.Name AS CountryName, " +
                        "city.District, " +
                        "city.Population " +
                        "FROM city " +
                        "INNER JOIN country " +
                        "ON city.CountryCode = country.Code " +
                        "WHERE country.Continent = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, continent);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    City city = new City();

                    city.setName(rs.getString("CityName"));
                    city.setCountry(rs.getString("CountryName"));
                    city.setDistrict(rs.getString("District"));
                    city.setPopulation(rs.getInt("Population"));

                    cities.add(city);
                }
            }

        } catch (Exception e) {
            System.out.println(
                    "Failed to retrieve cities for continent: " + continent
            );
            e.printStackTrace();
        }

        return cities;
    }
}