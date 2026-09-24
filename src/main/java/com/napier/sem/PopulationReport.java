package com.napier.sem;

public class PopulationReport {
    private String name;
    private long totalPopulation;
    private long cityPopulation;
    private double cityPopulationPercent;
    private long nonCityPopulation;
    private double nonCityPopulationPercent;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public long getTotalPopulation() { return totalPopulation; }
    public void setTotalPopulation(long totalPopulation) { this.totalPopulation = totalPopulation; }

    public long getCityPopulation() { return cityPopulation; }
    public void setCityPopulation(long cityPopulation) { this.cityPopulation = cityPopulation; }

    public double getCityPopulationPercent() { return cityPopulationPercent; }
    public void setCityPopulationPercent(double cityPopulationPercent) { this.cityPopulationPercent = cityPopulationPercent; }

    public long getNonCityPopulation() { return nonCityPopulation; }
    public void setNonCityPopulation(long nonCityPopulation) { this.nonCityPopulation = nonCityPopulation; }

    public double getNonCityPopulationPercent() { return nonCityPopulationPercent; }
    public void setNonCityPopulationPercent(double nonCityPopulationPercent) { this.nonCityPopulationPercent = nonCityPopulationPercent; }
}
