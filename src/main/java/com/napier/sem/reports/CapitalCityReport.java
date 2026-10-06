package com.napier.sem.reports;

import com.napier.sem.models.CapitalCity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval and reporting for Capital City data.
 * Supports User Stories: US17 and US18.
 */
public class CapitalCityReport {

    private static final String REPORT_HEADER_FORMAT = "%-40s %-40s %12s%n";
    private static final String REPORT_ROW_FORMAT = "%-40s %-40s %12d%n";
    private static final String LINE_SEPARATOR = "---------------------------------------------------------------------------------------------";

    private final Connection con;

    public CapitalCityReport(Connection con) {
        this.con = con;
    }

    /**
     * Retrieves distinct continents present in the database.
     *
     * @return List of distinct continent names, or an empty list if none found.
     */
    private List<String> getContinents() {
        List<String> continents = new ArrayList<>();
        if (con == null) {
            return continents;
        }

        String strSelect = """
                SELECT DISTINCT Continent
                FROM country
                WHERE Continent IS NOT NULL
                ORDER BY Continent
                """;

        try (PreparedStatement pstmt = con.prepareStatement(strSelect);
             ResultSet rset = pstmt.executeQuery()) {

            while (rset.next()) {
                String continent = rset.getString("Continent");
                if (continent != null && !continent.isBlank()) {
                    continents.add(continent);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving continents: " + e.getMessage());
        }

        return continents;
    }

    /**
     * US17-T1: Retrieves all capital cities in the world sorted by population (descending).
     *
     * @return List of CapitalCity objects sorted by population descending, or empty list on error.
     */
    public List<CapitalCity> getAllCapitalCitiesByPopulation() {
        List<CapitalCity> capitalCities = new ArrayList<>();
        if (con == null) {
            return capitalCities;
        }

        String strSelect = """
                SELECT ci.Name AS Capital, c.Name AS Country, ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                ORDER BY ci.Population DESC
                """;

        try (PreparedStatement pstmt = con.prepareStatement(strSelect);
             ResultSet rset = pstmt.executeQuery()) {

            while (rset.next()) {
                capitalCities.add(mapResultSetToCapitalCity(rset));
            }
        } catch (SQLException e) {
            System.err.println("Failed to get capital city report for US17-T1: " + e.getMessage());
        }

        return capitalCities;
    }

    /**
     * US18-T1: Retrieves all capital cities within a specified continent,
     * sorted by population (descending).
     *
     * @param continent The continent to filter by.
     * @return List of CapitalCity objects sorted by population descending, or empty list on error.
     */
    public List<CapitalCity> getCapitalCitiesByContinent(String continent) {
        List<CapitalCity> capitalCities = new ArrayList<>();

        if (con == null || continent == null || continent.isBlank()) {
            return capitalCities;
        }

        String strSelect = """
                SELECT ci.Name AS Capital, c.Name AS Country, ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                WHERE c.Continent = ?
                ORDER BY ci.Population DESC
                """;

        try (PreparedStatement pstmt = con.prepareStatement(strSelect)) {
            pstmt.setString(1, continent);

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    capitalCities.add(mapResultSetToCapitalCity(rset));
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to get capital cities by continent for US18-T1: " + e.getMessage());
        }

        return capitalCities;
    }

    /**
     * US18-T2: Generates and prints capital city reports for all continents.
     */
    public void printAllCapitalCitiesByContinent() {
        List<String> continents = getContinents();

        for (String continent : continents) {
            List<CapitalCity> capitalCities = getCapitalCitiesByContinent(continent);
            System.out.println();
            System.out.println("=============================================================================================");
            System.out.println("CONTINENT: " + continent);
            System.out.println("=============================================================================================");
            printCapitalCities(capitalCities);
        }
    }

    /**
     * US17-T2: Prints a formatted table report of capital cities.
     *
     * @param capitalCities List of capital cities to print.
     */
    public void printCapitalCities(List<CapitalCity> capitalCities) {
        if (capitalCities == null || capitalCities.isEmpty()) {
            System.out.println("No capital cities found.");
            return;
        }

        System.out.println(LINE_SEPARATOR);
        System.out.printf(REPORT_HEADER_FORMAT, "Capital City", "Country", "Population");
        System.out.println(LINE_SEPARATOR);

        for (CapitalCity capital : capitalCities) {
            System.out.printf(
                    REPORT_ROW_FORMAT,
                    capital.getName(),
                    capital.getCountry(),
                    capital.getPopulation()
            );
        }
        System.out.println(LINE_SEPARATOR);
    }

    /**
     * Helper method to map a ResultSet row to a CapitalCity object.
     */
    private CapitalCity mapResultSetToCapitalCity(ResultSet rset) throws SQLException {
        CapitalCity capital = new CapitalCity();
        capital.setName(rset.getString("Capital"));
        capital.setCountry(rset.getString("Country"));
        capital.setPopulation(rset.getInt("Population"));
        return capital;
    }

    // =========================================================================
    // US19: Capital Cities in a Region
    // Assigned to: Product Owner (US19-T1), Developer 4 (US19-T2)
    // =========================================================================

    /**
     * US19-T1: Retrieves capital cities in a specified region sorted by population.
     */
    public List<CapitalCity> getCapitalCitiesByRegion(String region) {
        List<CapitalCity> capitals = new ArrayList<>();
        // TODO: Implement SQL query with WHERE country.Region = ?
        return capitals;
    }

    /**
     * US19-T2: Formats and prints the Region Capital City Report.
     */
    public void printCapitalCitiesByRegionReport(List<CapitalCity> capitals, String region) {
        // TODO: Implement report output logic
    }
}