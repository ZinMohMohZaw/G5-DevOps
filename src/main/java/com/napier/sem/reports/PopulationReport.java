package com.napier.sem.reports;

import com.napier.sem.models.Population;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
}