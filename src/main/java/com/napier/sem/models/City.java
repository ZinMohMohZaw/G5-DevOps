package com.napier.sem.models;

/**
 * Represents a city and its associated country, district and population.
 */
public class City {
    private String name;
    private String country;
    private String district;
    private int population;

    /**
     * Creates a City object.
     *
     * @param name       name of the city
     * @param country    name of the country
     * @param district   district in which the city is located
     * @param population population of the city
     */
    public City(String name, String country, String district, int population) {
        this.name = name;
        this.country = country;
        this.district = district;
        this.population = population;
    }

    /**
     * Gets the name of the city.
     *
     * @return city name
     */
    public String getName() { return name; }

    /**
     * Gets the country associated with the city.
     *
     * @return country name
     */
    public String getCountry() { return country; }

    /**
     * Gets the district in which the city is located.
     *
     * @return district name
     */
    public String getDistrict() { return district; }

    /**
     * Gets the population of the city.
     *
     * @return city population
     */
    public int getPopulation() { return population; }
}