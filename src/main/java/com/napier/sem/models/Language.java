package com.napier.sem.models;

/**
 * Represents language population statistics, including the total number
 * of speakers and their percentage of the world population.
 */
public class Language {
    private String name;
    private long totalSpeakers;
    private double percentageOfWorld;

    /**
     * Instantiates a Language object containing language population statistics.
     *
     * @param name              name of the language
     * @param totalSpeakers     total number of people who speak the language
     * @param percentageOfWorld percentage of the world population who speak the language
     */
    public Language(String name, long totalSpeakers, double percentageOfWorld) {
        this.name = name;
        this.totalSpeakers = totalSpeakers;
        this.percentageOfWorld = percentageOfWorld;
    }

    /**
     * Gets the name of the language.
     *
     * @return language name
     */
    public String getName() { return name; }

    /**
     * Gets the total number of speakers of the language.
     *
     * @return total number of speakers
     */
    public long getTotalSpeakers() { return totalSpeakers; }

    /**
     * Gets the percentage of the world population who speak the language.
     *
     * @return percentage of the world population
     */
    public double getPercentageOfWorld() { return percentageOfWorld; }
}