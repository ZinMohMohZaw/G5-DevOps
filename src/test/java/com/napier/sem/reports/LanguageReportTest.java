package com.napier.sem.reports;

import com.napier.sem.models.Language;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the LanguageReport class.
 */
public class LanguageReportTest {

    private LanguageReport languageReport;

    private ByteArrayOutputStream outputStream;

    private final PrintStream originalOut =
            System.out;

    /**
     * Creates a LanguageReport without a database connection
     * and redirects console output before each test.
     */
    @BeforeEach
    void setUp() {

        languageReport =
                new LanguageReport(null);

        outputStream =
                new ByteArrayOutputStream();

        System.setOut(
                new PrintStream(outputStream)
        );
    }

    /**
     * Restores normal console output after each test.
     */
    @AfterEach
    void tearDown() {

        System.setOut(originalOut);
    }

    /**
     * Tests that requesting the language population report
     * without a database connection returns an empty list.
     */
    @Test
    void testGetLanguagePopulationReportWithNullConnection() {

        List<Language> languages =
                languageReport.getLanguagePopulationReport();

        assertNotNull(languages);

        assertTrue(
                languages.isEmpty()
        );

        assertTrue(
                outputStream.toString()
                        .contains(
                                "Database connection is null."
                        )
        );

        originalOut.println(
                "Null database connection handled successfully."
        );

        originalOut.println(
                "Returned language records: "
                        + languages.size()
        );
    }

    /**
     * Tests that a null language list is handled safely.
     */
    @Test
    void testPrintLanguagePopulationReportWithNullList() {

        assertDoesNotThrow(
                () -> languageReport
                        .printLanguagePopulationReport(null)
        );

        assertTrue(
                outputStream.toString()
                        .contains(
                                "No language population data available."
                        )
        );

        originalOut.println(
                "Null language list handled successfully."
        );
    }

    /**
     * Tests that an empty language list is handled safely.
     */
    @Test
    void testPrintLanguagePopulationReportWithEmptyList() {

        List<Language> languages =
                new ArrayList<>();

        assertDoesNotThrow(
                () -> languageReport
                        .printLanguagePopulationReport(languages)
        );

        assertTrue(
                outputStream.toString()
                        .contains(
                                "No language population data available."
                        )
        );

        originalOut.println(
                "Empty language list handled successfully."
        );
    }

    /**
     * Tests that valid language data is printed correctly.
     */
    @Test
    void testPrintLanguagePopulationReportWithValidData() {

        List<Language> languages =
                new ArrayList<>();

        languages.add(
                new Language(
                        "English",
                        347077867L,
                        5.71
                )
        );

        languages.add(
                new Language(
                        "Spanish",
                        355029462L,
                        5.84
                )
        );

        languageReport
                .printLanguagePopulationReport(languages);

        String output =
                outputStream.toString();

        assertTrue(
                output.contains(
                        "LANGUAGE POPULATION REPORT"
                )
        );

        assertTrue(
                output.contains("English")
        );

        assertTrue(
                output.contains("Spanish")
        );

        assertTrue(
                output.contains("347,077,867")
        );

        assertTrue(
                output.contains("355,029,462")
        );

        assertTrue(
                output.contains("5.71%")
        );

        assertTrue(
                output.contains("5.84%")
        );

        originalOut.println();
        originalOut.println(
                "LanguageReport unit test output:"
        );

        originalOut.println(output);
    }

    /**
     * Tests that null entries inside the language list
     * are ignored without causing an exception.
     */
    @Test
    void testPrintLanguagePopulationReportWithNullEntry() {

        List<Language> languages =
                new ArrayList<>();

        languages.add(
                new Language(
                        "English",
                        347077867L,
                        5.71
                )
        );

        languages.add(null);

        assertDoesNotThrow(
                () -> languageReport
                        .printLanguagePopulationReport(languages)
        );

        String output =
                outputStream.toString();

        assertTrue(
                output.contains("English")
        );

        assertTrue(
                output.contains("347,077,867")
        );

        originalOut.println(
                "Null entry inside language list skipped successfully."
        );
    }
}