package com.napier.sem.reports;

import com.napier.sem.models.Population;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval, validation and reporting for population data.
 */
public class PopulationReport {

    private final Connection connection;

    /**
     * Constructs the report generator with an active database connection.
     * @param connection Active MySQL database connection
     */
    public PopulationReport(Connection connection) {
        this.connection = connection;
    }

    /**
     * US26-T1: Calculate World Population.
     * Queries the country table for the total world population
     * and stores the result in a Population object.
     *
     * @return Population object containing the total world population,
     *         or null if the database connection or query fails
     */
    public Population getWorldPopulation() {
        if (connection == null) {
            System.out.println("Database connection is null.");
            return null;
        }

        try (Statement stmt = connection.createStatement()) {
            String strSelect = "SELECT SUM(CAST(Population AS UNSIGNED)) AS TotalWorldPopulation FROM country;";
            try (ResultSet rset = stmt.executeQuery(strSelect)) {
                if (rset.next()) {
                    return new Population("World", rset.getLong("TotalWorldPopulation"));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to calculate world population.");
        }
        return null;
    }

    /**
     * US26-T2: Validate World Population Data
     * Validates that the returned Population object is non-null and contains a valid population sum.
     * @param pop Population object to validate
     * @return true if valid, false otherwise
     */
    public boolean validateWorldPopulation(Population pop) {
        if (pop == null) {
            System.out.println("Validation Failed: Population data is null.");
            return false;
        }
        if (pop.getName() == null || pop.getName().trim().isEmpty()) {
            System.out.println("Validation Failed: Population name is missing or empty.");
            return false;
        }
        if (pop.getTotalPopulation() <= 0) {
            System.out.println("Validation Failed: Total population must be greater than 0.");
            return false;
        }
        return true;
    }

    /**
     * US26-T2: Generate World Population Report Output
     * Formats and prints the total world population report.
     * @param pop Population object retrieved from getWorldPopulation
     */
    public void printWorldPopulationReport(Population pop) {
        if (!validateWorldPopulation(pop)) {
            System.out.println("Unable to display World Population Report due to invalid data.");
            return;
        }

        System.out.println("=================================================");
        System.out.println("             WORLD POPULATION REPORT             ");
        System.out.println("=================================================");
        System.out.printf("%-20s | %-20s%n", "Entity", "Total Population");
        System.out.println("-------------------------------------------------");
        System.out.printf("%-20s | %,20d%n", pop.getName(), pop.getTotalPopulation());
        System.out.println("=================================================\n");
    }

    // =========================================================================
    // US27: Population of a Continent
    // =========================================================================

    /**
     * Retrieves the total population of a specific continent.
     *
     * @param continent Name of the continent
     * @return Population object representing the continent's total population
     */
    public Population getPopulationByContinent(String continent) {
        Population population = null;
        if (connection == null || continent == null || continent.isBlank()) {
            return new Population(continent, 0);
        }

        String sql = """
                SELECT Continent, SUM(CAST(Population AS UNSIGNED)) AS TotalPopulation
                FROM country
                WHERE Continent = ?
                GROUP BY Continent
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, continent);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    population = new Population(rs.getString("Continent"), rs.getLong("TotalPopulation"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving population for continent " + continent + " (US27): " + e.getMessage());
        }

        return population != null ? population : new Population(continent, 0);
    }

    /**
     * Prints the population report for a continent.
     *
     * @param pop Population object
     */
    public void printPopulationByContinentReport(Population pop) {
        if (pop == null) {
            System.out.println("No population data available for continent.");
            return;
        }

        System.out.println();
        System.out.println("=================================================");
        System.out.println("          CONTINENT POPULATION REPORT            ");
        System.out.println("=================================================");
        System.out.printf("%-20s | %-20s%n", "Continent", "Total Population");
        System.out.println("-------------------------------------------------");
        System.out.printf("%-20s | %,20d%n", pop.getName(), pop.getTotalPopulation());
        System.out.println("=================================================\n");
    }

    // =========================================================================
    // US28: Population of a Region
    // =========================================================================

    /**
     * Retrieves the total population of a specific region.
     *
     * @param region Name of the region
     * @return Population object representing the region's total population
     */
    public Population getPopulationByRegion(String region) {
        Population population = null;
        if (connection == null || region == null || region.isBlank()) {
            return new Population(region, 0);
        }

        String sql = """
                SELECT Region, SUM(CAST(Population AS UNSIGNED)) AS TotalPopulation
                FROM country
                WHERE Region = ?
                GROUP BY Region
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, region);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    population = new Population(rs.getString("Region"), rs.getLong("TotalPopulation"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving population for region " + region + " (US28): " + e.getMessage());
        }

        return population != null ? population : new Population(region, 0);
    }

    /**
     * Prints the population report for a region.
     *
     * @param pop Population object
     */
    public void printPopulationByRegionReport(Population pop) {
        if (pop == null) {
            System.out.println("No population data available for region.");
            return;
        }

        System.out.println();
        System.out.println("=================================================");
        System.out.println("            REGION POPULATION REPORT             ");
        System.out.println("=================================================");
        System.out.printf("%-20s | %-20s%n", "Region", "Total Population");
        System.out.println("-------------------------------------------------");
        System.out.printf("%-20s | %,20d%n", pop.getName(), pop.getTotalPopulation());
        System.out.println("=================================================\n");
    }

    // =========================================================================
    // US29: Population of a Country
    // =========================================================================

    /**
     * Retrieves the total population of a specific country.
     *
     * @param countryName Name of the country
     * @return Population object representing the country's total population
     */
    public Population getPopulationByCountry(String countryName) {
        Population population = null;
        if (connection == null || countryName == null || countryName.isBlank()) {
            return new Population(countryName, 0);
        }

        String sql = """
                SELECT Name, Population
                FROM country
                WHERE Name = ?
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, countryName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    population = new Population(rs.getString("Name"), rs.getLong("Population"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving population for country " + countryName + " (US29): " + e.getMessage());
        }

        return population != null ? population : new Population(countryName, 0);
    }

    /**
     * Prints the population report for a country.
     *
     * @param pop Population object
     */
    public void printPopulationByCountryReport(Population pop) {
        if (pop == null) {
            System.out.println("No population data available for country.");
            return;
        }

        System.out.println();
        System.out.println("=================================================");
        System.out.println("            COUNTRY POPULATION REPORT            ");
        System.out.println("=================================================");
        System.out.printf("%-20s | %-20s%n", "Country", "Total Population");
        System.out.println("-------------------------------------------------");
        System.out.printf("%-20s | %,20d%n", pop.getName(), pop.getTotalPopulation());
        System.out.println("=================================================\n");
    }

    // =========================================================================
    // US23: City and Non-City Population by Continent
    // =========================================================================

    /**
     * US23:
     * Retrieves population statistics for each continent, including the total
     * population, population living in cities, population not living in cities,
     * and their corresponding percentages.
     *
     * @return List of Population objects containing city and non-city population
     *         statistics for each continent, or an empty list on database error
     */
    public List<Population> getCityPopulationByContinent() {

        List<Population> populations = new ArrayList<>();

        if (connection == null) {
            System.out.println("Database connection is null.");
            return populations;
        }

        String sql = """
            SELECT cp.Continent,
                   cp.TotalPopulation,
                   COALESCE(up.UrbanPopulation, 0) AS UrbanPopulation
            FROM
                (
                    SELECT Continent,
                           SUM(CAST(Population AS UNSIGNED)) AS TotalPopulation
                    FROM country
                    GROUP BY Continent
                ) cp
            LEFT JOIN
                (
                    SELECT c.Continent,
                           SUM(CAST(ci.Population AS UNSIGNED)) AS UrbanPopulation
                    FROM country c
                    INNER JOIN city ci
                        ON c.Code = ci.CountryCode
                    GROUP BY c.Continent
                ) up
                ON cp.Continent = up.Continent
            ORDER BY cp.Continent
            """;

        try (Statement stmt = connection.createStatement();
             ResultSet rset = stmt.executeQuery(sql)) {

            while (rset.next()) {

                String continent = rset.getString("Continent");
                long totalPopulation = rset.getLong("TotalPopulation");
                long urbanPopulation = rset.getLong("UrbanPopulation");

                long nonUrbanPopulation =
                        totalPopulation - urbanPopulation;

                double urbanPercentage = 0.0;
                double nonUrbanPercentage = 0.0;

                if (totalPopulation > 0) {
                    urbanPercentage =
                            ((double) urbanPopulation / totalPopulation) * 100;

                    nonUrbanPercentage =
                            ((double) nonUrbanPopulation / totalPopulation) * 100;
                }

                Population population = new Population(
                        continent,
                        totalPopulation,
                        urbanPopulation,
                        urbanPercentage,
                        nonUrbanPopulation,
                        nonUrbanPercentage
                );

                populations.add(population);
            }

        } catch (Exception e) {
            System.err.println(
                    "Failed to retrieve continent population report for US23: "
                            + e.getMessage()
            );
        }

        return populations;
    }

    /**
     * US23:
     * Prints the city and non-city population report for each continent,
     * including population totals and percentages.
     *
     * @param populations List of continent population statistics
     */
    public void printCityPopulationByContinentReport(
            List<Population> populations) {

        if (populations == null || populations.isEmpty()) {
            System.out.println(
                    "No continent population data available."
            );
            return;
        }

        System.out.println();
        System.out.println(
                "==============================================================================================================="
        );
        System.out.println(
                "                              CONTINENT POPULATION REPORT"
        );
        System.out.println(
                "==============================================================================================================="
        );

        System.out.printf(
                "%-20s | %18s | %18s | %10s | %20s | %10s%n",
                "Continent",
                "Total Population",
                "In Cities",
                "City %",
                "Outside Cities",
                "Outside %"
        );

        System.out.println(
                "---------------------------------------------------------------------------------------------------------------"
        );

        for (Population population : populations) {

            if (population == null) {
                continue;
            }

            System.out.printf(
                    "%-20s | %,18d | %,18d | %9.2f%% | %,20d | %9.2f%%%n",
                    population.getName(),
                    population.getTotalPopulation(),
                    population.getUrbanPopulation(),
                    population.getUrbanPercentage(),
                    population.getNonUrbanPopulation(),
                    population.getNonUrbanPercentage()
            );
        }

        System.out.println(
                "==============================================================================================================="
        );
    }

    // =========================================================================
// US24: City and Non-City Population by Region
// =========================================================================

    /**
     * US24:
     * Retrieves population statistics for each region, including the total
     * population, population living in cities, population not living in cities,
     * and their corresponding percentages.
     *
     * @return List of Population objects containing city and non-city population
     *         statistics for each region, or an empty list on database error
     */
    public List<Population> getCityPopulationByRegion() {

        List<Population> populations = new ArrayList<>();

        if (connection == null) {
            System.out.println("Database connection is null.");
            return populations;
        }

        String sql = """
            SELECT rp.Region,
                   rp.TotalPopulation,
                   COALESCE(up.UrbanPopulation, 0) AS UrbanPopulation
            FROM
                (
                    SELECT Region,
                           SUM(CAST(Population AS UNSIGNED)) AS TotalPopulation
                    FROM country
                    GROUP BY Region
                ) rp
            LEFT JOIN
                (
                    SELECT c.Region,
                           SUM(CAST(ci.Population AS UNSIGNED)) AS UrbanPopulation
                    FROM country c
                    INNER JOIN city ci
                        ON c.Code = ci.CountryCode
                    GROUP BY c.Region
                ) up
                ON rp.Region = up.Region
            ORDER BY rp.Region
            """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                String region =
                        resultSet.getString("Region");

                long totalPopulation =
                        resultSet.getLong("TotalPopulation");

                long urbanPopulation =
                        resultSet.getLong("UrbanPopulation");

                long nonUrbanPopulation =
                        totalPopulation - urbanPopulation;

                double urbanPercentage = 0.0;
                double nonUrbanPercentage = 0.0;

                if (totalPopulation > 0) {

                    urbanPercentage =
                            ((double) urbanPopulation
                                    / totalPopulation) * 100;

                    nonUrbanPercentage =
                            ((double) nonUrbanPopulation
                                    / totalPopulation) * 100;
                }

                Population population = new Population(
                        region,
                        totalPopulation,
                        urbanPopulation,
                        urbanPercentage,
                        nonUrbanPopulation,
                        nonUrbanPercentage
                );

                populations.add(population);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Failed to retrieve regional city population report for US24: "
                            + e.getMessage()
            );
        }

        return populations;
    }

    /**
     * US24:
     * Prints the city and non-city population report for each region,
     * including population totals and percentages.
     *
     * @param populations List of regional population statistics
     */
    public void printCityPopulationByRegionReport(
            List<Population> populations) {

        if (populations == null || populations.isEmpty()) {
            System.out.println(
                    "No regional population data available."
            );
            return;
        }

        System.out.println();
        System.out.println(
                "==============================================================================================================="
        );
        System.out.println(
                "                                REGION POPULATION REPORT"
        );
        System.out.println(
                "==============================================================================================================="
        );

        System.out.printf(
                "%-30s | %18s | %18s | %10s | %20s | %10s%n",
                "Region",
                "Total Population",
                "In Cities",
                "City %",
                "Outside Cities",
                "Outside %"
        );

        System.out.println(
                "-------------------------------------------------------------------------------------------------------------------------"
        );

        for (Population population : populations) {

            if (population == null) {
                continue;
            }

            System.out.printf(
                    "%-30s | %,18d | %,18d | %9.2f%% | %,20d | %9.2f%%%n",
                    population.getName(),
                    population.getTotalPopulation(),
                    population.getUrbanPopulation(),
                    population.getUrbanPercentage(),
                    population.getNonUrbanPopulation(),
                    population.getNonUrbanPercentage()
            );
        }

        System.out.println(
                "==============================================================================================================="
        );
    }

}