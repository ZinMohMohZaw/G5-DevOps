package com.napier.sem.reports;

import com.napier.sem.models.Language;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LanguageReportTest {

    private LanguageReport languageReport;
    private ByteArrayOutputStream outputStream;

    private final PrintStream originalOut =
            System.out;

    @BeforeEach
    void setUp() {

        languageReport =
                new LanguageReport(null);

        outputStream =
                new ByteArrayOutputStream();

        System.setOut(
                new PrintStream(
                        outputStream,
                        true,
                        StandardCharsets.UTF_8
                )
        );
    }

    @AfterEach
    void tearDown() {

        System.setOut(originalOut);
    }

    @Test
    void testGetLanguagePopulationReportWithNullConnection() {

        List<Language> languages =
                languageReport
                        .getLanguagePopulationReport();

        String output =
                outputStream.toString(
                        StandardCharsets.UTF_8
                );

        assertAll(
                "Null language DB connection",

                () -> assertNotNull(languages),

                () -> assertTrue(
                        languages.isEmpty()
                ),

                () -> assertTrue(
                        output.contains(
                                "Database connection is null."
                        )
                )
        );
    }

    @Test
    void testPrintLanguagePopulationReportWithNullList() {

        assertDoesNotThrow(
                () -> languageReport
                        .printLanguagePopulationReport(
                                null
                        )
        );

        String output =
                outputStream.toString(
                        StandardCharsets.UTF_8
                );

        assertTrue(
                output.contains(
                        "No language population data available."
                )
        );
    }

    @Test
    void testPrintLanguagePopulationReportWithEmptyList() {

        List<Language> languages =
                new ArrayList<>();

        assertDoesNotThrow(
                () -> languageReport
                        .printLanguagePopulationReport(
                                languages
                        )
        );

        String output =
                outputStream.toString(
                        StandardCharsets.UTF_8
                );

        assertTrue(
                output.contains(
                        "No language population data available."
                )
        );
    }

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
                .printLanguagePopulationReport(
                        languages
                );

        String output =
                outputStream.toString(
                        StandardCharsets.UTF_8
                );

        assertAll(
                "Valid language report output",

                () -> assertTrue(
                        output.contains(
                                "LANGUAGE POPULATION REPORT"
                        )
                ),

                () -> assertTrue(
                        output.contains("English")
                ),

                () -> assertTrue(
                        output.contains("Spanish")
                ),

                () -> assertTrue(
                        output.contains(
                                "347,077,867"
                        )
                ),

                () -> assertTrue(
                        output.contains(
                                "355,029,462"
                        )
                ),

                () -> assertTrue(
                        output.contains("5.71%")
                ),

                () -> assertTrue(
                        output.contains("5.84%")
                )
        );

        originalOut.println(output);
    }

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
                        .printLanguagePopulationReport(
                                languages
                        )
        );

        String output =
                outputStream.toString(
                        StandardCharsets.UTF_8
                );

        assertAll(
                "Null language entry",

                () -> assertTrue(
                        output.contains("English")
                ),

                () -> assertTrue(
                        output.contains(
                                "347,077,867"
                        )
                )
        );
    }
}