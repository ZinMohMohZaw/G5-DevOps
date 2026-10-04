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

                // Capital is stored as a city ID in the database.
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
}