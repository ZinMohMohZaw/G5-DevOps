package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for the App class.
 * Verifies database connectivity, complete application execution,
 * connection failure handling and command-line argument behaviour.
 */
class AppIntegrationTest {

    /**
     * Tests that the application can successfully connect
     * to the World database and disconnect afterwards.
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

        assertAll(
                "Connect and disconnect",

                () -> assertNotNull(
                        connection,
                        "Database connection should be created."
                ),

                () -> assertFalse(
                        connection.isClosed(),
                        "Database connection should be open."
                )
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
     * Tests the complete application workflow using
     * a real connection to the World database.
     * Verifies that the required reports are executed
     * and that the connection is closed after completion.
     */
    @Test
    void testRunExecutesApplicationWorkflow() throws Exception {

        App app = new App();

        ByteArrayOutputStream outputStream =
                new ByteArrayOutputStream();

        PrintStream originalOut =
                System.out;

        try {

            System.setOut(
                    new PrintStream(
                            outputStream,
                            true,
                            StandardCharsets.UTF_8
                    )
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
                outputStream.toString(
                        StandardCharsets.UTF_8
                );

        Connection connection =
                app.getConnection();

        assertAll(
                "Complete App workflow",

                () -> assertTrue(
                        output.contains(
                                "DevOps World Population System Initialized"
                        )
                ),

                () -> assertTrue(
                        output.contains(
                                "[US01] ALL COUNTRIES REPORT"
                        )
                ),

                () -> assertTrue(
                        output.contains(
                                "[US32] LANGUAGE POPULATION REPORT"
                        )
                ),

                () -> assertTrue(
                        output.contains(
                                "Database connection closed."
                        )
                ),

                () -> assertNotNull(
                        connection,
                        "App should have created a database connection."
                ),

                () -> assertTrue(
                        connection.isClosed(),
                        "Database connection should be closed after run() finishes."
                )
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
     * Tests that a database connection failure is
     * handled safely when an invalid database port is used.
     * Verifies that the connection remains null and
     * the expected error message is produced.
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
                    new PrintStream(
                            errorStream,
                            true,
                            StandardCharsets.UTF_8
                    )
            );

            app.connect(
                    "localhost:1",
                    0
            );

            String errorOutput =
                    errorStream.toString(
                            StandardCharsets.UTF_8
                    );

            assertAll(
                    "SQLException handling",

                    () -> assertNull(
                            app.getConnection(),
                            "Connection should remain null when database connection fails."
                    ),

                    () -> assertTrue(
                            errorOutput.contains(
                                    "Failed to connect to database attempt"
                            )
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
     * Tests that interruption during the database
     * connection delay is handled safely.
     * Verifies that no connection is created and
     * the interruption message is reported.
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
                    new PrintStream(
                            errorStream,
                            true,
                            StandardCharsets.UTF_8
                    )
            );

            Thread.currentThread().interrupt();

            app.connect(
                    "localhost:3306",
                    1000
            );

            String errorOutput =
                    errorStream.toString(
                            StandardCharsets.UTF_8
                    );

            assertAll(
                    "Interrupted connection handling",

                    () -> assertNull(
                            app.getConnection(),
                            "Connection should remain null when the connection delay is interrupted."
                    ),

                    () -> assertTrue(
                            errorOutput.contains(
                                    "Thread interrupted during connection delay."
                            )
                    )
            );

        } finally {

            Thread.interrupted();

            System.setErr(originalErr);
        }

        System.out.println(
                "Interrupted database connection attempt handled successfully."
        );
    }

    /**
     * Tests that the complete application workflow
     * is aborted safely when the database connection fails.
     * Verifies that report execution does not continue
     * after an unsuccessful connection attempt.
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
                    new PrintStream(
                            outputStream,
                            true,
                            StandardCharsets.UTF_8
                    )
            );

            System.setErr(
                    new PrintStream(
                            errorStream,
                            true,
                            StandardCharsets.UTF_8
                    )
            );

            Thread.currentThread().interrupt();

            app.run(
                    new String[]{
                            "localhost:3306"
                    }
            );

            String output =
                    outputStream.toString(
                            StandardCharsets.UTF_8
                    );

            String errorOutput =
                    errorStream.toString(
                            StandardCharsets.UTF_8
                    );

            assertAll(
                    "Failed App startup handling",

                    () -> assertNull(
                            app.getConnection(),
                            "Connection should remain null after failed application startup."
                    ),

                    () -> assertTrue(
                            output.contains(
                                    "DevOps World Population System Initialized"
                            )
                    ),

                    () -> assertTrue(
                            errorOutput.contains(
                                    "Thread interrupted during connection delay."
                            )
                    ),

                    () -> assertTrue(
                            errorOutput.contains(
                                    "Database connection failed. Aborting report execution."
                            )
                    ),

                    () -> assertFalse(
                            output.contains(
                                    "[US01] ALL COUNTRIES REPORT"
                            )
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
     * Tests that the application safely handles
     * null command-line arguments during startup.
     * Verifies initialization and safe termination
     * when the default connection attempt fails.
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
                    new PrintStream(
                            outputStream,
                            true,
                            StandardCharsets.UTF_8
                    )
            );

            System.setErr(
                    new PrintStream(
                            errorStream,
                            true,
                            StandardCharsets.UTF_8
                    )
            );

            Thread.currentThread().interrupt();

            app.run(null);

            String output =
                    outputStream.toString(
                            StandardCharsets.UTF_8
                    );

            String errorOutput =
                    errorStream.toString(
                            StandardCharsets.UTF_8
                    );

            assertAll(
                    "Null argument handling",

                    () -> assertNull(
                            app.getConnection()
                    ),

                    () -> assertTrue(
                            output.contains(
                                    "DevOps World Population System Initialized"
                            )
                    ),

                    () -> assertTrue(
                            errorOutput.contains(
                                    "Database connection failed. Aborting report execution."
                            )
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
     * Tests that the application safely handles
     * an empty command-line argument array during startup.
     * Verifies initialization and safe termination
     * when the default connection attempt fails.
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
                    new PrintStream(
                            outputStream,
                            true,
                            StandardCharsets.UTF_8
                    )
            );

            System.setErr(
                    new PrintStream(
                            errorStream,
                            true,
                            StandardCharsets.UTF_8
                    )
            );

            Thread.currentThread().interrupt();

            app.run(
                    new String[]{}
            );

            String output =
                    outputStream.toString(
                            StandardCharsets.UTF_8
                    );

            String errorOutput =
                    errorStream.toString(
                            StandardCharsets.UTF_8
                    );

            assertAll(
                    "Empty argument handling",

                    () -> assertNull(
                            app.getConnection()
                    ),

                    () -> assertTrue(
                            output.contains(
                                    "DevOps World Population System Initialized"
                            )
                    ),

                    () -> assertTrue(
                            errorOutput.contains(
                                    "Database connection failed. Aborting report execution."
                            )
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