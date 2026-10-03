package com.napier.sem.models;

public class Language {
        private String name;
        private long totalSpeakers;
        private double percentageOfWorld;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public long getTotalSpeakers() { return totalSpeakers; }
        public void setTotalSpeakers(long totalSpeakers) { this.totalSpeakers = totalSpeakers; }

        public double getPercentageOfWorld() { return percentageOfWorld; }
        public void setPercentageOfWorld(double percentageOfWorld) { this.percentageOfWorld = percentageOfWorld; }
    }
