package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the App class using the real world database.
 */
public class AppIntegrationTest {

    /**
     * Tests that App can establish a real database connection
     * and close it successfully.
     */
    @Test
    void testConnectAndDisconnect() throws Exception {

        App app = new App();

        app.connect(
                "localhost:3306",
                1000
        );

        Connection connection =
                app.getConnection();

        assertNotNull(
                connection,
                "Database connection should be created."
        );

        assertFalse(
                connection.isClosed(),
                "Database connection should be open."
        );

        System.out.println(
                "App successfully connected to the world database."
        );

        app.disconnect();

        assertTrue(
                connection.isClosed(),
                "Database connection should be closed after disconnect()."
        );

        System.out.println(
                "App successfully disconnected from the world database."
        );
    }

    /**
     * Tests that the complete application workflow
     * executes successfully using the real world database.
     */
    @Test
    void testRunExecutesApplicationWorkflow() throws Exception {

        App app = new App();

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        PrintStream originalOut =
                System.out;

        try {

            /*
             * Capture the large amount of report output
             * produced by the application.
             */
            System.setOut(
                    new PrintStream(outputStream)
            );

            app.run(
                    new String[]{
                            "localhost:3306"
                    }
            );

        } finally {

            System.setOut(originalOut);
        }

        String output =
                outputStream.toString();

        assertTrue(
                output.contains(
                        "DevOps World Population System Initialized"
                )
        );

        assertTrue(
                output.contains(
                        "[US01] ALL COUNTRIES REPORT"
                )
        );

        assertTrue(
                output.contains(
                        "[US32] LANGUAGE POPULATION REPORT"
                )
        );

        assertTrue(
                output.contains(
                        "Database connection closed."
                )
        );

        Connection connection =
                app.getConnection();

        assertNotNull(
                connection,
                "App should have created a database connection."
        );

        assertTrue(
                connection.isClosed(),
                "Database connection should be closed after run() finishes."
        );

        System.out.println(
                "App.run() successfully executed the complete reporting workflow."
        );

        System.out.println(
                "US01 report execution confirmed."
        );

        System.out.println(
                "US32 report execution confirmed."
        );

        System.out.println(
                "Database connection closed successfully after application execution."
        );
    }

    /**
     * Tests that App handles an SQLException when
     * the database cannot be reached.
     */
    @Test
    void testConnectHandlesSQLException() {

        App app = new App();

        ByteArrayOutputStream errorStream =
                new ByteArrayOutputStream();

        PrintStream originalErr =
                System.err;

        try {

            System.setErr(
                    new PrintStream(errorStream)
            );

            /*
             * Port 1 is deliberately invalid for MySQL.
             * Delay is zero so retries complete quickly.
             */
            app.connect(
                    "localhost:1",
                    0
            );

            assertNull(
                    app.getConnection(),
                    "Connection should remain null when database connection fails."
            );

            String errorOutput =
                    errorStream.toString();

            assertTrue(
                    errorOutput.contains(
                            "Failed to connect to database attempt"
                    )
            );

        } finally {

            System.setErr(originalErr);
        }

        System.out.println(
                "SQLException during database connection handled successfully."
        );
    }

    /**
     * Tests that App handles an interrupted
     * database connection delay.
     */
    @Test
    void testConnectHandlesInterruptedException() {

        App app = new App();

        ByteArrayOutputStream errorStream =
                new ByteArrayOutputStream();

        PrintStream originalErr =
                System.err;

        try {

            System.setErr(
                    new PrintStream(errorStream)
            );

            /*
             * Interrupt the current thread before connect().
             * Thread.sleep() inside connect() will throw
             * InterruptedException.
             */
            Thread.currentThread().interrupt();

            app.connect(
                    "localhost:3306",
                    1000
            );

            assertNull(
                    app.getConnection(),
                    "Connection should remain null when the connection delay is interrupted."
            );

            assertTrue(
                    errorStream.toString()
                            .contains(
                                    "Thread interrupted during connection delay."
                            )
            );

        } finally {

            /*
             * Clear the interrupted state so that
             * other JUnit tests are unaffected.
             */
            Thread.interrupted();

            System.setErr(originalErr);
        }

        System.out.println(
                "Interrupted database connection attempt handled successfully."
        );
    }

    /**
     * Tests that run() handles a failed database
     * connection and aborts report execution safely.
     */
    @Test
    void testRunHandlesFailedDatabaseConnection() {

        App app = new App();

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        ByteArrayOutputStream errorStream =
                new ByteArrayOutputStream();

        PrintStream originalOut =
                System.out;

        PrintStream originalErr =
                System.err;

        try {

            System.setOut(
                    new PrintStream(outputStream)
            );

            System.setErr(
                    new PrintStream(errorStream)
            );

            /*
             * Force connect() to fail immediately.
             */
            Thread.currentThread().interrupt();

            app.run(
                    new String[]{
                            "localhost:3306"
                    }
            );

            assertNull(
                    app.getConnection(),
                    "Connection should remain null after failed application startup."
            );

            String output =
                    outputStream.toString();

            String errorOutput =
                    errorStream.toString();

            assertTrue(
                    output.contains(
                            "DevOps World Population System Initialized"
                    )
            );

            assertTrue(
                    errorOutput.contains(
                            "Thread interrupted during connection delay."
                    )
            );

            assertTrue(
                    errorOutput.contains(
                            "Database connection failed. Aborting report execution."
                    )
            );

            assertFalse(
                    output.contains(
                            "[US01] ALL COUNTRIES REPORT"
                    )
            );

        } finally {

            Thread.interrupted();

            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        System.out.println(
                "App.run() handled failed database connection successfully."
        );
    }

    /**
     * Tests the args == null branch in run().
     *
     * This causes run() to select the default
     * database location.
     */
    @Test
    void testRunWithNullArguments() {

        App app = new App();

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        ByteArrayOutputStream errorStream =
                new ByteArrayOutputStream();

        PrintStream originalOut =
                System.out;

        PrintStream originalErr =
                System.err;

        try {

            System.setOut(
                    new PrintStream(outputStream)
            );

            System.setErr(
                    new PrintStream(errorStream)
            );

            /*
             * Interrupt before run() so the test does not
             * spend time attempting connections to the
             * default localhost:33060 location.
             */
            Thread.currentThread().interrupt();

            app.run(null);

            assertNull(
                    app.getConnection()
            );

            String output =
                    outputStream.toString();

            String errorOutput =
                    errorStream.toString();

            assertTrue(
                    output.contains(
                            "DevOps World Population System Initialized"
                    )
            );

            assertTrue(
                    errorOutput.contains(
                            "Database connection failed. Aborting report execution."
                    )
            );

        } finally {

            Thread.interrupted();

            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        System.out.println(
                "App.run() handled null command-line arguments successfully."
        );
    }

    /**
     * Tests the args.length < 1 branch in run().
     *
     * This verifies that an empty argument array
     * selects the default database location.
     */
    @Test
    void testRunWithEmptyArguments() {

        App app = new App();

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        ByteArrayOutputStream errorStream =
                new ByteArrayOutputStream();

        PrintStream originalOut =
                System.out;

        PrintStream originalErr =
                System.err;

        try {

            System.setOut(
                    new PrintStream(outputStream)
            );

            System.setErr(
                    new PrintStream(errorStream)
            );

            /*
             * Interrupt before run() so connection retries
             * using the default location finish immediately.
             */
            Thread.currentThread().interrupt();

            app.run(
                    new String[]{}
            );

            assertNull(
                    app.getConnection()
            );

            String output =
                    outputStream.toString();

            String errorOutput =
                    errorStream.toString();

            assertTrue(
                    output.contains(
                            "DevOps World Population System Initialized"
                    )
            );

            assertTrue(
                    errorOutput.contains(
                            "Database connection failed. Aborting report execution."
                    )
            );

        } finally {

            Thread.interrupted();

            System.setOut(originalOut);
            System.setErr(originalErr);
        }

        System.out.println(
                "App.run() handled empty command-line arguments successfully."
        );
    }
}