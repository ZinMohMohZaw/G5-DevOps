package com.napier.sem.reports;

import com.napier.sem.models.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CountryReport {

    private final Connection connection;

    public CountryReport(Connection connection) {
        this.connection = connection;
    }

    /**
     * US01-T1:
     * Retrieves all countries from the database and sorts
     * them by population from highest to lowest.
     *
     * @return list of countries sorted by population descending
     * @throws SQLException if the database query fails
     */
    public List<Country> getAllCountriesSorted() throws SQLException {

        String sql = """
                SELECT Code, Name, Continent, Region, Population, Capital
                FROM country
                ORDER BY Population DESC, Name ASC
                """;

        List<Country> countries = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Country country = new Country();

                country.setCode(resultSet.getString("Code"));
                country.setName(resultSet.getString("Name"));
                country.setContinent(resultSet.getString("Continent"));
                country.setRegion(resultSet.getString("Region"));
                country.setPopulation(resultSet.getInt("Population"));

                int capital = resultSet.getInt("Capital");

                if (resultSet.wasNull()) {
                    country.setCapital(null);
                } else {
                    country.setCapital(String.valueOf(capital));
                }

                countries.add(country);
            }
        }

        return countries;
    }

    /**
     * US01-T2:
     * Generates the Country Report Output using the countries
     * retrieved and sorted by US01-T1.
     *
     * @throws SQLException if the database query fails
     */
    public void generateCountryReport() throws SQLException {

        List<Country> countries = getAllCountriesSorted();

        System.out.println("Country Report");
        System.out.println("==============");
        System.out.printf("%-8s %-35s %-18s %-25s %15s %-25s%n",
                "Code",
                "Name",
                "Continent",
                "Region",
                "Population",
                "Capital");

        System.out.println(
                "--------------------------------------------------------------------------------------------------------------"
        );

        for (Country country : countries) {

            String capitalName = getCapitalCityName(country.getCapital());

            System.out.printf("%-8s %-35s %-18s %-25s %15d %-25s%n",
                    country.getCode(),
                    country.getName(),
                    country.getContinent(),
                    country.getRegion(),
                    country.getPopulation(),
                    capitalName);
        }
    }

    /**
     * Retrieves the capital city name using the capital city ID
     * stored in the country table.
     *
     * @param capitalId capital city ID
     * @return capital city name, or "N/A" if no capital is assigned
     * @throws SQLException if the database query fails
     */
    private String getCapitalCityName(String capitalId) throws SQLException {

        if (capitalId == null) {
            return "N/A";
        }

        String sql = """
                SELECT Name
                FROM city
                WHERE ID = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, Integer.parseInt(capitalId));

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getString("Name");
                }
            }
        }

        return "N/A";
    }
}