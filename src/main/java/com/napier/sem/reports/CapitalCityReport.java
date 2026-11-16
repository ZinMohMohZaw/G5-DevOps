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
     * US17-T1: Retrieves all capital cities in the world sorted by population (descending).
     *
     * @return List of CapitalCity objects sorted by population descending, or empty list on error.
     */
    public List<CapitalCity> getAllCapitalCitiesByPopulation() {

        // Initialise an empty list to store the retrieved capital cities.
        List<CapitalCity> capitalCities = new ArrayList<>();

        // Check whether a database connection is available.
        if (con == null) {
            return capitalCities;
        }

        // Define the SQL query to retrieve all capital cities, ordered by population.
        String strSelect = """
                SELECT ci.Name AS Capital, c.Name AS Country, ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                ORDER BY ci.Population DESC
                """;

        // Prepare and execute the SQL query to retrieve capital city records.
        try (PreparedStatement pstmt = con.prepareStatement(strSelect);
             ResultSet rset = pstmt.executeQuery()) {

            // Iterate through each row returned by the database.
            while (rset.next()) {

                // Map the current database row to a CapitalCity object and add it to the list.
                capitalCities.add(mapResultSetToCapitalCity(rset));
            }
            // Handle database errors that occur while retrieving capital city records.
        } catch (SQLException e) {
            // Display an error message if the database query fails.
            System.err.println("Failed to get capital city report for US17-T1: " + e.getMessage());
        }

        // Return the retrieved capital city records.
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

        // Initialise an empty list to store the matching capital cities.
        List<CapitalCity> capitalCities = new ArrayList<>();

        // Validate the database connection and continent input.
        if (con == null || continent == null || continent.isBlank()) {
            return capitalCities;
        }

        // Define the SQL query to retrieve capital cities in the selected continent.
        String strSelect = """
                SELECT ci.Name AS Capital, c.Name AS Country, ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                WHERE c.Continent = ?
                ORDER BY ci.Population DESC
                """;

        // Prepare the SQL query to retrieve capital cities for the selected continent.
        try (PreparedStatement pstmt = con.prepareStatement(strSelect)) {

            // Bind the selected continent to the SQL query parameter.
            pstmt.setString(1, continent);

            // Execute the query and retrieve the matching capital city records.
            try (ResultSet rset = pstmt.executeQuery()) {

                // Iterate through each capital city returned by the database.
                while (rset.next()) {

                    // Map the current database row to a CapitalCity object and store it in the list.
                    capitalCities.add(mapResultSetToCapitalCity(rset));
                }
            }
            // Handle database errors that occur while retrieving capital cities by continent.
        } catch (SQLException e) {
            // Display an error message if the database query fails.
            System.err.println("Failed to get capital cities by continent for US18-T1: " + e.getMessage());
        }

        // Return the matching capital city records.
        return capitalCities;
    }

    /**
     * US18-T2: Generates and prints capital city reports for a specific continent.
     *
     * @param continent The continent to display
     */
    public void printCapitalCitiesByContinent(String continent) {
        // Retrieve capital cities filtered by the specified continent.
        List<CapitalCity> capitals = getCapitalCitiesByContinent(continent);

        // Display a message and stop processing when no matching records are available.
        if (capitals == null || capitals.isEmpty()) {
            System.out.println("No capital cities found for continent: " + continent);
            return;
        }

        // Display the selected continent as the report heading.
        System.out.println("Capital Cities in Continent: " + continent.toUpperCase());

        // Print the formatted capital city table.
        printCapitalCities(capitals);

        // Display the total number of capital cities included in the report.
        System.out.println("Total Capital Cities Listed: " + capitals.size());
    }

    /**
     * US17-T2: Prints a formatted table report of capital cities.
     *
     * @param capitalCities List of capital cities to print.
     */
    public void printCapitalCities(List<CapitalCity> capitalCities) {
        // Check whether there are any capital city records to display.
        if (capitalCities == null || capitalCities.isEmpty()) {
            System.out.println("No capital cities found.");
            return;
        }

        // Display the table separator and column headings.
        System.out.println(LINE_SEPARATOR);
        System.out.printf(REPORT_HEADER_FORMAT, "Capital City", "Country", "Population");
        System.out.println(LINE_SEPARATOR);

        // Iterate through the capital city list and display each record.
        for (CapitalCity capital : capitalCities) {
            System.out.printf(
                    REPORT_ROW_FORMAT,
                    capital.getName(),
                    capital.getCountry(),
                    capital.getPopulation()
            );
        }

        // Display the closing table separator.
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

        // Extract the capital city, country and population from the current database row.
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

        // Initialise an empty list to store the matching capital cities.
        List<CapitalCity> capitals = new ArrayList<>();

        // Validate the database connection and region input.
        if (con == null || region == null || region.isBlank()) {
            return capitals;
        }

        // Define the SQL query to retrieve capital cities in the selected region.
        String strSelect = """
                SELECT ci.Name AS Capital, c.Name AS Country, ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                WHERE c.Region = ?
                ORDER BY ci.Population DESC
                """;

        // Prepare the SQL query to retrieve capital cities within the selected region.
        try (PreparedStatement pstmt = con.prepareStatement(strSelect)) {

            // Bind the selected region to the SQL query parameter.
            pstmt.setString(1, region);

            // Execute the query and retrieve the matching database records.
            try (ResultSet rset = pstmt.executeQuery()) {

                // Process each capital city record returned by the database.
                while (rset.next()) {

                    // Convert the current result row into a CapitalCity object and add it to the list.
                    capitals.add(mapResultSetToCapitalCity(rset));
                }
            }

            // Handle database errors that occur while retrieving capital cities by region.
        } catch (SQLException e) {
            // Display an error message if the database query fails.
            System.err.println(
                    "Failed to get capital cities by region for US19-T1: "
                            + e.getMessage()
            );
        }

        // Return the matching capital city records.
        return capitals;
    }

    /**
     * US19-T2: Formats and prints the Region Capital City Report.
     *
     * @param capitals List of CapitalCity objects returned by US19-T1
     * @param region   Target region name for report header
     */
    public void printCapitalCitiesByRegionReport(List<CapitalCity> capitals, String region) {
        // Check whether there are any capital city records to display.
        if (capitals == null || capitals.isEmpty()) {
            System.out.println("No capital cities found for region: " + (region != null ? region : "N/A"));
            return;
        }

        // Display the region report heading.
        System.out.println();
        System.out.println("=============================================================================================");
        System.out.println("CAPITAL CITIES IN REGION: " + region.toUpperCase());
        System.out.println("=============================================================================================");

        // Print the formatted capital city records.
        printCapitalCities(capitals);

        // Display the total number of capital cities in the selected region.
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
        // Initialise an empty list to store the top capital cities.
        List<CapitalCity> capitalCities = new ArrayList<>();

        // Validate the database connection and requested number of records.
        if (con == null || n <= 0) {
            return capitalCities;
        }

        // Define the SQL query to retrieve the top N capital cities worldwide.
        String strSelect = """
                SELECT ci.Name AS Capital, c.Name AS Country, ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                ORDER BY ci.Population DESC
                LIMIT ?
                """;

        // Prepare the SQL query to retrieve the requested number of capital cities.
        try (PreparedStatement pstmt = con.prepareStatement(strSelect)) {

            // Set the maximum number of capital city records to retrieve.
            pstmt.setInt(1, n);

            // Execute the query and retrieve the top N capital cities.
            try (ResultSet rset = pstmt.executeQuery()) {

                // Iterate through the returned capital city records.
                while (rset.next()) {

                    // Convert each database row into a CapitalCity object and add it to the list.
                    capitalCities.add(mapResultSetToCapitalCity(rset));
                }
            }
            // Handle database errors that occur while retrieving the top N capital cities worldwide.
        } catch (SQLException e) {
            System.err.println("Failed to get top N capital cities worldwide for US20: " + e.getMessage());
        }

        // Return the retrieved top N capital cities.
        return capitalCities;
    }

    /**
     * Generates and prints the Top N World Capital City Report.
     *
     * @param capitalCities List of CapitalCity objects
     * @param n             Number of top capital cities requested
     */
    public void printTopNCapitalCitiesWorldwideReport(List<CapitalCity> capitalCities, int n) {
        // Check whether there are any capital city records to display.
        if (capitalCities == null || capitalCities.isEmpty()) {
            System.out.println("No capital cities found for Top " + n + " worldwide report.");
            return;
        }

        // Display the report heading with the requested number of capital cities.
        System.out.println();
        System.out.println("=============================================================================================");
        System.out.println("                         TOP " + n + " POPULATED CAPITAL CITIES WORLDWIDE");
        System.out.println("=============================================================================================");

        // Print the formatted capital city results.
        printCapitalCities(capitalCities);

        // Display the actual number of records included in the report.
        System.out.println("Total Capital Cities Listed: " + capitalCities.size());
        System.out.println("=============================================================================================\n");
    }

    // =========================================================================
    // US21: Top N Populated Capital Cities in a Continent
    // =========================================================================

    /**
     * Retrieves the top N populated capital cities in a specified continent.
     *
     * @param continent Target continent name
     * @param n         Number of top capital cities to retrieve
     * @return List of CapitalCity objects sorted by population descending
     */
    public List<CapitalCity> getTopNCapitalCitiesByContinent(String continent, int n) {
        // Initialise an empty list to store the top capital cities.
        List<CapitalCity> capitalCities = new ArrayList<>();

        // Validate the database connection, continent input and requested result limit.
        if (con == null || continent == null || continent.isBlank() || n <= 0) {
            return capitalCities;
        }

        // Define the SQL query to retrieve the top N capital cities in the selected continent.
        String strSelect = """
                SELECT ci.Name AS Capital, c.Name AS Country, ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                WHERE c.Continent = ?
                ORDER BY ci.Population DESC
                LIMIT ?
                """;

        // Prepare the SQL query to retrieve the top N capital cities in the selected continent.
        try (PreparedStatement pstmt = con.prepareStatement(strSelect)) {

            // Bind the selected continent to the first SQL parameter.
            pstmt.setString(1, continent);

            // Bind the requested maximum number of results to the second SQL parameter.
            pstmt.setInt(2, n);

            // Execute the query and retrieve the matching capital city records.
            try (ResultSet rset = pstmt.executeQuery()) {

                // Iterate through the returned records.
                while (rset.next()) {

                    // Map each database row to a CapitalCity object and add it to the list.
                    capitalCities.add(mapResultSetToCapitalCity(rset));
                }
            }
            // Handle database errors that occur while retrieving the top N capital cities by continent.
        } catch (SQLException e) {
            // Display an error message if the database query fails.
            System.err.println("Failed to get top N capital cities by continent for US21: " + e.getMessage());
        }

        // Return the retrieved top N capital cities.
        return capitalCities;
    }

    /**
     * Generates and prints the Top N Continent Capital City Report.
     *
     * @param capitalCities List of CapitalCity objects
     * @param continent     Target continent name
     * @param n             Number of top capital cities requested
     */
    public void printTopNCapitalCitiesByContinentReport(List<CapitalCity> capitalCities, String continent, int n) {
        // Check whether there are any capital city records to display.
        if (capitalCities == null || capitalCities.isEmpty()) {
            System.out.println("No capital cities found for continent: " + (continent != null ? continent : "N/A"));
            return;
        }

        // Display the report heading with the selected continent and requested limit.
        System.out.println();
        System.out.println("=============================================================================================");
        System.out.printf("             TOP %d POPULATED CAPITAL CITIES IN CONTINENT: %s%n", n, continent.toUpperCase());
        System.out.println("=============================================================================================");

        // Print the formatted capital city report.
        printCapitalCities(capitalCities);

        // Display the total number of capital cities returned.
        System.out.println("Total Capital Cities Listed: " + capitalCities.size());
        System.out.println("=============================================================================================\n");
    }

    // =========================================================================
    // US22: Top N Populated Capital Cities in a Region
    // =========================================================================

    /**
     * Retrieves the top N populated capital cities in a specified region.
     *
     * @param region Target region name
     * @param n      Number of top capital cities to retrieve
     * @return List of CapitalCity objects sorted by population descending
     */
    public List<CapitalCity> getTopNCapitalCitiesByRegion(String region, int n) {
        // Initialise an empty list to store the top capital cities.
        List<CapitalCity> capitalCities = new ArrayList<>();

        // Validate the database connection, region input and requested result limit.
        if (con == null || region == null || region.isBlank() || n <= 0) {
            return capitalCities;
        }

        // Define the SQL query to retrieve the top N capital cities in the selected region.
        String strSelect = """
                SELECT ci.Name AS Capital, c.Name AS Country, ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                WHERE c.Region = ?
                ORDER BY ci.Population DESC
                LIMIT ?
                """;

        // Prepare the SQL query to retrieve the top N capital cities in the selected region.
        try (PreparedStatement pstmt = con.prepareStatement(strSelect)) {

            // Set the selected region as the first SQL query parameter.
            pstmt.setString(1, region);

            // Set the maximum number of capital city records to retrieve.
            pstmt.setInt(2, n);

            // Execute the query and retrieve the matching capital city records.
            try (ResultSet rset = pstmt.executeQuery()) {

                // Iterate through each result returned by the database.
                while (rset.next()) {

                    // Convert the current result row into a CapitalCity object and add it to the list.
                    capitalCities.add(mapResultSetToCapitalCity(rset));
                }
            }
            // Handle database errors that occur while retrieving the top N capital cities by region.
        } catch (SQLException e) {
            // Display an error message if the database query fails.
            System.err.println("Failed to get top N capital cities by region for US22: " + e.getMessage());
        }

        // Return the retrieved top N capital cities.
        return capitalCities;
    }

    /**
     * Generates and prints the Top N Region Capital City Report.
     *
     * @param capitalCities List of CapitalCity objects
     * @param region        Target region name
     * @param n             Number of top capital cities requested
     */
    public void printTopNCapitalCitiesByRegionReport(List<CapitalCity> capitalCities, String region, int n) {
        // Check whether there are any capital city records to display.
        if (capitalCities == null || capitalCities.isEmpty()) {
            System.out.println("No capital cities found for region: " + (region != null ? region : "N/A"));
            return;
        }

        // Display the report heading with the selected region and requested limit.
        System.out.println();
        System.out.println("=============================================================================================");
        System.out.printf("              TOP %d POPULATED CAPITAL CITIES IN REGION: %s%n", n, region.toUpperCase());
        System.out.println("=============================================================================================");

        // Print the formatted capital city report.
        printCapitalCities(capitalCities);

        // Display the total number of capital cities included in the report.
        System.out.println("Total Capital Cities Listed: " + capitalCities.size());
        System.out.println("=============================================================================================\n");
    }
}