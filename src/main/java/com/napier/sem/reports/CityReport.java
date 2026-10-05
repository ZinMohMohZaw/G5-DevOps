package com.napier.sem.reports;

import com.napier.sem.models.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Comparator;

public class CityReport {

    /**
     * US08-T1:
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


    /**
     * US08-T2:
     * Generates the city report for a selected continent.
     *
     * The cities retrieved by US08-T1 are sorted by
     * population in descending order and displayed
     * using the required city-report format.
     *
     * @param con       database connection
     * @param continent selected continent
     */
    public void generateContinentCityReport(Connection con, String continent) {

        // Use the existing US08-T1 implementation
        ArrayList<City> cities = getCitiesByContinent(con, continent);

        // Sort cities by population from highest to lowest
        cities.sort(
                Comparator.comparingInt(City::getPopulation).reversed()
        );

        // Report heading
        System.out.println();
        System.out.println("==============================================================");
        System.out.println("             CONTINENT CITY REPORT");
        System.out.println("==============================================================");
        System.out.println("Continent: " + continent);
        System.out.println("Total Cities: " + cities.size());
        System.out.println("==============================================================");

        // Display table heading
        System.out.printf(
                "%-30s %-30s %-25s %15s%n",
                "City",
                "Country",
                "District",
                "Population"
        );

        System.out.println("----------------------------------------------------------------------------------------------");

        // Display all city data
        for (City city : cities) {

            System.out.printf(
                    "%-30s %-30s %-25s %,15d%n",
                    city.getName(),
                    city.getCountry(),
                    city.getDistrict(),
                    city.getPopulation()
            );
        }

        System.out.println("----------------------------------------------------------------------------------------------");
        System.out.println("End of Continent City Report");
        System.out.println("==============================================================");
        System.out.println();
    }
}