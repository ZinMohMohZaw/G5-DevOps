package com.napier.sem.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CapitalCityTest {

    @Test
    void testCapitalCityConstructorAndGetters() {

        CapitalCity capital =
                new CapitalCity(
                        "Naypyidaw",
                        "Myanmar",
                        924608
                );

        assertAll(
                "CapitalCity constructor and getters",

                () -> assertEquals(
                        "Naypyidaw",
                        capital.getName()
                ),

                () -> assertEquals(
                        "Myanmar",
                        capital.getCountry()
                ),

                () -> assertEquals(
                        924608,
                        capital.getPopulation()
                )
        );

        System.out.println(
                "CapitalCity constructor and getters Passed"
        );
    }
}