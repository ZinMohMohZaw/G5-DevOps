package com.napier.sem.reports;

import com.napier.sem.models.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval and reporting for City data (US07, US08, US09, US10).
 */
public class CityReport {

    private static final String TABLE_HEADER_FORMAT = "%-30s %-30s %-25s %15s%n";
    private static final String TABLE_ROW_FORMAT = "%-30s %-30s %-25s %,15d%n";
    private static final String DIVIDER_LINE = "--------------------------------------------------------------------------------------------------";
    private static final String BORDER_LINE  = "==================================================================================================";

    private final Connection connection;

    /**
     * Constructs the report generator with an active database connection.
     *
     * @param connection Active database connection
     */
    public CityReport(Connection connection) {
        this.connection = connection;
    }

    // =========================================================================
    // US07: All Cities Report
    // =========================================================================

    /**
     * US07-T1: Retrieves all cities from the database sorted by population descending.
     *
     * @return List of cities sorted by population descending
     */
    public List<City> getAllCitiesSortedByPopulation() {
        List<City> cities = new ArrayList<>();

        String sql = """
                SELECT city.Name AS CityName,
                       country.Name AS CountryName,
                       city.District,
                       city.Population
                FROM city
                INNER JOIN country ON city.CountryCode = country.Code
                ORDER BY city.Population DESC
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                cities.add(mapResultSetToCity(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error executing US07-T1 (getAllCitiesSortedByPopulation): " + e.getMessage());
        }

        return cities;
    }

    /**
     * US07-T2: Generates and prints the formatted All Cities Report.
     *
     * @param cities List of cities returned by US07-T1
     */
    public void generateCityReport(List<City> cities) {
        printCityReportTable(cities, "ALL CITIES REPORT");
    }

    // =========================================================================
    // US08: Cities by Continent Report
    // =========================================================================

    /**
     * US08-T1: Retrieves all cities belonging to countries within the selected continent,
     * sorted by population from highest to lowest.
     *
     * @param continent Selected continent
     * @return List of cities in the continent
     */
    public List<City> getCitiesByContinent(String continent) {
        List<City> cities = new ArrayList<>();

        String sql = """
                SELECT city.Name AS CityName,
                       country.Name AS CountryName,
                       city.District,
                       city.Population
                FROM city
                INNER JOIN country ON city.CountryCode = country.Code
                WHERE country.Continent = ?
                ORDER BY city.Population DESC
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, continent);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(mapResultSetToCity(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error executing US08-T1 (getCitiesByContinent) for " + continent + ": " + e.getMessage());
        }

        return cities;
    }

    /**
     * US08-T2: Generates and prints the formatted Continent City Report.
     *
     * @param continent Selected continent name
     */
    public void generateContinentCityReport(String continent) {
        List<City> cities = getCitiesByContinent(continent);
        printCityReportTable(cities, "CONTINENT CITY REPORT: " + (continent != null ? continent.toUpperCase() : "N/A"));
    }

    // =========================================================================
    // US09: Cities by Region Report
    // =========================================================================

    /**
     * US09-T1: Retrieves all cities belonging to countries within the specified region,
     * sorted by population from highest to lowest.
     *
     * @param region Target region to search for
     * @return List of cities in the specified region
     */
    public List<City> getCitiesByRegion(String region) {
        List<City> cities = new ArrayList<>();

        String sql = """
                SELECT city.Name AS CityName,
                       country.Name AS CountryName,
                       city.District,
                       city.Population
                FROM city
                INNER JOIN country ON city.CountryCode = country.Code
                WHERE country.Region = ?
                ORDER BY city.Population DESC
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, region);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(mapResultSetToCity(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error executing US09-T1 (getCitiesByRegion) for " + region + ": " + e.getMessage());
        }

        return cities;
    }

    /**
     * US09-T2: Generates and prints the formatted Region City Report.
     *
     * @param cities     List of City objects returned by US09-T1
     * @param regionName Target region name for report header
     */
    public void printCitiesByRegionReport(List<City> cities, String regionName) {
        printCityReportTable(cities, "REGION CITY REPORT: " + (regionName != null ? regionName.toUpperCase() : "N/A"));
    }

    // =========================================================================
    // US10: Cities by Country Report
    // =========================================================================

    /**
     * US10-T1: Retrieves all cities belonging to the specified country,
     * sorted by population from largest to smallest.
     *
     * @param country Target country to search for
     * @return List of cities in the specified country, sorted by population descending
     */
    public List<City> getCitiesByCountry(String country) {
        List<City> cities = new ArrayList<>();

        String sql = """
                SELECT city.Name AS CityName,
                       country.Name AS CountryName,
                       city.District,
                       city.Population
                FROM city
                INNER JOIN country ON city.CountryCode = country.Code
                WHERE country.Name = ?
                ORDER BY city.Population DESC
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, country);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(mapResultSetToCity(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error executing US10-T1 (getCitiesByCountry) for " + country + ": " + e.getMessage());
        }

        return cities;
    }

    /**
     * US10-T2: Generates and prints the formatted Country City Report.
     *
     * @param cities      List of City objects returned by US10-T1
     * @param countryName Target country name for report header
     */
    public void printCitiesByCountryReport(List<City> cities, String countryName) {
        printCityReportTable(cities, "COUNTRY CITY REPORT: " + (countryName != null ? countryName.toUpperCase() : "N/A"));
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    /**
     * Maps a single ResultSet row into a City object.
     */
    private City mapResultSetToCity(ResultSet rs) throws SQLException {
        City city = new City();
        city.setName(rs.getString("CityName"));
        city.setCountry(rs.getString("CountryName"));
        city.setDistrict(rs.getString("District"));
        city.setPopulation(rs.getInt("Population"));
        return city;
    }

    /**
     * Universal table printing helper for city reports.
     */
    private void printCityReportTable(List<City> cities, String title) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No city data available for: " + title);
            return;
        }

        System.out.println();
        System.out.println(BORDER_LINE);
        System.out.printf("%" + ((BORDER_LINE.length() + title.length()) / 2) + "s%n", title);
        System.out.println(BORDER_LINE);

        System.out.printf(TABLE_HEADER_FORMAT, "City", "Country", "District", "Population");
        System.out.println(DIVIDER_LINE);

        for (City city : cities) {
            if (city == null) continue;

            System.out.printf(
                    TABLE_ROW_FORMAT,
                    city.getName(),
                    city.getCountry(),
                    city.getDistrict(),
                    city.getPopulation()
            );
        }

        System.out.println(BORDER_LINE);
        System.out.println("Total Cities Listed: " + cities.size());
        System.out.println(BORDER_LINE + "\n");
    }


    // =========================================================================
    // US11: All Cities in a District
    // Assigned to: Developer 1 (US11-T1), Developer 3 (US11-T2)
    // =========================================================================

    /**
     * US11-T1: Retrieves all cities in a district sorted by population.
     *
     * @param district Target district to search for
     * @return List of cities in the specified district,
     *         sorted by population descending
     */
    public List<City> getCitiesByDistrict(String district) {

        List<City> cities = new ArrayList<>();

        String sql = """
            SELECT city.Name AS CityName,
                   country.Name AS CountryName,
                   city.District,
                   city.Population
            FROM city
            INNER JOIN country ON city.CountryCode = country.Code
            WHERE city.District = ?
            ORDER BY city.Population DESC
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, district);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    cities.add(mapResultSetToCity(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error executing US11-T1 (getCitiesByDistrict) for "
                            + district + ": " + e.getMessage()
            );
        }

        return cities;
    }


    /**
     * US11-T2:
     * Generates and prints the formatted District City Report.
     *
     * @param cities List of City objects retrieved for the selected district
     * @param district The district name displayed in the report heading
     */
    public void printCitiesByDistrictReport(List<City> cities, String district) {

        // Display a message if no cities are available for the district.
        if (cities == null || cities.isEmpty()) {
            System.out.println("No city data available for district: "
                    + (district != null ? district : "N/A"));
            return;
        }

        // Prepare the report title using the selected district name.
        String title = "DISTRICT CITY REPORT: "
                + (district != null ? district.toUpperCase() : "N/A");

        // Print the report heading.
        System.out.println();
        System.out.println(BORDER_LINE);
        System.out.printf("%" + ((BORDER_LINE.length() + title.length()) / 2)
                + "s%n", title);
        System.out.println(BORDER_LINE);

        // Print the column headings using the existing city-report format.
        System.out.printf(TABLE_HEADER_FORMAT,
                "City", "Country", "District", "Population");
        System.out.println(DIVIDER_LINE);

        // Print each city in the order provided by the retrieval method.
        for (City city : cities) {
            if (city == null) {
                continue;
            }

            System.out.printf(
                    TABLE_ROW_FORMAT,
                    city.getName(),
                    city.getCountry(),
                    city.getDistrict(),
                    city.getPopulation()
            );
        }

        // Print the total number of cities included in the report.
        System.out.println(BORDER_LINE);
        System.out.println("Total Cities Listed: " + cities.size());
        System.out.println(BORDER_LINE);
        System.out.println();
    }


    // =========================================================================
    // US12: Top N Populated Cities Worldwide
    // Assigned to: Developer 1 (US12-T1), Developer 3 (US12-T2)
    // =========================================================================

    /**
     * US12-T1: Retrieves the top N cities worldwide sorted by population.
     *
     * @param limit Number of cities to retrieve
     * @return List of top N cities sorted by population descending
     */
    public List<City> getTopNCities(int limit) {

        List<City> cities = new ArrayList<>();

        String sql = """
            SELECT city.Name AS CityName,
                   country.Name AS CountryName,
                   city.District,
                   city.Population
            FROM city
            INNER JOIN country ON city.CountryCode = country.Code
            ORDER BY city.Population DESC
            LIMIT ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, limit);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    cities.add(mapResultSetToCity(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error executing US12-T1 (getTopNCities): "
                            + e.getMessage()
            );
        }

        return cities;
    }


    /**
     * US12-T2:
     * Generates and prints the formatted Top N World City Report.
     *
     * @param cities List of City objects retrieved by US12-T1
     * @param n The number of top populated cities to display
     */
    public void printTopNCitiesWorldwideReport(List<City> cities, int n) {

        // Check whether the requested number of cities is valid.
        if (n <= 0) {
            System.out.println("Please enter a valid number of cities.");
            return;
        }

        // Check whether any city data is available.
        if (cities == null || cities.isEmpty()) {
            System.out.println("No city data available for the Top N World City Report.");
            return;
        }

        // Print the report using the exist city table format.
        int numberOfCities = Math.min(n, cities.size());

        List<City> topCities = cities.subList(0, numberOfCities);

        printCityReportTable(topCities, "TOP " + numberOfCities
                + " POPULATED CITIES WORLDWIDE REPORT");
    }

    // =========================================================================
    // US13: Top N Populated Cities in a Continent
    // Assigned to: Developer 2 (US13-T1), Developer 4 (US13-T2)
    // =========================================================================

    /**
     * US13-T1: Retrieves the top N populated cities in a continent sorted by population (descending).
     *
     * @param continent Target continent name to filter cities by
     * @param n         The number of top populated cities to retrieve
     * @return List of City objects matching criteria, or an empty list if inputs are invalid or on database error
     */
    public List<City> getTopNCitiesByContinent(String continent, int n) {
        List<City> cities = new ArrayList<>();
        // validate input parameters
        if (continent == null || continent.isBlank()) {
            System.out.println(
                    "Invalid parameter continent for US13-T1: Continent cannot be empty.");
            return cities;
        }

        if (n <= 0) {
            System.out.println(
                    "Invalid parameter N for US13-T1: N must be greater than 0.");
            return cities;
        }

        if (connection == null) {
            System.out.println(
                    "Database connection is not available for US13-T1.");
            return cities;
        }

        String sql = """
            SELECT city.Name AS CityName,
                   country.Name AS CountryName,
                   city.District,
                   city.Population
            FROM city
            INNER JOIN country ON city.CountryCode = country.Code
            WHERE country.Continent = ?
            ORDER BY city.Population DESC
            LIMIT ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, continent);
            stmt.setInt(2, n);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(mapResultSetToCity(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println(
                    "Error executing US13-T1 for " +
                            continent + ": " + e.getMessage());
        }
        return cities;
    }

    /**
     * US13-T2: Formats and prints the Top N Continent City Report.
     *
     * @param cities    List of City objects returned by US13-T1
     * @param continent Target continent name for report header
     * @param n         The number of top populated cities requested
     */
    public void printTopNCitiesByContinentReport(List<City> cities, String continent, int n) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No cities found for continent: " + (continent != null ? continent : "N/A"));
            return;
        }

        System.out.println("=========================================================================================");
        System.out.printf("                        TOP %d POPULATED CITIES IN CONTINENT: %s%n", n, continent.toUpperCase());
        System.out.println("=========================================================================================");
        System.out.printf("%-35s | %-25s | %-25s | %-12s%n",
                "Name", "Country", "District", "Population");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (City c : cities) {
            if (c == null) continue;
            System.out.printf("%-35s | %-25s | %-25s | %,12d%n",
                    c.getName(),
                    c.getCountry(),
                    c.getDistrict(),
                    c.getPopulation()
            );
        }

        System.out.println("=========================================================================================");
        System.out.println("Total Cities Listed: " + cities.size());
        System.out.println("=========================================================================================\n");
    }

    // =========================================================================
    // US14: Top N Populated Cities in a Region
    // Assigned to: Developer 2 (US14-T1), Developer 4 (US14-T2)
    // =========================================================================

    /**
     * US14-T1: Retrieves top N populated cities in a specified region, sorted by population (descending).
     *
     * @param region The region in which to retrieve the cities
     * @param n The number of top populated cities to retrieve
     * @return List of City objects, or an empty list if the region is invalid,
     *         n <= 0, the database connection is unavailable, or a database error occurs
     */
    public List<City> getTopNCitiesByRegion(String region, int n) {
        List<City> cities = new ArrayList<>();
        if (region == null || region.isBlank()) {
            System.out.println("Invalid parameter region for US14-T1: Region cannot be empty.");
            return cities;
        }

        if (n <= 0) {
            System.out.println("Invalid parameter N for US14-T1: N must be greater than 0.");
            return cities;
        }

        if (connection == null) {
            System.out.println("Database connection is not available for US14-T1.");
            return cities;
        }

        String sql = """
            SELECT city.Name AS CityName,
                   country.Name AS CountryName,
                   city.District,
                   city.Population
            FROM city
            INNER JOIN country ON city.CountryCode = country.Code
            WHERE country.Region = ?
            ORDER BY city.Population DESC
            LIMIT ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, region);
            stmt.setInt(2, n);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(mapResultSetToCity(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error executing US14-T1 (getTopNCitiesByRegion) for "
                    + region + ": " + e.getMessage());
        }
        return cities;
    }

    /**
     * US14-T2: Formats and prints the Top N Region City Report.
     *
     * @param cities List of City objects returned by US14-T1
     * @param region Target region name for report header
     * @param n      The number of top populated cities requested
     */
    public void printTopNCitiesByRegionReport(List<City> cities, String region, int n) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No cities found for region: " + (region != null ? region : "N/A"));
            return;
        }

        System.out.println("=========================================================================================");
        System.out.printf("                        TOP %d POPULATED CITIES IN REGION: %s%n", n, region.toUpperCase());
        System.out.println("=========================================================================================");
        System.out.printf("%-35s | %-25s | %-25s | %-12s%n",
                "Name", "Country", "District", "Population");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (City c : cities) {
            if (c == null) continue;
            System.out.printf("%-35s | %-25s | %-25s | %,12d%n",
                    c.getName(),
                    c.getCountry(),
                    c.getDistrict(),
                    c.getPopulation()
            );
        }

        System.out.println("=========================================================================================");
        System.out.println("Total Cities Listed: " + cities.size());
        System.out.println("=========================================================================================\n");
    }

    // =========================================================================
    // US15: Top N Populated Cities in a Country
    // Assigned to: Developer 3 (US15-T1), Developer 4 (US15-T2)
    // =========================================================================

    /**
     * US15-T1: Retrieves top N populated cities in a country.
     *
     * @param country Target country to search for
     * @param n Number of top cities to retrieve
     * @return List of top N cities in the specified country,
     *         sorted by population descending
     */
    public List<City> getTopNCitiesByCountry(String country, int n) {
        List<City> cities = new ArrayList<>();

        String sql = """
            SELECT city.Name AS CityName,
                   country.Name AS CountryName,
                   city.District,
                   city.Population
            FROM city
            INNER JOIN country ON city.CountryCode = country.Code
            WHERE country.Name = ?
            ORDER BY city.Population DESC
            LIMIT ?
            """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, country);
            stmt.setInt(2, n);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    cities.add(mapResultSetToCity(rs));
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Error executing US15-T1 (getTopNCitiesByCountry) for "
                            + country + ": " + e.getMessage()
            );
        }

        return cities;
    }

    /**
     * US15-T2: Formats and prints the Top N Country City Report.
     *
     * @param cities  List of City objects returned by US15-T1
     * @param country Target country name for report header
     * @param n       The number of top populated cities requested
     */
    public void printTopNCitiesByCountryReport(List<City> cities, String country, int n) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No cities found for country: " + (country != null ? country : "N/A"));
            return;
        }

        System.out.println();
        System.out.println("=========================================================================================");
        System.out.printf("                TOP %d POPULATED CITIES IN COUNTRY: %s%n", n, country.toUpperCase());
        System.out.println("=========================================================================================");
        System.out.printf("%-35s | %-25s | %-20s | %-12s%n",
                "Name", "Country", "District", "Population");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (City c : cities) {
            if (c == null) continue;
            System.out.printf("%-35s | %-25s | %-20s | %,12d%n",
                    c.getName(),
                    c.getCountry(),
                    c.getDistrict(),
                    c.getPopulation()
            );
        }

        System.out.println("=========================================================================================");
        System.out.println("Total Cities Listed: " + cities.size());
        System.out.println("=========================================================================================\n");
    }

    // =========================================================================
    // US16: Top N Populated Cities in a District
    // Assigned to: Developer 3 (US16-T1), Developer 4 (US16-T2)
    // =========================================================================

    /**
     * US16-T1:
     * Retrieves the top N populated cities within a selected district,
     * sorted by population in descending order.
     *
     * @param district The district name to search for
     * @param n The number of top populated cities to retrieve
     * @return List of City objects, or an empty list if n <= 0 or on database error
     */
    public List<City> getTopNCitiesByDistrict(String district, int n) {

        // Create a list to store the cities found by the query.
        List<City> cities = new ArrayList<>();

        // Check that a valid district and positive number of cities are provided.
        if (district == null || district.trim().isEmpty() || n <= 0) {
            return cities;
        }

        // SQL query to find cities in the selected district.
        // Results are sorted from highest to lowest population.
        String sql =
                "SELECT city.Name, country.Name AS Country, city.District, city.Population " +
                        "FROM city " +
                        "INNER JOIN country ON city.CountryCode = country.Code " +
                        "WHERE city.District = ? " +
                        "ORDER BY city.Population DESC " +
                        "LIMIT ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // Set the district and number of cities for the query.
            stmt.setString(1, district);
            stmt.setInt(2, n);

            try (ResultSet rs = stmt.executeQuery()) {

                // Read each city returned by the database.
                while (rs.next()) {

                    // Create a City object and store its details.
                    City city = new City();

                    city.setName(rs.getString("Name"));
                    city.setCountry(rs.getString("Country"));
                    city.setDistrict(rs.getString("District"));
                    city.setPopulation(rs.getInt("Population"));

                    // Add the city to the result list.
                    cities.add(city);
                }
            }

        } catch (Exception e) {
            // Display an error message if the database query fails.
            System.err.println("Error retrieving top cities by district: " + e.getMessage());
        }

        // Return the list of cities found.
        return cities;
    }

    /**
     * US16-T2: Formats and prints the Top N District City Report.
     *
     * @param cities   List of City objects returned by US16-T1
     * @param district Target district name for report header
     * @param n        The number of top populated cities requested
     */
    public void printTopNCitiesByDistrictReport(List<City> cities, String district, int n) {
        if (cities == null || cities.isEmpty()) {
            System.out.println("No cities found for district: " + (district != null ? district : "N/A"));
            return;
        }

        System.out.println();
        System.out.println("=========================================================================================");
        System.out.printf("                TOP %d POPULATED CITIES IN DISTRICT: %s%n", n, district.toUpperCase());
        System.out.println("=========================================================================================");
        System.out.printf("%-35s | %-25s | %-20s | %-12s%n",
                "Name", "Country", "District", "Population");
        System.out.println("-----------------------------------------------------------------------------------------");

        for (City c : cities) {
            if (c == null) continue;
            System.out.printf("%-35s | %-25s | %-20s | %,12d%n",
                    c.getName(),
                    c.getCountry(),
                    c.getDistrict(),
                    c.getPopulation()
            );
        }

        System.out.println("=========================================================================================");
        System.out.println("Total Cities Listed: " + cities.size());
        System.out.println("=========================================================================================\n");
    }
}