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
     *         or an empty list if a database error occurs
     */
    public List<Language> getLanguagePopulationReport() {

        List<Language> languages = new ArrayList<>();

        if (connection == null) {
            System.out.println("Database connection is null.");
            return languages;
        }

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

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet =
                     statement.executeQuery()) {

            while (resultSet.next()) {

                String languageName =
                        resultSet.getString("Language");

                long totalSpeakers =
                        resultSet.getLong("TotalSpeakers");

                double worldPercentage =
                        resultSet.getDouble("WorldPercentage");

                Language language = new Language(
                        languageName,
                        totalSpeakers,
                        worldPercentage
                );

                languages.add(language);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Failed to retrieve language population report for US32: "
                            + e.getMessage()
            );
        }

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

        if (languages == null || languages.isEmpty()) {
            System.out.println(
                    "No language population data available."
            );
            return;
        }

        System.out.println();
        System.out.println(
                "============================================================================="
        );
        System.out.println(
                "                         LANGUAGE POPULATION REPORT"
        );
        System.out.println(
                "============================================================================="
        );

        System.out.printf(
                "%-20s | %20s | %20s%n",
                "Language",
                "Total Speakers",
                "World Population %"
        );

        System.out.println(
                "-----------------------------------------------------------------------------"
        );

        for (Language language : languages) {

            if (language == null) {
                continue;
            }

            System.out.printf(
                    "%-20s | %,20d | %19.2f%%%n",
                    language.getName(),
                    language.getTotalSpeakers(),
                    language.getPercentageOfWorld()
            );
        }

        System.out.println(
                "============================================================================="
        );
    }
}