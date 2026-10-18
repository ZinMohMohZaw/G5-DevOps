package com.napier.sem.models;

/**
 * Represents a capital city and its associated country and population.
 */
public class CapitalCity {
    private String name;
    private String country;
    private int population;

    /**
     * Creates a CapitalCity object.
     *
     * @param name       name of the capital city
     * @param country    name of the country
     * @param population population of the capital city
     */
    public CapitalCity(String name, String country, int population) {
        this.name = name;
        this.country = country;
        this.population = population;
    }

    /**
     * Gets the name of the capital city.
     *
     * @return capital city name
     */
    public String getName() { return name; }

    /**
     * Gets the country associated with the capital city.
     *
     * @return country name
     */
    public String getCountry() { return country; }

    /**
     * Gets the population of the capital city.
     *
     * @return capital city population
     */
    public int getPopulation() { return population; }
}