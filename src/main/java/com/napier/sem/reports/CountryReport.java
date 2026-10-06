package com.napier.sem.reports;

import com.napier.sem.models.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval and reporting for Country data (US01, US02).
 */
public class CountryReport {

    private final Connection connection;

    /**
     * Constructs the report generator with an active database connection.
     * @param connection Active MySQL database connection
     */
    public CountryReport(Connection connection) {
        this.connection = connection;
    }

    // =========================================================================
    // US01: All Countries Report
    // =========================================================================

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

    // =========================================================================
    // US02: Countries by Continent Report
    // =========================================================================

    /**
     * US02-T1: Retrieve all countries in a specific continent sorted by population (descending).
     * @param continent Name of the continent to filter by (e.g., "Asia", "Europe")
     * @return List of Country objects sorted by population descending, or empty list on error.
     */
    public List<Country> getCountriesByContinent(String continent) {
        List<Country> countries = new ArrayList<>();

        String strSelect =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital " +
                        "FROM country c " +
                        "LEFT JOIN city ci ON c.Capital = ci.ID " +
                        "WHERE c.Continent = ? " +
                        "ORDER BY c.Population DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(strSelect)) {
            pstmt.setString(1, continent);

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    Country country = new Country();
                    country.setCode(rset.getString("Code"));
                    country.setName(rset.getString("Name"));
                    country.setContinent(rset.getString("Continent"));
                    country.setRegion(rset.getString("Region"));
                    country.setPopulation(rset.getInt("Population"));
                    country.setCapital(rset.getString("Capital"));
                    countries.add(country);
                }
            }
            return countries;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to get country report by continent for US02-T1");
            return countries;
        }
    }

    /**
     * US02-T2: Output formatted table of countries by continent.
     * @param countries List of Country objects retrieved from getCountriesByContinent
     * @param continent Target continent name for report header
     */
    public void printCountriesByContinentReport(List<Country> countries, String continent) {
        if (countries == null || countries.isEmpty()) {
            System.out.println("No countries found for continent: " + continent);
            return;
        }

        System.out.println("==========================================================================================================");
        System.out.println("                                CONTINENT COUNTRY REPORT: " + continent.toUpperCase());
        System.out.println("==========================================================================================================");
        System.out.printf("%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                "Code", "Name", "Continent", "Region", "Population", "Capital");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        for (Country c : countries) {
            if (c == null) continue;
            System.out.printf("%-6s | %-35s | %-15s | %-25s | %,12d | %-20s%n",
                    c.getCode(),
                    c.getName(),
                    c.getContinent(),
                    c.getRegion(),
                    c.getPopulation(),
                    c.getCapital() != null ? c.getCapital() : "N/A"
            );
        }

        System.out.println("==========================================================================================================");
        System.out.println("Total Countries Listed: " + countries.size());
        System.out.println("==========================================================================================================\n");
    }
}