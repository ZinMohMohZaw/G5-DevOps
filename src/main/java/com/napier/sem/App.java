package com.napier.sem;

import com.napier.sem.models.CapitalCity;
import com.napier.sem.models.City;
import com.napier.sem.models.Country;
import com.napier.sem.models.Population;
import com.napier.sem.reports.CapitalCityReport;
import com.napier.sem.reports.CityReport;
import com.napier.sem.reports.CountryReport;
import com.napier.sem.reports.PopulationReport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for the World Population Reporting System.
 * Manages the MySQL database connection, initializes the report generators,
 * and executes the country, city, capital city,district, language and population reports.
 */
public class App {

    private Connection con = null;

    /**
     * Gets the current database connection.
     *
     * @return active database connection, or null if no connection is available
     */
    public Connection getConnection() {
        return con;
    }

    /**
     * Connects the application to the MySQL world database.
     * The method retries the connection several times if the database
     * is not immediately available.
     *
     * @param location database host and port
     * @param delay    delay in milliseconds before each connection attempt
     */
    public void connect(String location, int delay) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                Thread.sleep(delay);
                con = DriverManager.getConnection(
                        "jdbc:mysql://" + location + "/world?useSSL=false&allowPublicKeyRetrieval=true",
                        "root",
                        "example"
                );
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.err.println("Failed to connect to database attempt " + i);
                System.err.println(sqle.getMessage());
            } catch (InterruptedException ie) {
                System.err.println("Thread interrupted during connection delay.");
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Closes the active database connection if one exists.
     */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Database connection closed.");
            } catch (Exception e) {
                System.err.println("Error closing database connection");
            }
        }
    }

    /**
     * Application entry point.
     * Establishes the database connection, initializes the report generators,
     * executes the available reports and closes the connection when finished.
     *
     * @param args command-line arguments; the first argument may specify
     *             the database host and port
     */
    public static void main(String[] args) {
        App app = new App();
        String location = args.length < 1 ? "localhost:33060" : args[0];
        app.connect(location, 3000);

        System.out.println("\n==========================================================================================");
        System.out.println("                        DevOps World Population System Initialized                        ");
        System.out.println("==========================================================================================\n");

        if (app.getConnection() != null) {

            // Initialize All Report Generators
            CountryReport countryReport = new CountryReport(app.getConnection());
            CityReport cityReport = new CityReport(app.getConnection());
            CapitalCityReport capitalReport = new CapitalCityReport(app.getConnection());
            PopulationReport populationReport = new PopulationReport(app.getConnection());

            // =========================================================================
            // 1. COUNTRY REPORTS (US01, US02, US03)
            // =========================================================================
            try {
                System.out.println("\n------------------------------------------------------------------------------------------");
                System.out.println(" [US01] ALL COUNTRIES REPORT ");
                System.out.println("------------------------------------------------------------------------------------------");
                countryReport.generateCountryReport();
            } catch (SQLException e) {
                System.err.println("Error generating US01 Country Report: " + e.getMessage());
            }

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US02] CONTINENT COUNTRY REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleContinent = "Europe";
            List<Country> countriesByContinent = countryReport.getCountriesByContinent(sampleContinent);
            countryReport.printCountriesByContinentReport(countriesByContinent, sampleContinent);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US03] REGION COUNTRY REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleRegion = "Caribbean";
            ArrayList<Country> countriesByRegion = countryReport.getCountriesByRegion(sampleRegion);
            countryReport.printCountriesByRegion(countriesByRegion, sampleRegion);

            // =========================================================================
            // 2. CITY REPORTS (US07, US08, US09, US10)
            // =========================================================================
            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US07] ALL CITIES REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            List<City> allCities = cityReport.getAllCitiesSortedByPopulation();
            cityReport.generateCityReport(allCities);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US08] CITIES BY CONTINENT REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            cityReport.generateContinentCityReport("Asia");

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US09] CITIES BY REGION REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String cityRegion = "Polynesia";
            List<City> citiesByRegion = cityReport.getCitiesByRegion(cityRegion);
            cityReport.printCitiesByRegionReport(citiesByRegion, cityRegion);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US10] CITIES BY COUNTRY REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String cityCountry = "France";
            List<City> citiesByCountry = cityReport.getCitiesByCountry(cityCountry);
            cityReport.printCitiesByCountryReport(citiesByCountry, cityCountry);

            // =========================================================================
            // 3. CAPITAL CITY REPORTS (US17, US18)
            // =========================================================================
            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US17] ALL CAPITAL CITIES REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            List<CapitalCity> worldCapitals = capitalReport.getAllCapitalCitiesByPopulation();
            capitalReport.printCapitalCities(worldCapitals);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US18] ALL CAPITAL CITIES BY CONTINENT REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleCapitalContinent = "Asia";
            capitalReport.printCapitalCitiesByContinent(sampleCapitalContinent);

            // =========================================================================
            // 4. POPULATION REPORTS (US26)
            // =========================================================================
            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US26] WORLD POPULATION REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            Population worldPop = populationReport.getWorldPopulation();
            populationReport.printWorldPopulationReport(worldPop);

            // =========================================================================
            // SPRINT 2 REPORTS (US04, US05, US06, US11 - US16, US19)
            // =========================================================================

            // --- 1. COUNTRY REPORTS (US04, US05, US06) ---

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US04] TOP N COUNTRIES WORLDWIDE REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            int topNWorldCountries = 5;
            List<Country> topCountriesWorld = countryReport.getTopNCountriesWorldwide(topNWorldCountries);
            countryReport.printTopNCountriesWorldwideReport(topCountriesWorld, topNWorldCountries);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US05] TOP N COUNTRIES BY CONTINENT REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleContinentForTopN = "Asia";
            int topNContinentCountries = 5;
            List<Country> topCountriesContinent = countryReport.getTopNCountriesByContinent(sampleContinentForTopN, topNContinentCountries);
            countryReport.printTopNCountriesByContinentReport(topCountriesContinent, sampleContinentForTopN, topNContinentCountries);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US06] TOP N COUNTRIES BY REGION REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleRegionForTopN = "Caribbean";
            int topNRegionCountries = 3;
            List<Country> topCountriesRegion = countryReport.getTopNCountriesByRegion(sampleRegionForTopN, topNRegionCountries);
            countryReport.printTopNCountriesByRegionReport(topCountriesRegion, sampleRegionForTopN, topNRegionCountries);


            // --- 2. CITY REPORTS (US11, US12, US13, US14, US15, US16) ---

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US11] CITIES BY DISTRICT REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleDistrict = "California";
            List<City> citiesByDistrict = cityReport.getCitiesByDistrict(sampleDistrict);
            cityReport.printCitiesByDistrictReport(citiesByDistrict, sampleDistrict);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US12] TOP N CITIES WORLDWIDE REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            int topNWorldCities = 10;
            List<City> topCitiesWorld = cityReport.getTopNCities(topNWorldCities);
            cityReport.printTopNCitiesWorldwideReport(topCitiesWorld, topNWorldCities);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US13] TOP N CITIES BY CONTINENT REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleContinentCity = "Asia";
            int topNContinentCities = 5;
            List<City> topCitiesContinent = cityReport.getTopNCitiesByContinent(sampleContinentCity, topNContinentCities);
            cityReport.printTopNCitiesByContinentReport(topCitiesContinent, sampleContinentCity, topNContinentCities);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US14] TOP N CITIES BY REGION REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleRegionCity = "Caribbean";
            int topNRegionCities = 5;
            List<City> topCitiesRegion = cityReport.getTopNCitiesByRegion(sampleRegionCity, topNRegionCities);
            cityReport.printTopNCitiesByRegionReport(topCitiesRegion, sampleRegionCity, topNRegionCities);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US15] TOP N CITIES BY COUNTRY REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleCountryCity = "France";
            int topNCountryCities = 5;
            List<City> topCitiesCountry = cityReport.getTopNCitiesByCountry(sampleCountryCity, topNCountryCities);
            cityReport.printTopNCitiesByCountryReport(topCitiesCountry, sampleCountryCity, topNCountryCities);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US16] TOP N CITIES BY DISTRICT REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleDistrictTopN = "California";
            int topNDistrictCities = 3;
            List<City> topCitiesDistrict = cityReport.getTopNCitiesByDistrict(sampleDistrictTopN, topNDistrictCities);
            cityReport.printTopNCitiesByDistrictReport(topCitiesDistrict, sampleDistrictTopN, topNDistrictCities);


            // --- 3. CAPITAL CITY REPORTS (US19) ---

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US19] CAPITAL CITIES BY REGION REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleCapitalRegion = "Caribbean";
            List<CapitalCity> capitalCitiesRegion = capitalReport.getCapitalCitiesByRegion(sampleCapitalRegion);
            capitalReport.printCapitalCitiesByRegionReport(capitalCitiesRegion, sampleCapitalRegion);

            // =========================================================================
            // Sprint 3 Reports (US20, US21, US22, US27, US28, US29)
            // =========================================================================

            // --- CAPITAL CITY TOP N REPORTS (US20, US21, US22) ---
            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US20] TOP N CAPITAL CITIES WORLDWIDE REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            int topNCapitalWorld = 5;
            List<CapitalCity> topCapitalsWorld = capitalReport.getTopNCapitalCitiesWorldwide(topNCapitalWorld);
            capitalReport.printTopNCapitalCitiesWorldwideReport(topCapitalsWorld, topNCapitalWorld);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US21] TOP N CAPITAL CITIES BY CONTINENT REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleCapitalContinentTopN = "Asia";
            int topNCapitalContinent = 5;
            List<CapitalCity> topCapitalsContinent = capitalReport.getTopNCapitalCitiesByContinent(sampleCapitalContinentTopN, topNCapitalContinent);
            capitalReport.printTopNCapitalCitiesByContinentReport(topCapitalsContinent, sampleCapitalContinentTopN, topNCapitalContinent);

            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US22] TOP N CAPITAL CITIES BY REGION REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String sampleCapitalRegionTopN = "Caribbean";
            int topNCapitalRegion = 3;
            List<CapitalCity> topCapitalsRegion = capitalReport.getTopNCapitalCitiesByRegion(sampleCapitalRegionTopN, topNCapitalRegion);
            capitalReport.printTopNCapitalCitiesByRegionReport(topCapitalsRegion, sampleCapitalRegionTopN, topNCapitalRegion);

            // --- POPULATION REPORTS BY AREA (US27, US28, US29) ---
            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US27] POPULATION BY CONTINENT REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            String samplePopContinent = "Asia";
            Population popContinent = populationReport.getPopulationByContinent(samplePopContinent);
            populationReport.printPopulationByContinentReport(popContinent);
        } else {
            System.err.println("Database connection failed. Aborting report execution.");
        }

        app.disconnect();
    }
}