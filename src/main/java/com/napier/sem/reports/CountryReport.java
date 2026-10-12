package com.napier.sem.reports;

import com.napier.sem.models.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval and reporting for Country data (US01, US02, US03).
 */
public class CountryReport {

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
     *
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
     *
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

        ArrayList<Country> countries = new ArrayList<>();

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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, region);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Country country = new Country();

                    country.setCode(rs.getString("CountryCode"));
                    country.setName(rs.getString("CountryName"));
                    country.setContinent(rs.getString("Continent"));
                    country.setRegion(rs.getString("Region"));
                    country.setPopulation(rs.getInt("Population"));

                    String capital = rs.getString("CapitalName");

                    if (capital != null) {
                        country.setCapital(capital);
                    } else {
                        country.setCapital("N/A");
                    }

                    countries.add(country);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Failed to retrieve countries for region: " + region
            );
            e.printStackTrace();
        }

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

        if (countries == null || countries.isEmpty()) {
            System.out.println(
                    "No countries found for region: " + regionName
            );
            return;
        }

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

        System.out.printf(
                "%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                "Code",
                "Name",
                "Continent",
                "Region",
                "Population",
                "Capital"
        );

        System.out.println(
                "----------------------------------------------------------------------------------------------------------"
        );

        for (Country c : countries) {

            if (c == null) {
                continue;
            }

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
        List<Country> countries = new ArrayList<>();
        if (n <= 0) {
            System.out.println("Invalid parameter N for US04-T1: N must be greater than 0.");
            return countries;
        }

        String sql = """
                SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital
                FROM country c
                LEFT JOIN city ci ON c.Capital = ci.ID
                ORDER BY c.Population DESC
                LIMIT ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, n);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Country country = new Country();

                    country.setCode(resultSet.getString("Code"));
                    country.setName(resultSet.getString("Name"));
                    country.setContinent(resultSet.getString("Continent"));
                    country.setRegion(resultSet.getString("Region"));
                    country.setPopulation(resultSet.getInt("Population"));

                    String capitalName = resultSet.getString("Capital");
                    country.setCapital(capitalName != null ? capitalName : "N/A");

                    countries.add(country);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve Top " + n + " countries worldwide for US04-T1: " + e.getMessage());
        }

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

        if (countries == null || countries.isEmpty()) {
            System.out.println("No countries found for Top " + n + " report.");
            return;
        }

        System.out.println(
                "=========================================================================================================="
        );
        System.out.println(
                "                                      TOP " + n + " WORLD COUNTRY REPORT"
        );
        System.out.println(
                "=========================================================================================================="
        );

        System.out.printf(
                "%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                "Code",
                "Name",
                "Continent",
                "Region",
                "Population",
                "Capital"
        );

        System.out.println(
                "----------------------------------------------------------------------------------------------------------"
        );

        for (Country country : countries) {

            if (country == null) {
                continue;
            }

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
        List<Country> countries = new ArrayList<>();

        if (continent == null || continent.trim().isEmpty()) {
            System.out.println("Invalid parameter 'continent' for US05-T1: Continent cannot be empty.");
            return countries;
        }

        if (n <= 0) {
            System.out.println("Invalid parameter N for US05-T1: N must be greater than 0.");
            return countries;
        }

        String sql = """
                SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, ci.Name AS Capital
                FROM country c
                LEFT JOIN city ci ON c.Capital = ci.ID
                WHERE c.Continent = ?
                ORDER BY c.Population DESC
                LIMIT ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, continent);
            statement.setInt(2, n);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Country country = new Country();

                    country.setCode(resultSet.getString("Code"));
                    country.setName(resultSet.getString("Name"));
                    country.setContinent(resultSet.getString("Continent"));
                    country.setRegion(resultSet.getString("Region"));
                    country.setPopulation(resultSet.getInt("Population"));

                    String capitalName = resultSet.getString("Capital");
                    country.setCapital(capitalName != null ? capitalName : "N/A");

                    countries.add(country);
                }
            }
        } catch (SQLException e) {
            System.out.println("Failed to retrieve Top " + n + " countries for continent '"
                    + continent + "' in US05-T1: " + e.getMessage());
        }

        return countries;
    }

    /**
     * US05-T2: Formats and prints the Top N Continent Country Report.
     * @param countries List of top N countries retrieved for the continent
     * @param continent Target continent name for the report header
     * @param n Number of top populated countries requested
     */
    public void printTopNCountriesByContinentReport(List<Country> countries, String continent, int n) {
        if (countries == null || countries.isEmpty()) {
            System.out.println(
                    "No countries found for continent: " + continent
            );
            return;
        }

        System.out.println("==========================================================================================================");
        System.out.println("TOP " + n + " POPULATED COUNTRIES IN " + continent.toUpperCase());
        System.out.println("==========================================================================================================");

        System.out.printf("%-6s | %-35s | %-15s | %-25s | %-12s | %-20s%n",
                        "Code", "Name", "Continent", "Region", "Population", "Capital");
        System.out.println("----------------------------------------------------------------------------------------------------------");

        for (Country c: countries) {
            if (c == null) {
                continue;
            }
            System.out.printf("%-6s | %-35s | %-15s | %-25s | %,12d | %-20s%n",
                                c.getCode(),
                                c.getName(),
                                c.getContinent(),
                                c.getRegion(),
                                c.getPopulation(),
                                c.getCapital() != null ? c.getCapital() : "N/A");
        }
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

        ArrayList<Country> countries = new ArrayList<>();

        if (n <= 0) {
            return countries;
        }

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

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, region);
            stmt.setInt(2, n);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Country country = new Country();

                    country.setCode(rs.getString("CountryCode"));
                    country.setName(rs.getString("CountryName"));
                    country.setContinent(rs.getString("Continent"));
                    country.setRegion(rs.getString("Region"));
                    country.setPopulation(rs.getInt("Population"));

                    String capital = rs.getString("CapitalName");

                    if (capital != null) {
                        country.setCapital(capital);
                    } else {
                        country.setCapital("N/A");
                    }

                    countries.add(country);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                    "Failed to retrieve top " + n +
                            " countries for region: " + region
            );
            e.printStackTrace();
        }

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
