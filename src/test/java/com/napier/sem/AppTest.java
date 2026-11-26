package com.napier.sem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the App class.
 * Verifies the initial connection state and database
 * disconnection behaviour under normal and error conditions.
 */
class AppTest {

    private App app;

    /**
     * Creates a new App instance before each test.
     */
    @BeforeEach
    void setUp() {
        app = new App();
    }

    /**
     * Tests that a newly created App instance
     * initially has no active database connection.
     */
    @Test
    void testInitialConnectionIsNull() {

        assertNull(
                app.getConnection()
        );

        System.out.println(
                "Initial database connection is null as expected."
        );
    }

    /**
     * Tests that disconnecting when no database connection
     * exists does not throw an exception.
     */
    @Test
    void testDisconnectWithNullConnection() {

        assertAll(
                "Grouped assertions for testDisconnectWithNullConnection",

                () -> assertDoesNotThrow(
                        () -> app.disconnect()
                ),

                () -> assertNull(
                        app.getConnection()
                )
        );

        System.out.println(
                "Disconnect with no active connection handled successfully."
        );
    }

    /**
     * Tests that an exception raised while closing the database
     * connection is caught and reported without terminating the application.
     */
    @Test
    void testDisconnectHandlesException() throws Exception {

        /*
         * Creates a mock Connection using a dynamic proxy.
         * The proxy throws an SQLException whenever close()
         * is called to simulate a database disconnection failure.
         */
        Connection failingConnection =
                (Connection) Proxy.newProxyInstance(
                        Connection.class.getClassLoader(),
                        new Class[]{Connection.class},
                        (proxy, method, args) -> {

                            if ("close".equals(method.getName())) {
                                throw new SQLException(
                                        "Simulated close failure"
                                );
                            }

                            Class<?> returnType =
                                    method.getReturnType();

                            if (returnType.equals(boolean.class)) {
                                return false;
                            }

                            if (returnType.equals(byte.class)) {
                                return (byte) 0;
                            }

                            if (returnType.equals(short.class)) {
                                return (short) 0;
                            }

                            if (returnType.equals(int.class)) {
                                return 0;
                            }

                            if (returnType.equals(long.class)) {
                                return 0L;
                            }

                            if (returnType.equals(float.class)) {
                                return 0.0f;
                            }

                            if (returnType.equals(double.class)) {
                                return 0.0;
                            }

                            if (returnType.equals(char.class)) {
                                return '\0';
                            }

                            return null;
                        }
                );

        /*
         * Uses reflection to inject the failing Connection
         * into the private App connection field.
         */
        Field connectionField =
                App.class.getDeclaredField("con");

        connectionField.setAccessible(true);

        connectionField.set(
                app,
                failingConnection
        );

        /*
         * Captures standard error output so the test can verify
         * that the expected disconnection error message is produced.
         */
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

            assertAll(
                    "Grouped assertions for testDisconnectHandlesException",

                    () -> assertDoesNotThrow(
                            () -> app.disconnect()
                    ),

                    () -> assertTrue(
                            errorStream
                                    .toString(StandardCharsets.UTF_8)
                                    .contains(
                                            "Error closing database connection"
                                    )
                    )
            );

        } finally {

            System.setErr(originalErr);
        }

        System.out.println(
                "Database disconnect exception handled successfully."
        );
    }
}