package com.napier.sem.models;

public class Population {
    private String name;
    private long totalPopulation;
    private long urbanPopulation;
    private double urbanPercentage;
    private long nonUrbanPopulation;
    private double nonUrbanPercentage;

    public Population() {
    }

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
    public void setName(String name) { this.name = name; }

    public long getTotalPopulation() { return totalPopulation; }
    public void setTotalPopulation(long totalPopulation) { this.totalPopulation = totalPopulation; }

    public long getUrbanPopulation() { return urbanPopulation; }
    public void setUrbanPopulation(long urbanPopulation) { this.urbanPopulation = urbanPopulation; }

    public double getUrbanPercentage() { return urbanPercentage; }
    public void setUrbanPercentage(double urbanPercentage) { this.urbanPercentage = urbanPercentage; }

    public long getNonUrbanPopulation() { return nonUrbanPopulation; }
    public void setNonUrbanPopulation(long nonUrbanPopulation) { this.nonUrbanPopulation = nonUrbanPopulation; }

    public double getNonUrbanPercentage() { return nonUrbanPercentage; }
    public void setNonUrbanPercentage(double nonUrbanPercentage) { this.nonUrbanPercentage = nonUrbanPercentage; }
}
