package com.napier.sem.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the Language model class.
 */
public class LanguageTest {

    private Language language;

    /**
     * Creates a Language object before each test.
     */
    @BeforeEach
    void setUp() {
        language = new Language(
                "English",
                347077867L,
                5.71
        );
    }

    /**
     * Tests that the language name is stored and returned correctly.
     */
    @Test
    void testGetName() {
        assertEquals("English", language.getName());
    }

    /**
     * Tests that the total number of speakers is stored and returned correctly.
     */
    @Test
    void testGetTotalSpeakers() {
        assertEquals(347077867L, language.getTotalSpeakers());
    }

    /**
     * Tests that the world population percentage is stored and returned correctly.
     */
    @Test
    void testGetPercentageOfWorld() {
        assertEquals(
                5.71,
                language.getPercentageOfWorld(),
                0.001
        );
    }
}