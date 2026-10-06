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
    /**
     * US09-T2 - Generate Region City Report
     * Outputs a formatted table of all cities in a specific region.
     *
     * @param cities     List of City objects from getCitiesByRegion
     * @param regionName The target region name for report header
     */
    public void printCitiesByRegionReport(List<City> cities, String regionName) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No cities found for region: " + regionName);
            return;
        }

        System.out.println("=========================================================================================");
        System.out.println("                                REGION CITY REPORT: " + regionName.toUpperCase());
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