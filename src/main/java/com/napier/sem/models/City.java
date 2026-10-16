package com.napier.sem.models;

public class City {
    private String name;
    private String country;
    private String district;
    private int population;

    public City(String name, String country, String district, int population) {
        this.name = name;
        this.country = country;
        this.district = district;
        this.population = population;
    }

    public String getName() { return name; }

    public String getCountry() { return country; }

    public String getDistrict() { return district; }

    public int getPopulation() { return population; }
}