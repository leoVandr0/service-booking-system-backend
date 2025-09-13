package com.servicebookingsystem;

import com.servicebookingsystem.config.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "DATABASE_URL=postgresql://testuser:testpass@localhost:5432/testdb"
})
class DatabaseConfigTest {

    @Autowired(required = false)
    private DataSource dataSource;

    @Autowired
    private Environment environment;

    @Test
    void testDatabaseConfigCreatesDataSourceWhenDatabaseUrlPresent() {
        // Test that DatabaseConfig creates a DataSource when DATABASE_URL is present
        DatabaseConfig config = new DatabaseConfig(environment);
        
        // This should not throw an exception during bean creation
        assertNotNull(config);
        
        // If DATABASE_URL is set, dataSource should be created
        String databaseUrl = environment.getProperty("DATABASE_URL");
        if (databaseUrl != null && databaseUrl.startsWith("postgresql://")) {
            assertNotNull(dataSource, "DataSource should be created when DATABASE_URL is present");
        }
    }

    @Test
    void testContextLoadsWithDatabaseConfig() {
        // This test ensures the Spring context loads successfully with our DatabaseConfig
        // If there are any issues with the configuration, this test will fail
        assertTrue(true, "Spring context loaded successfully");
    }
}
