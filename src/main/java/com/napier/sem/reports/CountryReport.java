package com.napier.sem.reports;

import com.napier.sem.models.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval and reporting for country data.
 * Supports User Stories US01 to US06.
 */
public class CountryReport {

    // Store the database connection used by the report methods.
    private final Connection connection;

    /**
     * Constructs the report generator with an active database connection.
     *
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
     * @return list of countries sorted by population descending,
     *         or an empty list if a database error occurs
     */
    public List<Country> getAllCountriesSorted() {

        // Define the SQL query to retrieve all countries in population order.
        String sql = """
            SELECT Code, Name, Continent, Region, Population, Capital
            FROM country
            ORDER BY Population DESC, Name ASC
            """;

        // Create a list to store the retrieved country records.
        List<Country> countries = new ArrayList<>();

        // Execute the SQL query using the database connection.
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // Read each country record returned by the database.
            while (resultSet.next()) {

                // Retrieve the capital ID and check whether it is NULL.
                int capital = resultSet.getInt("Capital");
                String capitalCode =
                        resultSet.wasNull() ? null : String.valueOf(capital);

                // Create a Country object using the current database record.
                Country country = new Country(
                        resultSet.getString("Code"),
                        resultSet.getString("Name"),
                        resultSet.getString("Continent"),
                        resultSet.getString("Region"),
                        resultSet.getInt("Population"),
                        capitalCode
                );

                // Add the Country object to the result list.
                countries.add(country);
            }

            // Handle database errors that occur while retrieving all countries.
        } catch (SQLException e) {
            // Display an error message if the database query fails.
            System.out.println(
                    "Failed to retrieve all countries for US01-T1: "
                            + e.getMessage()
            );
        }

        // Return the list of countries retrieved from the database.
        return countries;
    }

    /**
     * US01-T2:
     * Generates the Country Report Output using the countries
     * retrieved and sorted by US01-T1.
     */
    public void generateCountryReport() {

        // Retrieve all countries sorted by population.
        List<Country> countries = getAllCountriesSorted();

        // Check whether any country records are available for the report.
        if (countries == null || countries.isEmpty()) {
            System.out.println("No countries available for US01 report.");
            return;
        }

        // Print the report title and heading.
        System.out.println("Country Report");
        System.out.println("==============");

        // Display the column headings for the country information.
        System.out.printf(
                "%-8s %-35s %-18s %-25s %15s %-25s%n",
                "Code",
                "Name",
                "Continent",
                "Region",
                "Population",
                "Capital"
        );

        // Print a separator between the headings and country records.
        System.out.println(
                "--------------------------------------------------------------------------------------------------------------"
        );

        // Loop through each country in the retrieved list.
        for (Country country : countries) {

            // Retrieve the capital city name using the capital ID.
            String capitalName =
                    getCapitalCityName(country.getCapital());

            // Print the country details in the formatted report table.
            System.out.printf(
                    "%-8s %-35s %-18s %-25s %15d %-25s%n",
                    country.getCode(),
                    country.getName(),
                    country.getContinent(),
                    country.getRegion(),
                    country.getPopulation(),
                    capitalName
            );
        }
    }

    /**
     * Retrieves the capital city name using the capital city ID
     * stored in the country table.
     *
     * @param capitalId capital city ID
     * @return capital city name, or "N/A" if no capital is assigned
     *         or the database query fails
     */
    private String getCapitalCityName(String capitalId) {

        // Return N/A when the country does not have an assigned capital.
        if (capitalId == null) {
            return "N/A";
        }

        // Define the SQL query to retrieve the capital city name.
        String sql = """
            SELECT Name
            FROM city
            WHERE ID = ?
            """;

        // Prepare the SQL statement using the database connection.
        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Bind the capital city ID to the query parameter.
            statement.setInt(
                    1,
                    Integer.parseInt(capitalId)
            );

            // Execute the query to retrieve the capital city record.
            try (ResultSet resultSet =
                         statement.executeQuery()) {

                // Return the capital city name if a matching record exists.
                if (resultSet.next()) {
                    return resultSet.getString("Name");
                }
            }

            // Handle database errors that occur while retrieving the capital city name.
        } catch (SQLException e) {
            // Display an error message if the capital city query fails.
            System.out.println(
                    "Failed to retrieve capital city for US01: "
                            + e.getMessage()
            );
        }

        // Return N/A when no matching capital city is found.
        return "N/A";
    }

    // =========================================================================
    // US02: Countries by Continent Report
    // =========================================================================

    /**
     * US02-T1: Retrieve all countries in a specific continent sorted by population (descending).
     *
     * @param continent Name of the continent to filter by (e.g., "Asia", "Europe")
     * @return List of Country objects sorted by population descending, or empty list on error.
     */
    public List<Country> getCountriesByContinent(String continent) {
        // Create a list to store countries belonging to the selected continent.
        List<Country> countries = new ArrayList<>();

        // Define the SQL query to retrieve countries and their capital city names.
        String strSelect =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital " +
                        "FROM country c " +
                        "LEFT JOIN city ci ON c.Capital = ci.ID " +
                        "WHERE c.Continent = ? " +
                        "ORDER BY c.Population DESC";

        // Prepare the SQL statement using the database connection.
        try (PreparedStatement pstmt = connection.prepareStatement(strSelect)) {
            // Bind the selected continent to the query parameter.
            pstmt.setString(1, continent);

            // Execute the query and retrieve the matching country records.
            try (ResultSet rset = pstmt.executeQuery()) {

                // Read each country record returned by the database.
                while (rset.next()) {

                    // Create a Country object using the current database record.
                    Country country = new Country(
                            rset.getString("Code"),
                            rset.getString("Name"),
                            rset.getString("Continent"),
                            rset.getString("Region"),
                            rset.getInt("Population"),
                            rset.getString("Capital"));

                    // Add the country to the result list.
                    countries.add(country);
                }
            }

            // Return all countries retrieved for the selected continent.
            return countries;

            // Handle database errors that occur while retrieving countries by continent.
        } catch (SQLException e) {
            // Display an error message if the database query fails.
            System.out.println(e.getMessage());
            System.out.println("Failed to get country report by continent for US02-T1");

            // Return the current list of countries.
            return countries;
        }
    }

    /**
     * US02-T2: Output formatted table of countries by continent.
     *
     * @param countries List of Country objects retrieved from getCountriesByContinent
     * @param continent Target continent name for report header
     */
    public void printCountriesByContinentReport(List<Country> countries, String continent) {

        // Check whether any countries are available for the selected continent.
        if (countries == null || countries.isEmpty()) {
            System.out.println("No countries found for continent: " + continent);
            return;
        }

        // Print the report title and selected continent.
        System.out.println("==========================================================================================================");
        System.out.println("                                CONTINENT COUNTRY REPORT: " + continent.toUpperCase());
        System.out.println("==========================================================================================================");

        // Display the column headings for the country information.
        System.out.printf("%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                "Code", "Name", "Continent", "Region", "Population", "Capital");

        // Print a separator between the headings and country records.
        System.out.println("----------------------------------------------------------------------------------------------------------");

        // Loop through each country in the result list.
        for (Country c : countries) {

            // Skip the current item if it is null.
            if (c == null) continue;

            // Print the country details in a formatted table row.
            System.out.printf("%-6s | %-35s | %-15s | %-25s | %,12d | %-20s%n",
                    c.getCode(),
                    c.getName(),
                    c.getContinent(),
                    c.getRegion(),
                    c.getPopulation(),
                    c.getCapital() != null ? c.getCapital() : "N/A"
            );
        }

        // Print the report closing border and total number of countries.
        System.out.println("==========================================================================================================");
        System.out.println("Total Countries Listed: " + countries.size());
        System.out.println("==========================================================================================================\n");
    }

    // =========================================================================
    // US03: Countries by Region Report
    // =========================================================================

    /**
     * US03-T1:
     * Retrieves all countries belonging to a selected region
     * and orders them by population from largest to smallest.
     *
     * @param region selected region
     * @return list of countries in the selected region
     */
    public ArrayList<Country> getCountriesByRegion(String region) {

        // Create a list to store countries belonging to the selected region.
        ArrayList<Country> countries = new ArrayList<>();

        // Define the SQL query to retrieve countries and their capital city names.
        String sql =
                "SELECT co.Code AS CountryCode, " +
                        "co.Name AS CountryName, " +
                        "co.Continent, " +
                        "co.Region, " +
                        "co.Population, " +
                        "cap.Name AS CapitalName " +
                        "FROM country co " +
                        "LEFT JOIN city cap ON co.Capital = cap.ID " +
                        "WHERE co.Region = ? " +
                        "ORDER BY co.Population DESC";

        // Prepare the SQL statement using the database connection.
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Bind the selected region to the query parameter.
            stmt.setString(1, region);

            // Execute the query and retrieve the matching country records.
            try (ResultSet rs = stmt.executeQuery()) {

                // Read each country record returned by the database.
                while (rs.next()) {

                    // Retrieve the capital city name from the current record.
                    String capital = rs.getString("CapitalName");

                    // Create a Country object using the retrieved information.
                    Country country = new Country(
                            rs.getString("CountryCode"),
                            rs.getString("CountryName"),
                            rs.getString("Continent"),
                            rs.getString("Region"),
                            rs.getInt("Population"),
                            capital != null ? capital : "N/A");

                    // Add the country to the result list.
                    countries.add(country);
                }
            }

            // Handle database errors that occur while retrieving countries by region.
        } catch (SQLException e) {
            // Display an error message if the region query fails.
            System.out.println(
                    "Failed to retrieve countries for region: " + region
            );
            e.printStackTrace();
        }

        // Return the countries retrieved for the selected region.
        return countries;
    }

    /**
     * US03-T2: Generate Region Country Report
     * Outputs a formatted table of all countries in a specific region.
     *
     * @param countries  List of Country objects retrieved from getCountriesByRegion
     * @param regionName Target region name for report header
     */
    public void printCountriesByRegion(
            ArrayList<Country> countries,
            String regionName) {

        // Check whether any countries are available for the selected region.
        if (countries == null || countries.isEmpty()) {
            System.out.println(
                    "No countries found for region: " + regionName
            );
            return;
        }

        // Print the report title and selected region.
        System.out.println(
                "=========================================================================================================="
        );
        System.out.println(
                "                                         REGION COUNTRY REPORT: "
                        + regionName.toUpperCase()
        );
        System.out.println(
                "=========================================================================================================="
        );

        // Display the column headings for the country information.
        System.out.printf(
                "%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                "Code",
                "Name",
                "Continent",
                "Region",
                "Population",
                "Capital"
        );

        // Print a separator between the headings and country records.
        System.out.println(
                "----------------------------------------------------------------------------------------------------------"
        );

        // Loop through each country in the result list.
        for (Country c : countries) {

            // Skip the current item if it is null.
            if (c == null) {
                continue;
            }

            // Print the country details in a formatted table row.
            System.out.printf(
                    "%-6s | %-35s | %-15s | %-25s | %,12d | %-20s%n",
                    c.getCode(),
                    c.getName(),
                    c.getContinent(),
                    c.getRegion(),
                    c.getPopulation(),
                    c.getCapital() != null ? c.getCapital() : "N/A"
            );
        }

        // Print the report closing border and total number of countries.
        System.out.println(
                "=========================================================================================================="
        );
        System.out.println(
                "Total Countries Listed: " + countries.size()
        );
        System.out.println(
                "=========================================================================================================="
        );
    }

    // =========================================================================
    // US04: Top N Populated Countries Worldwide
    // Assigned to: Scrum Master (US04-T1), Developer 1 (US04-T2)
    // =========================================================================

    /**
     * US04-T1:
     * Retrieves the top N populated countries in the world sorted by population (descending).
     *
     * @param n The number of top populated countries to retrieve
     * @return List of Country objects, or an empty list if n <= 0 or on database error
     */
    public List<Country> getTopNCountriesWorldwide(int n) {

        // Create a list to store the top N countries.
        List<Country> countries = new ArrayList<>();

        // Validate the requested number of countries.
        if (n <= 0) {
            System.out.println("Invalid parameter N for US04-T1: N must be greater than 0.");
            return countries;
        }

        // Define the SQL query to retrieve the top N populated countries worldwide.
        String sql = """
                SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital
                FROM country c
                LEFT JOIN city ci ON c.Capital = ci.ID
                ORDER BY c.Population DESC
                LIMIT ?
                """;

        // Prepare the SQL statement using the database connection.
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            // Bind the requested number of countries to the query parameter.
            statement.setInt(1, n);

            // Execute the query and retrieve the top N country records.
            try (ResultSet resultSet = statement.executeQuery()) {

                // Read each country record returned by the database.
                while (resultSet.next()) {

                    // Retrieve the capital city name from the current record.
                    String capitalName = resultSet.getString("Capital");

                    // Create a Country object using the retrieved information.
                    Country country = new Country(
                            resultSet.getString("Code"),
                            resultSet.getString("Name"),
                            resultSet.getString("Continent"),
                            resultSet.getString("Region"),
                            resultSet.getInt("Population"),
                            capitalName != null ? capitalName : "N/A");

                    // Add the country to the result list.
                    countries.add(country);
                }
            }
            // Handle database errors that occur while retrieving the top N countries worldwide.
        } catch (SQLException e) {
            // Display an error message if the top N country query fails.
            System.out.println("Failed to retrieve Top " + n + " countries worldwide for US04-T1: " + e.getMessage());
        }

        // Return the top N countries retrieved from the database.
        return countries;
    }

    /**
     * US04-T2: Formats and prints the Top N World Country Report.
     *
     * @param countries List of top N countries retrieved by US04-T1
     * @param n Number of countries requested for the report
     */
    public void printTopNCountriesWorldwideReport(
            List<Country> countries,
            int n) {

        // Check whether any countries are available for the report.
        if (countries == null || countries.isEmpty()) {
            System.out.println("No countries found for Top " + n + " report.");
            return;
        }

        // Print the report title and report borders.
        System.out.println(
                "=========================================================================================================="
        );
        System.out.println(
                "                                      TOP " + n + " WORLD COUNTRY REPORT"
        );
        System.out.println(
                "=========================================================================================================="
        );

        // Display the column headings for the country information.
        System.out.printf(
                "%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                "Code",
                "Name",
                "Continent",
                "Region",
                "Population",
                "Capital"
        );

        // Print a separator between the headings and country records.
        System.out.println(
                "----------------------------------------------------------------------------------------------------------"
        );

        // Loop through each country in the result list.
        for (Country country : countries) {

            // Skip the current item if it is null.
            if (country == null) {
                continue;
            }

            // Print the country details in a formatted table row.
            System.out.printf(
                    "%-6s | %-35s | %-15s | %-25s | %,12d | %-20s%n",
                    country.getCode(),
                    country.getName(),
                    country.getContinent(),
                    country.getRegion(),
                    country.getPopulation(),
                    country.getCapital() != null
                            ? country.getCapital()
                            : "N/A"
            );
        }

        // Print the report closing border and total number of countries.
        System.out.println(
                "=========================================================================================================="
        );
        System.out.println(
                "Total Countries Listed: " + countries.size()
        );
        System.out.println(
                "=========================================================================================================="
        );
    }

    // =========================================================================
    // US05: Top N Populated Countries in a Continent
    // Assigned to: Scrum Master (US05-T1), Developer 2 (US05-T2)
    // =========================================================================

    /**
     * US05-T1:
     * Retrieves the top N populated countries in a specified continent sorted by population (descending).
     *
     * @param continent Name of the continent to filter by (e.g., "Asia", "Europe")
     * @param n         The number of top populated countries to retrieve
     * @return List of Country objects sorted by population descending, or an empty list on validation/database error
     */
    public List<Country> getTopNCountriesByContinent(String continent, int n) {

        // Create a list to store the top N countries in the selected continent.
        List<Country> countries = new ArrayList<>();

        // Validate that the continent name is not null or empty.
        if (continent == null || continent.trim().isEmpty()) {
            System.out.println("Invalid parameter 'continent' for US05-T1: Continent cannot be empty.");
            return countries;
        }

        // Validate that the requested number of countries is greater than zero.
        if (n <= 0) {
            System.out.println("Invalid parameter N for US05-T1: N must be greater than 0.");
            return countries;
        }

        // Define the SQL query to retrieve the top N countries in the selected continent.
        String sql = """
                SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital
                FROM country c
                LEFT JOIN city ci ON c.Capital = ci.ID
                WHERE c.Continent = ?
                ORDER BY c.Population DESC
                LIMIT ?
                """;

        // Prepare the SQL statement using the database connection.
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            // Bind the selected continent and requested number to the query parameters.
            statement.setString(1, continent);
            statement.setInt(2, n);

            // Execute the query and retrieve the matching country records.
            try (ResultSet resultSet = statement.executeQuery()) {

                // Read each country record returned by the database.
                while (resultSet.next()) {

                    // Retrieve the capital city name from the current record.
                    String capitalName = resultSet.getString("Capital");

                    // Create a Country object using the retrieved information.
                    Country country = new Country(
                            resultSet.getString("Code"),
                            resultSet.getString("Name"),
                            resultSet.getString("Continent"),
                            resultSet.getString("Region"),
                            resultSet.getInt("Population"),
                            capitalName != null ? capitalName : "N/A");

                    // Add the country to the result list.
                    countries.add(country);
                }
            }
            // Handle database errors that occur while retrieving the top N countries by continent.
        } catch (SQLException e) {
            // Display an error message if the continent query fails.
            System.out.println("Failed to retrieve Top " + n + " countries for continent '"
                    + continent + "' in US05-T1: " + e.getMessage());
        }

        // Return the top N countries retrieved for the selected continent.
        return countries;
    }

    /**
     * US05-T2: Formats and prints the Top N Continent Country Report.
     * @param countries List of top N countries retrieved for the continent
     * @param continent Target continent name for the report header
     * @param n Number of top populated countries requested
     */
    public void printTopNCountriesByContinentReport(List<Country> countries, String continent, int n) {

        // Check whether any countries are available for the selected continent.
        if (countries == null || countries.isEmpty()) {
            System.out.println(
                    "No countries found for continent: " + continent
            );
            return;
        }

        // Print the report title and selected continent.
        System.out.println("==========================================================================================================");
        System.out.println("TOP " + n + " POPULATED COUNTRIES IN " + continent.toUpperCase());
        System.out.println("==========================================================================================================");

        // Display the column headings for the country information.
        System.out.printf("%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                "Code", "Name", "Continent", "Region", "Population", "Capital");

        // Print a separator between the headings and country records.
        System.out.println("----------------------------------------------------------------------------------------------------------");

        // Loop through each country in the result list.
        for (Country c: countries) {

            // Skip the current item if it is null.
            if (c == null) {
                continue;
            }

            // Print the country details in a formatted table row.
            System.out.printf("%-6s | %-35s | %-15s | %-25s | %,12d | %-20s%n",
                    c.getCode(),
                    c.getName(),
                    c.getContinent(),
                    c.getRegion(),
                    c.getPopulation(),
                    c.getCapital() != null ? c.getCapital() : "N/A");
        }

        // Print the report closing border and total number of countries.
        System.out.println("==========================================================================================================");
        System.out.println("Total Countries Listed: " + countries.size());
        System.out.println("==========================================================================================================");
    }

    // =========================================================================
    // US06: Top N Populated Countries in a Region
    // Assigned to: Product Owner (US06-T1), Developer 3 (US06-T2)
    // =========================================================================

    /**
     * US06-T1:
     * Retrieves the top N populated countries within a selected region
     * sorted by population in descending order.
     *
     * @param region The region to search for
     * @param n The number of top populated countries to retrieve
     * @return List of Country objects, or an empty list if n <= 0 or on database error
     */
    public ArrayList<Country> getTopNCountriesByRegion(String region, int n) {

        // Create a list to store the top N countries in the selected region.
        ArrayList<Country> countries = new ArrayList<>();

        // Validate the requested number of countries.
        if (n <= 0) {
            return countries;
        }

        // Define the SQL query to retrieve the top N countries in the selected region.
        String sql =
                "SELECT co.Code AS CountryCode, " +
                        "co.Name AS CountryName, " +
                        "co.Continent, " +
                        "co.Region, " +
                        "co.Population, " +
                        "cap.Name AS CapitalName " +
                        "FROM country co " +
                        "LEFT JOIN city cap ON co.Capital = cap.ID " +
                        "WHERE co.Region = ? " +
                        "ORDER BY co.Population DESC " +
                        "LIMIT ?";

        // Prepare the SQL statement using the database connection.
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Bind the selected region and requested number to the query parameters.
            stmt.setString(1, region);
            stmt.setInt(2, n);

            // Execute the query and retrieve the matching country records.
            try (ResultSet rs = stmt.executeQuery()) {

                // Read each country record returned by the database.
                while (rs.next()) {

                    // Retrieve the capital city name from the current record.
                    String capital = rs.getString("CapitalName");

                    // Create a Country object using the retrieved information.
                    Country country = new Country(
                            rs.getString("CountryCode"),
                            rs.getString("CountryName"),
                            rs.getString("Continent"),
                            rs.getString("Region"),
                            rs.getInt("Population"),
                            capital != null ? capital : "N/A");

                    // Add the country to the result list.
                    countries.add(country);
                }
            }
            // Handle database errors that occur while retrieving the top N countries by region.
        } catch (SQLException e) {
            // Display an error message if the region query fails.
            System.out.println(
                    "Failed to retrieve top " + n +
                            " countries for region: " + region
            );
            e.printStackTrace();
        }

        // Return the top N countries retrieved for the selected region.
        return countries;
    }

    /**
     * US06-T2: Formats and prints the Top N Region Country Report.
     * This method displays the countries returned by US06-T1 in a formatted table.
     * The countries are expected to be * ordered from highest to lowest population.
     *
     * @param countries List of top N Country objects retrieved by US06-T1
     * @param region Target region name for the report header
     * @param n Number of countries requested
     */
    public void printTopNCountriesByRegionReport(
            List<Country> countries,
            String region,
            int n) {

        // Check whether the list is null or contains no countries.
        // If no data is available, display a message and stop the method.
        if (countries == null || countries.isEmpty()) {
            System.out.println(
                    "No countries found for region: " + region
            );
            return;
        }

        // Print the top border of the report.
        System.out.println(
                "=========================================================================================================="
        );

        // Print the report title using the requested number of countries and the selected region.
        System.out.println(
                "                              TOP " + n +
                        " REGION COUNTRY REPORT: " +
                        region.toUpperCase()
        );

        // Print a line below the report title.
        System.out.println(
                "=========================================================================================================="
        );

        // Print the column headings for the country information.
        // The %- values are used to keep the table columns aligned.
        System.out.printf(
                "%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                "Code",
                "Name",
                "Continent",
                "Region",
                "Population",
                "Capital"
        );

        // Print a separator between the headings and the country data.
        System.out.println(
                "----------------------------------------------------------------------------------------------------------"
        );

        // Loop through each Country object in the result list.
        for (Country country : countries) {

            // Skip the current item if it is null.
            // This prevents errors when accessing Country attributes.
            if (country == null) {
                continue;
            }

            // Print the country details in a formatted row.
            System.out.printf(
                    "%-6s | %-35s | %-15s | %-25s | %,12d | %-20s%n",
                    country.getCode(),
                    country.getName(),
                    country.getContinent(),
                    country.getRegion(),
                    country.getPopulation(),
                    country.getCapital() != null
                            ? country.getCapital()
                            : "N/A"
            );
        }

        // Print the bottom border of the report table.
        System.out.println(
                "=========================================================================================================="
        );

        // Display the number of countries included in the report.
        System.out.println(
                "Total Countries Listed: " + countries.size()
        );

        // Print the final separator to clearly finish the report.
        System.out.println(
                "=========================================================================================================="
        );
    }
}