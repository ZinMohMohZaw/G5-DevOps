package com.napier.sem.reports;

import com.napier.sem.models.Population;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class PopulationReport {
    /**
     * US26-T1: Calculate World Population
     * Queries the database country table for total world population sum
     * and stores the result in the existing Population model.
     */
    public Population getWorldPopulation(Connection con) {
        if (con == null) {
            System.out.println("Database connection is null.");
            return null;
        }

        try {
            Statement stmt = con.createStatement();
            String strSelect = "SELECT SUM(CAST(Population AS UNSIGNED)) AS TotalWorldPopulation FROM country;";
            ResultSet rset = stmt.executeQuery(strSelect);

            if (rset.next()) {
                Population pop = new Population();
                pop.setName("World");
                pop.setTotalPopulation(rset.getLong("TotalWorldPopulation"));
                return pop;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Failed to calculate world population.");
        }
        return null;
    }
}




