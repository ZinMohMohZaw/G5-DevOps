package com.napier.sem.reports;

import com.napier.sem.App;
import com.napier.sem.models.Language;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the LanguageReport class.
 * Verifies language population data retrieval, ordering,
 * population totals, percentages and database error handling.
 */
class LanguageReportIntegrationTest {

    private static App app;
    private static LanguageReport languageReport;

    /**
     * Establishes a database connection and creates the
     * LanguageReport instance before all integration tests.
     */
    @BeforeAll
    static void setUp() {

        app =
                new App();

        app.connect(
                "localhost:3306",
                1000
        );

        assertNotNull(
                app.getConnection(),
                "Database connection should be available for integration testing."
        );

        languageReport =
                new LanguageReport(
                        app.getConnection()
                );
    }

    /**
     * Closes the database connection after all
     * language report integration tests have completed.
     */
    @AfterAll
    static void tearDown() {

        if (app != null) {

            app.disconnect();
        }
    }

    /**
     * Tests that the language population report returns
     * all five required languages.
     */
    @Test
    void testLanguagePopulationReportReturnsFiveLanguages() {

        List<Language> languages =
                languageReport
                        .getLanguagePopulationReport();

        assertAll(
                "Five required languages",

                () -> assertNotNull(languages),

                () -> assertEquals(
                        5,
                        languages.size()
                )
        );

        languageReport
                .printLanguagePopulationReport(
                        languages
                );
    }

    /**
     * Tests that the five required languages are returned
     * in the expected descending population order.
     */
    @Test
    void testLanguagePopulationReportOrdering() {

        List<Language> languages =
                languageReport
                        .getLanguagePopulationReport();

        assertAll(
                "Language population ordering",

                () -> assertEquals(
                        5,
                        languages.size()
                ),

                () -> assertEquals(
                        "Chinese",
                        languages.get(0).getName()
                ),

                () -> assertEquals(
                        "Hindi",
                        languages.get(1).getName()
                ),

                () -> assertEquals(
                        "Spanish",
                        languages.get(2).getName()
                ),

                () -> assertEquals(
                        "English",
                        languages.get(3).getName()
                ),

                () -> assertEquals(
                        "Arabic",
                        languages.get(4).getName()
                )
        );
    }

    /**
     * Tests that the calculated total number of speakers
     * for each required language matches the expected values.
     */
    @Test
    void testLanguagePopulationSpeakerTotals() {

        List<Language> languages =
                languageReport
                        .getLanguagePopulationReport();

        assertAll(
                "Language speaker totals",

                () -> assertEquals(
                        5,
                        languages.size()
                ),

                () -> assertEquals(
                        1191843539L,
                        languages.get(0)
                                .getTotalSpeakers()
                ),

                () -> assertEquals(
                        405633070L,
                        languages.get(1)
                                .getTotalSpeakers()
                ),

                () -> assertEquals(
                        355029462L,
                        languages.get(2)
                                .getTotalSpeakers()
                ),

                () -> assertEquals(
                        347077867L,
                        languages.get(3)
                                .getTotalSpeakers()
                ),

                () -> assertEquals(
                        233839239L,
                        languages.get(4)
                                .getTotalSpeakers()
                )
        );
    }

    /**
     * Tests that the percentage of the world population
     * represented by each required language is calculated correctly.
     */
    @Test
    void testLanguagePopulationPercentages() {

        List<Language> languages =
                languageReport
                        .getLanguagePopulationReport();

        assertAll(
                "Language population percentages",

                () -> assertEquals(
                        5,
                        languages.size()
                ),

                () -> assertEquals(
                        19.61,
                        languages.get(0)
                                .getPercentageOfWorld(),
                        0.01
                ),

                () -> assertEquals(
                        6.67,
                        languages.get(1)
                                .getPercentageOfWorld(),
                        0.01
                ),

                () -> assertEquals(
                        5.84,
                        languages.get(2)
                                .getPercentageOfWorld(),
                        0.01
                ),

                () -> assertEquals(
                        5.71,
                        languages.get(3)
                                .getPercentageOfWorld(),
                        0.01
                ),

                () -> assertEquals(
                        3.85,
                        languages.get(4)
                                .getPercentageOfWorld(),
                        0.01
                )
        );
    }

    /**
     * Tests that the language population report is sorted
     * from the highest to the lowest total number of speakers.
     */
    @Test
    void testLanguagePopulationIsSortedDescending() {

        List<Language> languages =
                languageReport
                        .getLanguagePopulationReport();

        assertAll(
                "Language population descending order",

                () -> assertNotNull(languages),

                () -> assertFalse(
                        languages.isEmpty()
                ),

                () -> {
                    for (int i = 0;
                         i < languages.size() - 1;
                         i++) {

                        assertTrue(
                                languages.get(i)
                                        .getTotalSpeakers()
                                        >=
                                        languages
                                                .get(i + 1)
                                                .getTotalSpeakers()
                        );
                    }
                }
        );
    }

    /**
     * Tests that the language population report handles
     * a database SQLException safely and returns an empty result.
     */
    @Test
    void testLanguagePopulationReportHandlesSQLException()
            throws Exception {

        App temporaryApp =
                new App();

        temporaryApp.connect(
                "localhost:3306",
                1000
        );

        Connection closedConnection =
                temporaryApp.getConnection();

        assertNotNull(
                closedConnection,
                "Temporary database connection should be created."
        );

        closedConnection.close();

        LanguageReport report =
                new LanguageReport(
                        closedConnection
                );

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        PrintStream originalOut =
                System.out;

        try {

            System.setOut(
                    new PrintStream(
                            outputStream,
                            true,
                            StandardCharsets.UTF_8
                    )
            );

            List<Language> languages =
                    report
                            .getLanguagePopulationReport();

            String output =
                    outputStream.toString(
                            StandardCharsets.UTF_8
                    );

            assertAll(
                    "Language SQL exception",

                    () -> assertNotNull(
                            languages
                    ),

                    () -> assertTrue(
                            languages.isEmpty()
                    ),

                    () -> assertTrue(
                            output.contains(
                                    "Failed to retrieve language population report for US32"
                            )
                    )
            );

        } finally {

            System.setOut(originalOut);
        }
    }
}