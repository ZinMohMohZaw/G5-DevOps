package com.napier.sem.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Population model class.
 */
class PopulationTest {

    /**
     * Tests the primary Population constructor.
     * Verifies that the name and total population are initialized
     * correctly while the urban and non-urban values use their
     * expected default values.
     */
    @Test
    void testPrimaryConstructor() {

        Population pop =
                new Population(
                        "Thailand",
                        71000000
                );

        assertAll(
                "Primary Population constructor",

                () -> assertEquals(
                        "Thailand",
                        pop.getName()
                ),

                () -> assertEquals(
                        71000000,
                        pop.getTotalPopulation()
                ),

                () -> assertEquals(
                        0,
                        pop.getUrbanPopulation()
                ),

                () -> assertEquals(
                        0.0,
                        pop.getUrbanPercentage()
                ),

                () -> assertEquals(
                        0,
                        pop.getNonUrbanPopulation()
                ),

                () -> assertEquals(
                        0.0,
                        pop.getNonUrbanPercentage()
                )
        );
    }

    /**
     * Tests the full Population constructor.
     * Verifies that the total, urban and non-urban population
     * values and their percentages are initialized correctly.
     */
    @Test
    void testFullConstructor() {

        Population pop =
                new Population(
                        "Asia",
                        4000000000L,
                        1500000000L,
                        37.5,
                        2500000000L,
                        62.5
                );

        assertAll(
                "Full Population constructor",

                () -> assertEquals(
                        "Asia",
                        pop.getName()
                ),

                () -> assertEquals(
                        4000000000L,
                        pop.getTotalPopulation()
                ),

                () -> assertEquals(
                        1500000000L,
                        pop.getUrbanPopulation()
                ),

                () -> assertEquals(
                        37.5,
                        pop.getUrbanPercentage(),
                        0.001
                ),

                () -> assertEquals(
                        2500000000L,
                        pop.getNonUrbanPopulation()
                ),

                () -> assertEquals(
                        62.5,
                        pop.getNonUrbanPercentage(),
                        0.001
                )
        );
    }
}