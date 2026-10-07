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
     */
    public List<City> getCitiesByDistrict(String district) {
        List<City> cities = new ArrayList<>();
        // TODO: Implement SQL query with WHERE city.District = ?
        return cities;
    }

    /**
     * US11-T2: Formats and prints the District City Report.
     */
    public void printCitiesByDistrictReport(List<City> cities, String district) {
        // TODO: Implement report output logic
    }

    // =========================================================================
    // US12: Top N Populated Cities Worldwide
    // Assigned to: Developer 1 (US12-T1), Developer 3 (US12-T2)
    // =========================================================================

    /**
     * US12-T1: Retrieves top N populated cities in the world.
     */
    public List<City> getTopNCitiesWorldwide(int n) {
        List<City> cities = new ArrayList<>();
        // TODO: Implement SQL query with LIMIT ?
        return cities;
    }

    /**
     * US12-T2: Formats and prints the Top N World City Report.
     */
    public void printTopNCitiesWorldwideReport(List<City> cities, int n) {
        // TODO: Implement report output logic
    }

    // =========================================================================
    // US13: Top N Populated Cities in a Continent
    // Assigned to: Developer 2 (US13-T1), Developer 4 (US13-T2)
    // =========================================================================

    /**
     * US13-T1: Retrieves top N populated cities in a continent.
     */
    public List<City> getTopNCitiesByContinent(String continent, int n) {
        List<City> cities = new ArrayList<>();
        // TODO: Implement SQL query with WHERE country.Continent = ? AND LIMIT ?
        return cities;
    }

    /**
     * US13-T2: Formats and prints the Top N Continent City Report.
     */
    public void printTopNCitiesByContinentReport(List<City> cities, String continent, int n) {
        // TODO: Implement report output logic
    }

    // =========================================================================
    // US14: Top N Populated Cities in a Region
    // Assigned to: Developer 2 (US14-T1), Developer 4 (US14-T2)
    // =========================================================================

    /**
     * US14-T1: Retrieves top N populated cities in a region.
     */
    public List<City> getTopNCitiesByRegion(String region, int n) {
        List<City> cities = new ArrayList<>();
        // TODO: Implement SQL query with WHERE country.Region = ? AND LIMIT ?
        return cities;
    }

    /**
     * US14-T2: Formats and prints the Top N Region City Report.
     */
    public void printTopNCitiesByRegionReport(List<City> cities, String region, int n) {
        // TODO: Implement report output logic
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
     */
    public void printTopNCitiesByCountryReport(List<City> cities, String country, int n) {
        // TODO: Implement report output logic
    }

    // =========================================================================
    // US16: Top N Populated Cities in a District
    // Assigned to: Developer 3 (US16-T1), Developer 4 (US16-T2)
    // =========================================================================

    /**
     * US16-T1: Retrieves top N populated cities in a district.
     */
    public List<City> getTopNCitiesByDistrict(String district, int n) {
        List<City> cities = new ArrayList<>();
        // TODO: Implement SQL query with WHERE city.District = ? AND LIMIT ?
        return cities;
    }

    /**
     * US16-T2: Formats and prints the Top N District City Report.
     */
    public void printTopNCitiesByDistrictReport(List<City> cities, String district, int n) {
        // TODO: Implement report output logic
    }
}