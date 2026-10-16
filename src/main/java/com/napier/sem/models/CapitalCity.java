package com.napier.sem.models;

public class CapitalCity {
    private String name;
    private String country;
    private int population;

    public CapitalCity(String name, String country, int population) {
        this.name = name;
        this.country = country;
        this.population = population;
    }

    public String getName() { return name; }

    public String getCountry() { return country; }

    public int getPopulation() { return population; }
}