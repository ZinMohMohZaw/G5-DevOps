package com.napier.sem.reports;

import com.napier.sem.models.Country;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

public class CountryReportTest {
    public static void main(String[] args)
    {
        try
        {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:33060/world?useSSL=false&allowPublicKeyRetrieval=true",
                    "root",
                    "example"
            );

            System.out.println("Connected successfully.");

            CountryReport report = new CountryReport();

            ArrayList<Country> countries =
                    report.getCountriesByRegion(
                            con,
                            "Southern and Central Asia"
                    );

            System.out.println(
                    "Number of countries found: " + countries.size()
            );

            for (Country country : countries)
            {
                System.out.println(
                        country.getCode() + " | " +
                                country.getName() + " | " +
                                country.getContinent() + " | " +
                                country.getRegion() + " | " +
                                country.getPopulation() + " | " +
                                country.getCapital()
                );
            }

            con.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}