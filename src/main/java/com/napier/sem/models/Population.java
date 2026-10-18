package com.napier.sem.models;

/**
 * Represents population statistics for a geographical area, including
 * total, urban and non-urban population information.
 */
public class Population {
    private String name;
    private long totalPopulation;
    private long urbanPopulation;
    private double urbanPercentage;
    private long nonUrbanPopulation;
    private double nonUrbanPercentage;

    /**
     * Creates a Population object containing the name and total population.
     * Urban and non-urban population values are initialised to zero.
     *
     * @param name            name of the geographical area
     * @param totalPopulation total population of the area
     */
    public Population(String name, long totalPopulation) {
        this(name, totalPopulation, 0, 0.0, 0, 0.0);
    }

    /**
     * Creates a Population object containing complete population statistics.
     *
     * @param name                 name of the geographical area
     * @param totalPopulation      total population of the area
     * @param urbanPopulation      population living in cities
     * @param urbanPercentage      percentage of the population living in cities
     * @param nonUrbanPopulation   population not living in cities
     * @param nonUrbanPercentage   percentage of the population not living in cities
     */
    public Population(String name, long totalPopulation, long urbanPopulation, double urbanPercentage,
                      long nonUrbanPopulation, double nonUrbanPercentage) {
        this.name = name;
        this.totalPopulation = totalPopulation;
        this.urbanPopulation = urbanPopulation;
        this.urbanPercentage = urbanPercentage;
        this.nonUrbanPopulation = nonUrbanPopulation;
        this.nonUrbanPercentage = nonUrbanPercentage;
    }

    /**
     * Gets the name of the geographical area.
     *
     * @return geographical area name
     */
    public String getName() { return name; }

    /**
     * Gets the total population of the geographical area.
     *
     * @return total population
     */
    public long getTotalPopulation() { return totalPopulation; }

    /**
     * Gets the population living in cities.
     *
     * @return urban population
     */
    public long getUrbanPopulation() { return urbanPopulation; }

    /**
     * Gets the percentage of the population living in cities.
     *
     * @return urban population percentage
     */
    public double getUrbanPercentage() { return urbanPercentage; }

    /**
     * Gets the population not living in cities.
     *
     * @return non-urban population
     */
    public long getNonUrbanPopulation() { return nonUrbanPopulation; }

    /**
     * Gets the percentage of the population not living in cities.
     *
     * @return non-urban population percentage
     */
    public double getNonUrbanPercentage() { return nonUrbanPercentage; }
}