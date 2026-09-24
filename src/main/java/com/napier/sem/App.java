package com.napier.sem;

public class App {
    public static void main(String[] args) {
        System.out.println("DevOps World Population System Initialized (Amazon Corretto 25 Runtime).");

        // Simple validation check for entities
        Country sample = new Country();
        sample.setCode("GBR");
        sample.setName("United Kingdom");
        sample.setPopulation(67081000);

        System.out.println("Loaded Sample Entity: " + sample.getName() + " [" + sample.getCode() + "]");
    }
}
