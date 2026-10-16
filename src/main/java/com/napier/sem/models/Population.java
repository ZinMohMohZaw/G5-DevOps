package com.napier.sem.models;

public class Population {
    private String name;
    private long totalPopulation;
    private long urbanPopulation;
    private double urbanPercentage;
    private long nonUrbanPopulation;
    private double nonUrbanPercentage;

    public Population(String name, long totalPopulation) {
        this(name, totalPopulation, 0, 0.0, 0, 0.0);
    }

    public Population(String name, long totalPopulation, long urbanPopulation, double urbanPercentage,
                      long nonUrbanPopulation, double nonUrbanPercentage) {
        this.name = name;
        this.totalPopulation = totalPopulation;
        this.urbanPopulation = urbanPopulation;
        this.urbanPercentage = urbanPercentage;
        this.nonUrbanPopulation = nonUrbanPopulation;
        this.nonUrbanPercentage = nonUrbanPercentage;
    }

    public String getName() { return name; }

    public long getTotalPopulation() { return totalPopulation; }

    public long getUrbanPopulation() { return urbanPopulation; }

    public double getUrbanPercentage() { return urbanPercentage; }

    public long getNonUrbanPopulation() { return nonUrbanPopulation; }

    public double getNonUrbanPercentage() { return nonUrbanPercentage; }
}
