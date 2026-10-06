package com.napier.sem.reports;

import com.napier.sem.models.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CityReport {

    private final Connection connection;

    /**
     * Constructs the report generator with an active database connection.
     * @param connection Active MySQL database connection
     */
    public CityReport(Connection connection) {
        this.connection = connection;
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

        try (PreparedStatement stmt = connection.prepareStatement(query)) {

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

    /**
     * US10-T2 - Generate Country City Report
     * Outputs a formatted table of all cities in a specific country.
     *
     * @param cities      List of City objects from getCitiesByCountry
     * @param countryName The target country name for report header
     */
    public void printCitiesByCountryReport(List<City> cities, String countryName) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No cities found for country: " + countryName);
            return;
        }

        System.out.println("=========================================================================================");
        System.out.println("                               COUNTRY CITY REPORT: " + countryName.toUpperCase());
        System.out.println("=========================================================================================");
        System.out.printf("%-35s | %-25s | %-25s | %-12s%n",
                "Name", "Country", "District", "Population");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (City c : cities) {
            if (c == null) continue;
            System.out.printf("%-35s | %-25s | %-25s | %,12d%n",
                    c.getName(),
                    c.getCountry(),
                    c.getDistrict(),
                    c.getPopulation()
            );
        }

        System.out.println("=========================================================================================");
        System.out.println("Total Cities Listed: " + cities.size());
        System.out.println("=========================================================================================\n");
    }
}