package com.napier.sem.models;

public class City {
    private String name;
    private String country;
    private String district;
    private int population;

    public City() {
    }

    public City(String name, String country, String district, int population) {
        this.name = name;
        this.country = country;
        this.district = district;
        this.population = population;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public int getPopulation() { return population; }
    public void setPopulation(int population) { this.population = population; }
}