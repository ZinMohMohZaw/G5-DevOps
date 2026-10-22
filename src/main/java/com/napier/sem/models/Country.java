package com.napier.sem.models;

/**
 * Represents a country and its geographical and population information.
 */
public class Country {
    private String code;
    private String name;
    private String continent;
    private String region;
    private int population;
    private String capital;

    /**
     * Instantiates a Country object.
     *
     * @param code       country code
     * @param name       country name
     * @param continent  continent in which the country is located
     * @param region     region in which the country is located
     * @param population population of the country
     * @param capital    name of the country's capital city
     */
    public Country(String code, String name, String continent, String region, int population, String capital) {
        this.code = code;
        this.name = name;
        this.continent = continent;
        this.region = region;
        this.population = population;
        this.capital = capital;
    }

    /**
     * Gets the country code.
     *
     * @return country code
     */
    public String getCode() { return code; }

    /**
     * Gets the country name.
     *
     * @return country name
     */
    public String getName() { return name; }

    /**
     * Gets the continent in which the country is located.
     *
     * @return continent name
     */
    public String getContinent() { return continent; }

    /**
     * Gets the region in which the country is located.
     *
     * @return region name
     */
    public String getRegion() { return region; }

    /**
     * Gets the population of the country.
     *
     * @return country population
     */
    public int getPopulation() { return population; }

    /**
     * Gets the name of the country's capital city.
     *
     * @return capital city name
     */
    public String getCapital() { return capital; }
}