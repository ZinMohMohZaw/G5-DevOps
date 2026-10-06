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

public class App {

    private Connection con = null;

    public Connection getConnection() {
        return con;
    }

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
            capitalReport.printAllCapitalCitiesByContinent();

            // =========================================================================
            // 4. POPULATION REPORTS (US26)
            // =========================================================================
            System.out.println("\n------------------------------------------------------------------------------------------");
            System.out.println(" [US26] WORLD POPULATION REPORT ");
            System.out.println("------------------------------------------------------------------------------------------");
            Population worldPop = populationReport.getWorldPopulation();
            populationReport.printWorldPopulationReport(worldPop);

        } else {
            System.err.println("Database connection failed. Aborting report execution.");
        }

        app.disconnect();
    }
}