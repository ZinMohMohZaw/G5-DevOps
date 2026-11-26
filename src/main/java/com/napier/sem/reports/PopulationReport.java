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

    // Store the database connection used by the report methods.
    private final Connection connection;

    /**
     * Constructs the report generator with an active database connection.
     *
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
     * or null if the database connection or query fails
     */
    public Population getWorldPopulation() {

        // Check whether the database connection is available.
        if (connection == null) {
            System.out.println("Database connection is null.");
            return null;
        }

        // Create a database statement to execute the world population query.
        try (Statement stmt = connection.createStatement()) {

            // Define the SQL query to calculate the total world population.
            String strSelect = "SELECT SUM(CAST(Population AS UNSIGNED)) AS TotalWorldPopulation FROM country;";

            // Execute the query and retrieve the calculated population.
            try (ResultSet rset = stmt.executeQuery(strSelect)) {

                // Check whether the query returned a result.
                if (rset.next()) {

                    // Create and return a Population object containing the world total.
                    return new Population("World", rset.getLong("TotalWorldPopulation"));
                }
            }
            // Handle unexpected errors that occur while calculating the world population for US26.
        } catch (Exception e) {

            // Display an error message if the population query fails.
            System.out.println(e.getMessage());
            System.out.println("Failed to calculate world population.");
        }

        // Return null if the population could not be retrieved.
        return null;
    }

    /**
     * US26-T2: Validate World Population Data
     * Validates that the returned Population object is non-null and contains a valid population sum.
     *
     * @param pop Population object to validate
     * @return true if valid, false otherwise
     */
    public boolean validateWorldPopulation(Population pop) {

        // Check whether the Population object exists.
        if (pop == null) {
            System.out.println("Validation Failed: Population data is null.");
            return false;
        }

        // Check whether the population name is missing or empty.
        if (pop.getName() == null || pop.getName().trim().isEmpty()) {
            System.out.println("Validation Failed: Population name is missing or empty.");
            return false;
        }

        // Check whether the total population is greater than zero.
        if (pop.getTotalPopulation() <= 0) {
            System.out.println("Validation Failed: Total population must be greater than 0.");
            return false;
        }

        // Return true when all validation checks pass.
        return true;
    }

    /**
     * US26-T2: Generate World Population Report Output
     * Formats and prints the total world population report.
     *
     * @param pop Population object retrieved from getWorldPopulation
     */
    public void printWorldPopulationReport(Population pop) {

        // Validate the population data before generating the report.
        if (!validateWorldPopulation(pop)) {
            System.out.println("Unable to display World Population Report due to invalid data.");
            return;
        }

        // Print the report title and borders.
        System.out.println("=================================================");
        System.out.println("             WORLD POPULATION REPORT             ");
        System.out.println("=================================================");

        // Display the column headings for the world population report.
        System.out.printf("%-20s | %-20s%n", "Entity", "Total Population");

        // Print a separator between the headings and population data.
        System.out.println("-------------------------------------------------");

        // Display the total world population in a formatted row.
        System.out.printf("%-20s | %,20d%n", pop.getName(), pop.getTotalPopulation());

        // Print the bottom border to finish the report.
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

        // Initialise the Population object to store the query result.
        Population population = null;

        // Validate the database connection and selected continent.
        if (connection == null || continent == null || continent.isBlank()) {
            return new Population(continent, 0);
        }

        // Define the SQL query to calculate the total population of the selected continent.
        String sql = """
                SELECT Continent, SUM(CAST(Population AS UNSIGNED)) AS TotalPopulation
                FROM country
                WHERE Continent = ?
                GROUP BY Continent
                """;

        // Prepare the SQL statement using the database connection.
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Bind the selected continent to the query parameter.
            stmt.setString(1, continent);

            // Execute the query and retrieve the continent population.
            try (ResultSet rs = stmt.executeQuery()) {

                // Check whether a matching continent record exists.
                if (rs.next()) {

                    // Create a Population object using the retrieved continent data.
                    population = new Population(rs.getString("Continent"), rs.getLong("TotalPopulation"));
                }
            }
            // Handle database errors that occur while retrieving population data for the selected continent (US27).
        } catch (SQLException e) {

            // Display an error message if the continent population query fails.
            System.err.println("Error retrieving population for continent " + continent + " (US27): " + e.getMessage());
        }

        // Return the retrieved population or a zero-population object if no result exists.
        return population != null ? population : new Population(continent, 0);
    }

    /**
     * Prints the population report for a continent.
     *
     * @param pop Population object
     */
    public void printPopulationByContinentReport(Population pop) {

        // Check whether continent population data is available.
        if (pop == null) {
            System.out.println("No population data available for continent.");
            return;
        }

        // Print the report title and borders.
        System.out.println();
        System.out.println("=================================================");
        System.out.println("          CONTINENT POPULATION REPORT            ");
        System.out.println("=================================================");

        // Display the column headings for the continent population report.
        System.out.printf("%-20s | %-20s%n", "Continent", "Total Population");

        // Print a separator between the headings and population data.
        System.out.println("-------------------------------------------------");

        // Display the selected continent and its total population.
        System.out.printf("%-20s | %,20d%n", pop.getName(), pop.getTotalPopulation());

        // Print the bottom border to finish the report.
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

        // Initialize the Population object to store the query result.
        Population population = null;

        // Validate the database connection and selected region.
        if (connection == null || region == null || region.isBlank()) {
            return new Population(region, 0);
        }

        // Define the SQL query to calculate the total population of the selected region.
        String sql = """
                SELECT Region, SUM(CAST(Population AS UNSIGNED)) AS TotalPopulation
                FROM country
                WHERE Region = ?
                GROUP BY Region
                """;

        // Prepare the SQL statement using the database connection.
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Bind the selected region to the query parameter.
            stmt.setString(1, region);

            // Execute the query and retrieve the region population.
            try (ResultSet rs = stmt.executeQuery()) {

                // Check whether a matching region record exists.
                if (rs.next()) {

                    // Create a Population object using the retrieved region data.
                    population = new Population(rs.getString("Region"), rs.getLong("TotalPopulation"));
                }
            }
            // Handle database errors that occur while retrieving population data for the selected region (US28).
        } catch (SQLException e) {

            // Display an error message if the region population query fails.
            System.err.println("Error retrieving population for region " + region + " (US28): " + e.getMessage());
        }

        // Return the retrieved population or a zero-population object if no result exists.
        return population != null ? population : new Population(region, 0);
    }

    /**
     * Prints the population report for a region.
     *
     * @param pop Population object
     */
    public void printPopulationByRegionReport(Population pop) {

        // Check whether region population data is available.
        if (pop == null) {
            System.out.println("No population data available for region.");
            return;
        }

        // Print the report title and borders.
        System.out.println();
        System.out.println("=================================================");
        System.out.println("            REGION POPULATION REPORT             ");
        System.out.println("=================================================");

        // Display the column headings for the region population report.
        System.out.printf("%-20s | %-20s%n", "Region", "Total Population");

        // Print a separator between the headings and population data.
        System.out.println("-------------------------------------------------");

        // Display the selected region and its total population.
        System.out.printf("%-20s | %,20d%n", pop.getName(), pop.getTotalPopulation());

        // Print the bottom border to finish the report.
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

        // Initialise the Population object to store the query result.
        Population population = null;

        // Validate the database connection and selected country name.
        if (connection == null || countryName == null || countryName.isBlank()) {
            return new Population(countryName, 0);
        }

        // Define the SQL query to retrieve the selected country's population.
        String sql = """
                SELECT Name, Population
                FROM country
                WHERE Name = ?
                """;

        // Prepare the SQL statement using the database connection.
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Bind the selected country name to the query parameter.
            stmt.setString(1, countryName);

            // Execute the query and retrieve the country population.
            try (ResultSet rs = stmt.executeQuery()) {

                // Check whether a matching country record exists.
                if (rs.next()) {

                    // Create a Population object using the retrieved country data.
                    population = new Population(rs.getString("Name"), rs.getLong("Population"));
                }
            }
            // Handle database errors that occur while retrieving population data for the selected country (US29).
        } catch (SQLException e) {

            // Display an error message if the country population query fails.
            System.err.println("Error retrieving population for country " + countryName + " (US29): " + e.getMessage());
        }

        // Return the retrieved population or a zero-population object if no result exists.
        return population != null ? population : new Population(countryName, 0);
    }

    /**
     * Prints the population report for a country.
     *
     * @param pop Population object
     */
    public void printPopulationByCountryReport(Population pop) {

        // Check whether country population data is available.
        if (pop == null) {
            System.out.println("No population data available for country.");
            return;
        }

        // Print the report title and borders.
        System.out.println();
        System.out.println("=================================================");
        System.out.println("            COUNTRY POPULATION REPORT            ");
        System.out.println("=================================================");

        // Display the column headings for the country population report.
        System.out.printf("%-20s | %-20s%n", "Country", "Total Population");

        // Print a separator between the headings and population data.
        System.out.println("-------------------------------------------------");

        // Display the selected country and its total population.
        System.out.printf("%-20s | %,20d%n", pop.getName(), pop.getTotalPopulation());

        // Print the bottom border to finish the report.
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
     * statistics for each continent, or an empty list on database error
     */
    public List<Population> getCityPopulationByContinent() {

        // Create a list to store population statistics for each continent.
        List<Population> populations = new ArrayList<>();

        // Check whether the database connection is available.
        if (connection == null) {
            System.out.println("Database connection is null.");
            return populations;
        }

        // Define the SQL query to retrieve total and urban population for each continent.
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

        // Execute the SQL query and retrieve continent population records.
        try (Statement stmt = connection.createStatement();
             ResultSet rset = stmt.executeQuery(sql)) {

            // Read each continent record returned by the database.
            while (rset.next()) {

                // Retrieve the continent name and its population figures.
                String continent = rset.getString("Continent");
                long totalPopulation = rset.getLong("TotalPopulation");
                long urbanPopulation = rset.getLong("UrbanPopulation");

                // Calculate the population living outside cities.
                long nonUrbanPopulation =
                        totalPopulation - urbanPopulation;

                // Initialize the urban and non-urban population percentages.
                double urbanPercentage = 0.0;
                double nonUrbanPercentage = 0.0;

                // Calculate both percentages when the total population is greater than zero.
                if (totalPopulation > 0) {
                    urbanPercentage =
                            ((double) urbanPopulation / totalPopulation) * 100;

                    nonUrbanPercentage =
                            ((double) nonUrbanPopulation / totalPopulation) * 100;
                }

                // Create a Population object containing the continent statistics.
                Population population = new Population(
                        continent,
                        totalPopulation,
                        urbanPopulation,
                        urbanPercentage,
                        nonUrbanPopulation,
                        nonUrbanPercentage
                );

                // Add the continent population statistics to the result list.
                populations.add(population);
            }

            // Handle unexpected errors that occur while retrieving continent population statistics for US23.
        } catch (Exception e) {

            // Display an error message if the continent population query fails.
            System.err.println(
                    "Failed to retrieve continent population report for US23: "
                            + e.getMessage()
            );
        }

        // Return the population statistics for all continents.
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

        // Check whether continent population data is available.
        if (populations == null || populations.isEmpty()) {
            System.out.println(
                    "No continent population data available."
            );
            return;
        }

        // Print the report title and borders.
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

        // Display the column headings for the continent population statistics.
        System.out.printf(
                "%-20s | %18s | %18s | %10s | %20s | %10s%n",
                "Continent",
                "Total Population",
                "In Cities",
                "City %",
                "Outside Cities",
                "Outside %"
        );

        // Print a separator between the headings and population records.
        System.out.println(
                "---------------------------------------------------------------------------------------------------------------"
        );

        // Loop through each continent population record.
        for (Population population : populations) {

            // Skip the current item if it is null.
            if (population == null) {
                continue;
            }

            // Print the continent population figures and percentages in a formatted row.
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

        // Print the bottom border to finish the report.
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
     * statistics for each region, or an empty list on database error
     */
    public List<Population> getCityPopulationByRegion() {

        // Create a list to store population statistics for each region.
        List<Population> populations = new ArrayList<>();

        // Check whether the database connection is available.
        if (connection == null) {
            System.out.println("Database connection is null.");
            return populations;
        }

        // Define the SQL query to retrieve total and urban population for each region.
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

        // Prepare and execute the SQL query using the database connection.
        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            // Read each region record returned by the database.
            while (resultSet.next()) {

                // Retrieve the region name and its population figures.
                String region =
                        resultSet.getString("Region");

                long totalPopulation =
                        resultSet.getLong("TotalPopulation");

                long urbanPopulation =
                        resultSet.getLong("UrbanPopulation");

                // Calculate the population living outside cities.
                long nonUrbanPopulation =
                        totalPopulation - urbanPopulation;

                // Initialize the urban and non-urban population percentages.
                double urbanPercentage = 0.0;
                double nonUrbanPercentage = 0.0;

                // Calculate both percentages when the total population is greater than zero.
                if (totalPopulation > 0) {

                    urbanPercentage =
                            ((double) urbanPopulation
                                    / totalPopulation) * 100;

                    nonUrbanPercentage =
                            ((double) nonUrbanPopulation
                                    / totalPopulation) * 100;
                }

                // Create a Population object containing the region statistics.
                Population population = new Population(
                        region,
                        totalPopulation,
                        urbanPopulation,
                        urbanPercentage,
                        nonUrbanPopulation,
                        nonUrbanPercentage
                );

                // Add the region population statistics to the result list.
                populations.add(population);
            }

            // Handle database errors that occur while retrieving regional city and non-city population statistics for US24.
        } catch (SQLException e) {

            // Display an error message if the region population query fails.
            System.out.println(
                    "Failed to retrieve regional city population report for US24: "
                            + e.getMessage()
            );
        }

        // Return the population statistics for all regions.
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

        // Check whether regional population data is available.
        if (populations == null || populations.isEmpty()) {
            System.out.println(
                    "No regional population data available."
            );
            return;
        }

        // Print the report title and borders.
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

        // Display the column headings for the regional population statistics.
        System.out.printf(
                "%-30s | %18s | %18s | %10s | %20s | %10s%n",
                "Region",
                "Total Population",
                "In Cities",
                "City %",
                "Outside Cities",
                "Outside %"
        );

        // Print a separator between the headings and population records.
        System.out.println(
                "-------------------------------------------------------------------------------------------------------------------------"
        );

        // Loop through each region population record.
        for (Population population : populations) {

            // Skip the current item if it is null.
            if (population == null) {
                continue;
            }

            // Print the region population figures and percentages in a formatted row.
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

        // Print the bottom border to finish the report.
        System.out.println(
                "==============================================================================================================="
        );
    }

    // =========================================================================
    // US25: City and Non-City Population by Country
    // =========================================================================

    /**
     * US25:
     * Retrieves population statistics for each country, including the total
     * population, population living in cities, population not living in cities,
     * and their corresponding percentages.
     *
     * @return List of Population objects containing city and non-city population
     * statistics for each country, or an empty list on database error
     */
    public List<Population> getCityPopulationByCountry() {

        // Create a list to store population statistics for each country.
        List<Population> populations = new ArrayList<>();

        // Check whether the database connection is available.
        if (connection == null) {
            System.out.println("Database connection is null.");
            return populations;
        }

        // Define the SQL query to retrieve total and urban population for each country.
        String sql = """
                SELECT c.Name AS Country,
                       CAST(c.Population AS UNSIGNED) AS TotalPopulation,
                       COALESCE(up.UrbanPopulation, 0) AS UrbanPopulation
                FROM country c
                LEFT JOIN
                    (
                        SELECT CountryCode,
                               SUM(CAST(Population AS UNSIGNED)) AS UrbanPopulation
                        FROM city
                        GROUP BY CountryCode
                    ) up
                    ON c.Code = up.CountryCode
                ORDER BY c.Name
                """;

        // Prepare and execute the SQL query using the database connection.
        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            // Read each country record returned by the database.
            while (resultSet.next()) {

                // Retrieve the country name and its population figures.
                String country =
                        resultSet.getString("Country");

                long totalPopulation =
                        resultSet.getLong("TotalPopulation");

                long urbanPopulation =
                        resultSet.getLong("UrbanPopulation");

                // Calculate the population living outside cities.
                long nonUrbanPopulation =
                        totalPopulation - urbanPopulation;

                // Initialize the urban and non-urban population percentages.
                double urbanPercentage = 0.0;
                double nonUrbanPercentage = 0.0;

                // Calculate both percentages when the total population is greater than zero.
                if (totalPopulation > 0) {

                    urbanPercentage =
                            ((double) urbanPopulation
                                    / totalPopulation) * 100;

                    nonUrbanPercentage =
                            ((double) nonUrbanPopulation
                                    / totalPopulation) * 100;
                }

                // Create a Population object containing the country statistics.
                Population population = new Population(
                        country,
                        totalPopulation,
                        urbanPopulation,
                        urbanPercentage,
                        nonUrbanPopulation,
                        nonUrbanPercentage
                );

                // Add the country population statistics to the result list.
                populations.add(population);
            }

            // Handle database errors that occur while retrieving country city and non-city population statistics for US25.
        } catch (SQLException e) {

            // Display an error message if the country population query fails.
            System.out.println(
                    "Failed to retrieve country city population report for US25: "
                            + e.getMessage()
            );
        }

        // Return the population statistics for all countries.
        return populations;
    }

    /**
     * US25:
     * Prints the city and non-city population report for each country,
     * including population totals and percentages.
     *
     * @param populations List of country population statistics
     */
    public void printCityPopulationByCountryReport(
            List<Population> populations) {

        // Check whether country population data is available.
        if (populations == null || populations.isEmpty()) {
            System.out.println(
                    "No country population data available."
            );
            return;
        }

        // Print the report title and borders.
        System.out.println();
        System.out.println(
                "========================================================================================================================="
        );
        System.out.println(
                "                                      COUNTRY POPULATION REPORT"
        );
        System.out.println(
                "========================================================================================================================="
        );

        // Display the column headings for the country population statistics.
        System.out.printf(
                "%-35s | %18s | %18s | %10s | %20s | %10s%n",
                "Country",
                "Total Population",
                "In Cities",
                "City %",
                "Outside Cities",
                "Outside %"
        );

        // Print a separator between the headings and population records.
        System.out.println(
                "-------------------------------------------------------------------------------------------------------------------------"
        );

        // Loop through each country population record.
        for (Population population : populations) {

            // Skip the current item if it is null.
            if (population == null) {
                continue;
            }

            // Print the country population figures and percentages in a formatted row.
            System.out.printf(
                    "%-35s | %,18d | %,18d | %9.2f%% | %,20d | %9.2f%%%n",
                    population.getName(),
                    population.getTotalPopulation(),
                    population.getUrbanPopulation(),
                    population.getUrbanPercentage(),
                    population.getNonUrbanPopulation(),
                    population.getNonUrbanPercentage()
            );
        }

        // Print the bottom border to finish the report.
        System.out.println(
                "========================================================================================================================="
        );
    }

    // =========================================================================
    // US30: Population by District
    // =========================================================================

    /**
     * US30:
     * Retrieves the total population of a specified district by summing
     * the populations of all cities belonging to that district.
     *
     * @param district district whose population is required
     * @return Population object containing the district name and total population,
     * or null if the district is invalid, not found, or a database error occurs
     */
    public Population getPopulationByDistrict(String district) {

        // Check whether the database connection is available.
        if (connection == null) {
            System.out.println("Database connection is null.");
            return null;
        }

        // Validate that the selected district name is not null or empty.
        if (district == null || district.trim().isEmpty()) {
            System.out.println("Invalid district for US30.");
            return null;
        }

        // Define the SQL query to calculate the total population of the selected district.
        String sql = """
                SELECT District,
                       SUM(CAST(Population AS UNSIGNED)) AS TotalPopulation
                FROM city
                WHERE District = ?
                GROUP BY District
                """;

        // Prepare the SQL statement using the database connection.
        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Bind the selected district to the query parameter.
            statement.setString(1, district);

            // Execute the query and retrieve the district population.
            try (ResultSet resultSet =
                         statement.executeQuery()) {

                // Check whether a matching district record exists.
                if (resultSet.next()) {

                    // Create and return a Population object using the retrieved district data.
                    return new Population(
                            resultSet.getString("District"),
                            resultSet.getLong("TotalPopulation")
                    );
                }
            }

            // Handle database errors that occur while retrieving population data for the selected district (US30).
        } catch (SQLException e) {

            // Display an error message if the district population query fails.
            System.out.println(
                    "Failed to retrieve population for district '"
                            + district + "' in US30: "
                            + e.getMessage()
            );
        }

        // Return null when no matching district is found.
        return null;
    }

    /**
     * US30:
     * Prints the total population of the specified district.
     *
     * @param population Population object containing district population data
     */
    public void printPopulationByDistrictReport(Population population) {

        // Check whether district population data is available.
        if (population == null) {
            System.out.println(
                    "No district population data available."
            );
            return;
        }

        // Print the report title and borders.
        System.out.println();
        System.out.println(
                "=============================================================="
        );
        System.out.println(
                "                    DISTRICT POPULATION REPORT"
        );
        System.out.println(
                "=============================================================="
        );

        // Display the column headings for the district population report.
        System.out.printf(
                "%-30s | %20s%n",
                "District",
                "Population"
        );

        // Print a separator between the headings and population data.
        System.out.println(
                "--------------------------------------------------------------"
        );

        // Display the district name and its total population.
        System.out.printf(
                "%-30s | %,20d%n",
                population.getName(),
                population.getTotalPopulation()
        );

        // Print the bottom border to finish the report.
        System.out.println(
                "=============================================================="
        );
    }

    // =========================================================================
    // US31: Population by City
    // =========================================================================

    /**
     * US31:
     * Retrieves the population of a specified city within a specified country.
     *
     * @param city    city whose population is required
     * @param country country containing the city
     * @return Population object containing the city name and population,
     * or null if the city is invalid, not found, or a database error occurs
     */
    public Population getPopulationByCity(String city, String country) {

        // Check whether the database connection is available.
        if (connection == null) {
            System.out.println("Database connection is null.");
            return null;
        }

        // Validate that both the city and country names are provided.
        if (city == null || city.trim().isEmpty()
                || country == null || country.trim().isEmpty()) {

            System.out.println("Invalid city or country for US31.");
            return null;
        }

        // Define the SQL query to retrieve the selected city's population within the specified country.
        String sql = """
                SELECT ci.Name AS City,
                       ci.Population
                FROM city ci
                INNER JOIN country c
                    ON ci.CountryCode = c.Code
                WHERE ci.Name = ?
                  AND c.Name = ?
                """;

        // Prepare the SQL statement using the database connection.
        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            // Bind the selected city and country names to the query parameters.
            statement.setString(1, city);
            statement.setString(2, country);

            // Execute the query and retrieve the matching city record.
            try (ResultSet resultSet =
                         statement.executeQuery()) {

                // Check whether a matching city record exists.
                if (resultSet.next()) {

                    // Create and return a Population object using the retrieved city data.
                    return new Population(
                            resultSet.getString("City"),
                            resultSet.getLong("Population")
                    );
                }
            }

            // Handle database errors that occur while retrieving population data for the selected city and country (US31).
        } catch (SQLException e) {

            // Display an error message if the city population query fails.
            System.out.println(
                    "Failed to retrieve population for city '"
                            + city + "' in US31: "
                            + e.getMessage()
            );
        }

        // Return null when no matching city is found.
        return null;
    }

    /**
     * US31:
     * Prints the population of the specified city.
     *
     * @param population Population object containing city population data
     */
    public void printPopulationByCityReport(Population population) {

        // Check whether city population data is available.
        if (population == null) {
            System.out.println(
                    "No city population data available."
            );
            return;
        }

        // Print the report title and borders.
        System.out.println();
        System.out.println(
                "=============================================================="
        );
        System.out.println(
                "                      CITY POPULATION REPORT"
        );
        System.out.println(
                "=============================================================="
        );

        // Display the column headings for the city population report.
        System.out.printf(
                "%-30s | %20s%n",
                "City",
                "Population"
        );

        // Print a separator between the headings and population data.
        System.out.println(
                "--------------------------------------------------------------"
        );

        // Display the city name and its total population.
        System.out.printf(
                "%-30s | %,20d%n",
                population.getName(),
                population.getTotalPopulation()
        );

        // Print the bottom border to finish the report.
        System.out.println(
                "=============================================================="
        );
    }
}