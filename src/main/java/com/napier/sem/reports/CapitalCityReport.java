package com.napier.sem.reports;

import com.napier.sem.models.CapitalCity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval and reporting for capital city data.
 * Supports User Stories US17, US18 and US19.
 */
public class CapitalCityReport {

    private static final String REPORT_HEADER_FORMAT = "%-40s %-40s %12s%n";
    private static final String REPORT_ROW_FORMAT = "%-40s %-40s %12d%n";
    private static final String LINE_SEPARATOR = "---------------------------------------------------------------------------------------------";

    private final Connection con;

    /**
     * Constructs the capital city report generator with an active database connection.
     *
     * @param con active MySQL database connection
     */
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
     * US18-T2: Generates and prints capital city reports for a specific continent.
     *
     * @param continent The continent to display
     */
    public void printCapitalCitiesByContinent(String continent) {
        List<CapitalCity> capitals = getCapitalCitiesByContinent(continent);
        if (capitals == null || capitals.isEmpty()) {
            System.out.println("No capital cities found for continent: " + continent);
            return;
        }
        System.out.println("Capital Cities in Continent: " + continent.toUpperCase());
        printCapitalCities(capitals);

        System.out.println("Total Capital Cities Listed: " + capitals.size());
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
     * Maps a ResultSet row to a CapitalCity object.
     *
     * @param rset ResultSet containing capital city data
     * @return CapitalCity object containing the retrieved data
     * @throws SQLException if the ResultSet data cannot be accessed
     */
    private CapitalCity mapResultSetToCapitalCity(ResultSet rset) throws SQLException {
        return new CapitalCity(
                rset.getString("Capital"),
                rset.getString("Country"),
                rset.getInt("Population"));
    }

    // =========================================================================
    // US19: Capital Cities in a Region
    // Assigned to: Product Owner (US19-T1), Developer 4 (US19-T2)
    // =========================================================================

    /**
     * US19-T1:
     * Retrieves capital cities within a selected region
     * sorted by population in descending order.
     *
     * @param region The region to search for
     * @return List of CapitalCity objects, or an empty list if the region is invalid or on database error
     */
    public List<CapitalCity> getCapitalCitiesByRegion(String region) {

        List<CapitalCity> capitals = new ArrayList<>();

        if (con == null || region == null || region.isBlank()) {
            return capitals;
        }

        String strSelect = """
            SELECT ci.Name AS Capital, c.Name AS Country, ci.Population
            FROM country c
            JOIN city ci ON c.Capital = ci.ID
            WHERE c.Region = ?
            ORDER BY ci.Population DESC
            """;

        try (PreparedStatement pstmt = con.prepareStatement(strSelect)) {

            pstmt.setString(1, region);

            try (ResultSet rset = pstmt.executeQuery()) {

                while (rset.next()) {
                    capitals.add(mapResultSetToCapitalCity(rset));
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Failed to get capital cities by region for US19-T1: "
                            + e.getMessage()
            );
        }

        return capitals;
    }

    /**
     * US19-T2: Formats and prints the Region Capital City Report.
     *
     * @param capitals List of CapitalCity objects returned by US19-T1
     * @param region   Target region name for report header
     */
    public void printCapitalCitiesByRegionReport(List<CapitalCity> capitals, String region) {
        if (capitals == null || capitals.isEmpty()) {
            System.out.println("No capital cities found for region: " + (region != null ? region : "N/A"));
            return;
        }

        System.out.println();
        System.out.println("=============================================================================================");
        System.out.println("CAPITAL CITIES IN REGION: " + region.toUpperCase());
        System.out.println("=============================================================================================");
        printCapitalCities(capitals);
        System.out.println("Total Capital Cities Listed: " + capitals.size());
        System.out.println("=============================================================================================\n");
    }

    // =========================================================================
    // US20: Top N Populated Capital Cities Worldwide
    // =========================================================================

    /**
     * Retrieves the top N populated capital cities worldwide sorted by population descending.
     *
     * @param n Number of top capital cities to retrieve
     * @return List of CapitalCity objects, or an empty list if n <= 0 or on error
     */
    public List<CapitalCity> getTopNCapitalCitiesWorldwide(int n) {
        List<CapitalCity> capitalCities = new ArrayList<>();
        if (con == null || n <= 0) {
            return capitalCities;
        }

        String strSelect = """
                SELECT ci.Name AS Capital, c.Name AS Country, ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                ORDER BY ci.Population DESC
                LIMIT ?
                """;

        try (PreparedStatement pstmt = con.prepareStatement(strSelect)) {
            pstmt.setInt(1, n);

            try (ResultSet rset = pstmt.executeQuery()) {
                while (rset.next()) {
                    capitalCities.add(mapResultSetToCapitalCity(rset));
                }
            }
        } catch (SQLException e) {
            System.err.println("Failed to get top N capital cities worldwide for US20: " + e.getMessage());
        }

        return capitalCities;
    }

    /**
     * Generates and prints the Top N World Capital City Report.
     *
     * @param capitalCities List of CapitalCity objects
     * @param n             Number of top capital cities requested
     */
    public void printTopNCapitalCitiesWorldwideReport(List<CapitalCity> capitalCities, int n) {
        if (capitalCities == null || capitalCities.isEmpty()) {
            System.out.println("No capital cities found for Top " + n + " worldwide report.");
            return;
        }

        System.out.println();
        System.out.println("=============================================================================================");
        System.out.println("                         TOP " + n + " POPULATED CAPITAL CITIES WORLDWIDE");
        System.out.println("=============================================================================================");
        printCapitalCities(capitalCities);
        System.out.println("Total Capital Cities Listed: " + capitalCities.size());
        System.out.println("=============================================================================================\n");
    }
}