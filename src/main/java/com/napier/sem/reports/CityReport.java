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
     * Retrieves all cities from the database and sorts
     * them by population from highest to lowest.
     *
     * @return list of cities sorted by population descending
     * @throws SQLException if the database query fails
     */
    public List<City> getAllCitiesSorted() throws SQLException {

        String sql = """
                SELECT city.Name,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                ORDER BY city.Population DESC, city.Name ASC
                """;

        List<City> cities = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                City city = new City();

                city.setName(resultSet.getString("Name"));
                city.setCountry(resultSet.getString("Country"));
                city.setDistrict(resultSet.getString("District"));
                city.setPopulation(resultSet.getInt("Population"));

                cities.add(city);
            }
        }

        return cities;
    }

    /**
     * US07-T2:
     * Generates the required city report output.
     *
     * Required columns:
     * Name, Country, District, Population
     *
     * @param cities list of cities
     * @return formatted city report
     */
    public String generateCityReportOutput(List<City> cities) {

        StringBuilder report = new StringBuilder();

        report.append(String.format(
                "%-35s %-35s %-25s %15s%n",
                "Name",
                "Country",
                "District",
                "Population"
        ));

        report.append("-----------------------------------------------------------------------------------------------")
                .append(System.lineSeparator());

        for (City city : cities) {

            report.append(String.format(
                    "%-35s %-35s %-25s %,15d%n",
                    city.getName(),
                    city.getCountry(),
                    city.getDistrict(),
                    city.getPopulation()
            ));
        }

        return report.toString();
    }

    /**
     * US08-T2:
     * Sorts and generates a city report for a selected continent.
     *
     * The cities are supplied by the US08-T1 implementation.
     *
     * @param continent selected continent
     * @param cities cities belonging to the selected continent
     * @return formatted continent city report
     */
    public String generateContinentCityReport(
            String continent,
            List<City> cities) {

        List<City> sortedCities = new ArrayList<>(cities);

        sortedCities.sort(
                Comparator.comparingInt(City::getPopulation)
                        .reversed()
                        .thenComparing(City::getName)
        );

        StringBuilder report = new StringBuilder();

        report.append("Continent: ")
                .append(continent)
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        report.append(generateCityReportOutput(sortedCities));

        return report.toString();
    }
}