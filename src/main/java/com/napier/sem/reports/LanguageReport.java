package com.napier.sem.reports;

import com.napier.sem.models.Language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles database retrieval and reporting for language population data.
 * Supports User Story US32.
 */
public class LanguageReport {

    // Store the database connection used by the report methods.
    private final Connection connection;

    /**
     * Constructs the language report generator with an active database connection.
     *
     * @param connection active MySQL database connection
     */
    public LanguageReport(Connection connection) {
        this.connection = connection;
    }

    // =========================================================================
    // US32: Language Population Report
    // =========================================================================

    /**
     * US32:
     * Retrieves the estimated number of people who speak Chinese, English,
     * Hindi, Spanish and Arabic worldwide. Results are ordered from the
     * largest to smallest number of speakers and include each language's
     * percentage of the world population.
     *
     * @return list of language population statistics,
     * or an empty list if a database error occurs
     */
    public List<Language> getLanguagePopulationReport() {

        // Create a list to store the language population statistics.
        List<Language> languages = new ArrayList<>();

        // Check whether the database connection is available.
        if (connection == null) {
            System.out.println("Database connection is null.");
            return languages;
        }

        // Define the SQL query to calculate worldwide language populations.
        // The query also calculates each language's percentage of the world population.
        String sql = """
                SELECT cl.Language,
                       ROUND(
                           SUM(
                               c.Population * cl.Percentage / 100.0
                           )
                       ) AS TotalSpeakers,
                       (
                           SUM(
                               c.Population * cl.Percentage / 100.0
                           ) / wp.WorldPopulation
                       ) * 100 AS WorldPercentage
                FROM countrylanguage cl
                INNER JOIN country c
                    ON cl.CountryCode = c.Code
                CROSS JOIN
                    (
                        SELECT SUM(Population) AS WorldPopulation
                        FROM country
                    ) wp
                WHERE cl.Language IN (
                    'Chinese',
                    'English',
                    'Hindi',
                    'Spanish',
                    'Arabic'
                )
                GROUP BY cl.Language, wp.WorldPopulation
                ORDER BY TotalSpeakers DESC
                """;

        // Prepare and execute the SQL query using the database connection.
        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            // Read each language record returned by the database.
            while (resultSet.next()) {

                // Retrieve the language name from the current result.
                String languageName =
                        resultSet.getString("Language");

                // Retrieve the estimated total number of speakers.
                long totalSpeakers =
                        resultSet.getLong("TotalSpeakers");

                // Retrieve the language's percentage of the world population.
                double worldPercentage =
                        resultSet.getDouble("WorldPercentage");

                // Create a Language object using the retrieved information.
                Language language = new Language(
                        languageName,
                        totalSpeakers,
                        worldPercentage
                );

                // Add the language statistics to the result list.
                languages.add(language);
            }

            // Handle database errors that occur while retrieving the US32 language population report.
        } catch (SQLException e) {
            // Display an error message if the database query fails.
            System.out.println(
                    "Failed to retrieve language population report for US32: "
                            + e.getMessage()
            );
        }

        // Return the language population statistics retrieved from the database.
        return languages;
    }

    /**
     * US32:
     * Prints the worldwide language population report.
     *
     * @param languages list of language population statistics
     */
    public void printLanguagePopulationReport(
            List<Language> languages) {

        // Check whether the language population data is available.
        if (languages == null || languages.isEmpty()) {
            System.out.println(
                    "No language population data available."
            );
            return;
        }

        // Print a blank line before displaying the report.
        System.out.println();

        // Print the top border and report title.
        System.out.println(
                "============================================================================="
        );
        System.out.println(
                "                         LANGUAGE POPULATION REPORT"
        );
        System.out.println(
                "============================================================================="
        );

        // Display the column headings for the language population information.
        System.out.printf(
                "%-20s | %20s | %20s%n",
                "Language",
                "Total Speakers",
                "World Population %"
        );

        // Print a separator between the headings and language records.
        System.out.println(
                "-----------------------------------------------------------------------------"
        );

        // Loop through each language in the result list.
        for (Language language : languages) {

            // Skip the current item if it is null.
            if (language == null) {
                continue;
            }

            // Print the language details in a formatted table row.
            // The output includes the total speakers and world population percentage.
            System.out.printf(
                    "%-20s | %,20d | %19.2f%%%n",
                    language.getName(),
                    language.getTotalSpeakers(),
                    language.getPercentageOfWorld()
            );
        }

        // Print the bottom border to finish the report.
        System.out.println(
                "============================================================================="
        );
    }
}