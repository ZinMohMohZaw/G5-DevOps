package com.napier.sem;

import com.napier.sem.models.*;
import com.napier.sem.reports.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App {

    private Connection con = null;

    public Connection getConnection() {
        return con;
    }

    public void connect(String location, int delay) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        int retries = 10;
        for (int i = 0; i < retries; ++i) {
            System.out.println("Connecting to database...");
            try {
                Thread.sleep(delay);
                con = DriverManager.getConnection("jdbc:mysql://" + location + "/world?useSSL=false", "root", "example");
                System.out.println("Successfully connected");
                break;
            } catch (SQLException sqle) {
                System.out.println("Failed to connect to database attempt " + i);
            } catch (InterruptedException ie) {
                System.out.println("Thread interrupted during connection delay.");
            }
        }
    }

    public void disconnect() {
        if (con != null) {
            try {
                con.close();
                System.out.println("Database connection closed.");
            } catch (Exception e) {
                System.out.println("Error closing database connection");
            }
        }
    }

    public static void main(String[] args) {
        App app = new App();
        app.connect(args.length < 1 ? "localhost:33060" : args[0], 3000);

        System.out.println("DevOps World Population System Initialized.");

        app.disconnect();

    }
}