package com.napier.sem.models;

public class Language {
        private String name;
        private long totalSpeakers;
        private double percentageOfWorld;

        public Language(String name, long totalSpeakers, double percentageOfWorld) {
            this.name = name;
            this.totalSpeakers = totalSpeakers;
            this.percentageOfWorld = percentageOfWorld;
        }

        public String getName() { return name; }

        public long getTotalSpeakers() { return totalSpeakers; }

        public double getPercentageOfWorld() { return percentageOfWorld; }
    }
