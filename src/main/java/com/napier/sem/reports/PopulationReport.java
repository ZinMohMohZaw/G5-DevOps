package com.napier.sem.reports;

import com.napier.sem.models.Population;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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
     * US26-T1: Calculate World Population
     * Queries the database country table for total world population sum
     * and stores the result in the existing Population model.
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
                    Population pop = new Population();
                    pop.setName("World");
                    pop.setTotalPopulation(rset.getLong("TotalWorldPopulation"));
                    return pop;
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
}