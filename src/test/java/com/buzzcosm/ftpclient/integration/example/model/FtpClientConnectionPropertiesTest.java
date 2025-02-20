package com.buzzcosm.ftpclient.integration.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the FtpClientConnectionProperties class.
 *
 * This class provides test cases to verify the correctness of the FtpClientConnectionProperties class,
 * which is responsible for storing and managing FTP connection properties.
 */
class FtpClientConnectionPropertiesTest {

    private FtpClientConnectionProperties ftpClientConnectionProperties;
    private final String host = "localhost";
    private final int port = 21;
    private final String username = "username";
    private final String password = "password";

    @BeforeEach
    void setUp() {
        ftpClientConnectionProperties = new FtpClientConnectionProperties();
    }

    /**
     * Tests the getHost() method to ensure it returns the correct host value.
     */
    @DisplayName("Test getHost() method")
    @Test
    void testCorrectSetServerProperty() {
        // Arrange
        ftpClientConnectionProperties.setHost(host);
        String expectedResult = host;
        // Act
        String actualResult = ftpClientConnectionProperties.getHost();
        // Assert
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Tests the getPort() method to ensure it returns the correct port value.
     */
    @DisplayName("Test getPort() method")
    @Test
    void testCorrectSetPortProperty() {
        // Arrange
        ftpClientConnectionProperties.setPort(port);
        int expectedResult = port;
        // Act
        int actualResult = ftpClientConnectionProperties.getPort();
        // Assert
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Tests the getUsername() method to ensure it returns the correct username value.
     */
    @DisplayName("Test getUsername() method")
    @Test
    void testCorrectSetUsernameProperty() {
        // Arrange
        ftpClientConnectionProperties.setUsername(username);
        String expectedResult = username;
        // Act
        String actualResult = ftpClientConnectionProperties.getUsername();
        // Assert
        assertEquals(expectedResult, actualResult);
    }

    /**
     * Tests the getPassword() method to ensure it returns the correct password value.
     */
    @DisplayName("Test getPassword() method")
    @Test
    void testCorrectSetPasswordProperty() {
        // Arrange
        ftpClientConnectionProperties.setPassword(password);
        String expectedResult = password;
        // Act
        String actualResult = ftpClientConnectionProperties.getPassword();
        // Assert
        assertEquals(expectedResult, actualResult);
    }
}