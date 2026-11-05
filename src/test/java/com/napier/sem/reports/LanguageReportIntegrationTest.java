package com.napier.sem.reports;

import com.napier.sem.App;
import com.napier.sem.models.Language;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for LanguageReport using the world database.
 */
public class LanguageReportIntegrationTest {

    private static App app;
    private static LanguageReport languageReport;

    /**
     * Connects to the world database before the
     * integration tests are executed.
     */
    @BeforeAll
    static void setUp() {

        app = new App();

        app.connect(
                "localhost:3306",
                1000
        );

        assertNotNull(
                app.getConnection(),
                "Database connection should be available for integration testing."
        );

        System.out.println(
                "Integration test database connection established successfully."
        );

        languageReport =
                new LanguageReport(app.getConnection());
    }

    /**
     * Disconnects from the database after all
     * integration tests have completed.
     */
    @AfterAll
    static void tearDown() {

        if (app != null) {

            app.disconnect();

            System.out.println(
                    "Integration test database connection closed."
            );
        }
    }

    /**
     * Tests that US32 returns the five required languages
     * and displays the retrieved database report.
     */
    @Test
    void testLanguagePopulationReportReturnsFiveLanguages() {

        List<Language> languages =
                languageReport.getLanguagePopulationReport();

        assertNotNull(languages);

        assertEquals(
                5,
                languages.size()
        );

        System.out.println();
        System.out.println(
                "US32 integration test database result:"
        );

        languageReport
                .printLanguagePopulationReport(languages);
    }

    /**
     * Tests that the required languages are returned
     * in the expected population order.
     */
    @Test
    void testLanguagePopulationReportOrdering() {

        List<Language> languages =
                languageReport.getLanguagePopulationReport();

        assertEquals(
                5,
                languages.size()
        );

        assertEquals(
                "Chinese",
                languages.get(0).getName()
        );

        assertEquals(
                "Hindi",
                languages.get(1).getName()
        );

        assertEquals(
                "Spanish",
                languages.get(2).getName()
        );

        assertEquals(
                "English",
                languages.get(3).getName()
        );

        assertEquals(
                "Arabic",
                languages.get(4).getName()
        );
    }

    /**
     * Tests the calculated total number of speakers
     * for each required language.
     */
    @Test
    void testLanguagePopulationSpeakerTotals() {

        List<Language> languages =
                languageReport.getLanguagePopulationReport();

        assertEquals(
                5,
                languages.size()
        );

        assertEquals(
                1191843539L,
                languages.get(0).getTotalSpeakers()
        );

        assertEquals(
                405633070L,
                languages.get(1).getTotalSpeakers()
        );

        assertEquals(
                355029462L,
                languages.get(2).getTotalSpeakers()
        );

        assertEquals(
                347077867L,
                languages.get(3).getTotalSpeakers()
        );

        assertEquals(
                233839239L,
                languages.get(4).getTotalSpeakers()
        );
    }

    /**
     * Tests the calculated world population percentages
     * for each required language.
     */
    @Test
    void testLanguagePopulationPercentages() {

        List<Language> languages =
                languageReport.getLanguagePopulationReport();

        assertEquals(
                5,
                languages.size()
        );

        assertEquals(
                19.61,
                languages.get(0).getPercentageOfWorld(),
                0.01
        );

        assertEquals(
                6.67,
                languages.get(1).getPercentageOfWorld(),
                0.01
        );

        assertEquals(
                5.84,
                languages.get(2).getPercentageOfWorld(),
                0.01
        );

        assertEquals(
                5.71,
                languages.get(3).getPercentageOfWorld(),
                0.01
        );

        assertEquals(
                3.85,
                languages.get(4).getPercentageOfWorld(),
                0.01
        );
    }

    /**
     * Tests that the returned languages are sorted
     * by total speakers from largest to smallest.
     */
    @Test
    void testLanguagePopulationIsSortedDescending() {

        List<Language> languages =
                languageReport.getLanguagePopulationReport();

        assertNotNull(languages);
        assertFalse(languages.isEmpty());

        for (int i = 0; i < languages.size() - 1; i++) {

            assertTrue(
                    languages.get(i).getTotalSpeakers()
                            >=
                            languages.get(i + 1).getTotalSpeakers()
            );
        }
    }

    /**
     * Tests that LanguageReport handles an SQLException
     * safely and returns an empty list.
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
                new LanguageReport(closedConnection);

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        PrintStream originalOut =
                System.out;

        try {

            System.setOut(
                    new PrintStream(outputStream)
            );

            List<Language> languages =
                    report.getLanguagePopulationReport();

            assertNotNull(languages);
            assertTrue(languages.isEmpty());

            assertTrue(
                    outputStream.toString()
                            .contains(
                                    "Failed to retrieve language population report for US32"
                            )
            );

        } finally {

            System.setOut(originalOut);
        }

        System.out.println(
                "SQLException handling tested successfully using a closed database connection."
        );
    }
}