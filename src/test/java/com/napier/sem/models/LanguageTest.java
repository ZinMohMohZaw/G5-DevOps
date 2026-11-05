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
     * Creates a sample Language object before each test.
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
     * Tests that the language name is returned correctly.
     */
    @Test
    void testGetName() {

        assertEquals(
                "English",
                language.getName()
        );

        System.out.println(
                "Language name tested: "
                        + language.getName()
        );
    }

    /**
     * Tests that the total number of speakers
     * is returned correctly.
     */
    @Test
    void testGetTotalSpeakers() {

        assertEquals(
                347077867L,
                language.getTotalSpeakers()
        );

        System.out.println(
                "Total speakers tested: "
                        + language.getTotalSpeakers()
        );
    }

    /**
     * Tests that the percentage of the world population
     * is returned correctly.
     */
    @Test
    void testGetPercentageOfWorld() {

        assertEquals(
                5.71,
                language.getPercentageOfWorld(),
                0.001
        );

        System.out.println(
                "World population percentage tested: "
                        + language.getPercentageOfWorld()
                        + "%"
        );
    }
}