package com.napier.sem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the App class.
 */
public class AppTest {

    private App app;

    /**
     * Creates a new App instance before each test.
     */
    @BeforeEach
    void setUp() {
        app = new App();
    }

    /**
     * Tests that a newly created App initially
     * has no database connection.
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
     * Tests that disconnecting when no database
     * connection exists does not throw an exception.
     */
    @Test
    void testDisconnectWithNullConnection() {

        assertDoesNotThrow(
                () -> app.disconnect()
        );

        assertNull(
                app.getConnection()
        );

        System.out.println(
                "Disconnect with no active connection handled successfully."
        );
    }

    /**
     * Tests that App handles an exception thrown
     * while closing the database connection.
     */
    @Test
    void testDisconnectHandlesException() throws Exception {

        /*
         * Create a fake Connection.
         * Its close() method deliberately throws
         * SQLException so that App.disconnect()
         * enters its catch block.
         */
        Connection failingConnection =
                (Connection) Proxy.newProxyInstance(
                        Connection.class.getClassLoader(),
                        new Class[]{Connection.class},
                        (proxy, method, args) -> {

                            /*
                             * Simulate a failure when
                             * App attempts to close
                             * the connection.
                             */
                            if (method.getName().equals("close")) {

                                throw new SQLException(
                                        "Simulated close failure"
                                );
                            }

                            /*
                             * Return safe default values
                             * for primitive return types.
                             */
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
         * The database connection field in App is private.
         * Reflection is used only inside this test
         * to inject the simulated failing connection.
         */
        Field connectionField =
                App.class.getDeclaredField("con");

        connectionField.setAccessible(true);

        connectionField.set(
                app,
                failingConnection
        );

        ByteArrayOutputStream errorStream =
                new ByteArrayOutputStream();

        PrintStream originalErr =
                System.err;

        try {

            /*
             * Capture System.err so that we can verify
             * the error message produced by disconnect().
             */
            System.setErr(
                    new PrintStream(errorStream)
            );

            /*
             * disconnect() should catch the exception
             * instead of allowing it to escape.
             */
            assertDoesNotThrow(
                    () -> app.disconnect()
            );

            String errorOutput =
                    errorStream.toString();

            assertTrue(
                    errorOutput.contains(
                            "Error closing database connection"
                    )
            );

        } finally {

            /*
             * Restore normal error output after the test.
             */
            System.setErr(originalErr);
        }

        System.out.println(
                "Database disconnect exception handled successfully."
        );
    }
}