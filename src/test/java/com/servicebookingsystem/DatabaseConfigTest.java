package com.servicebookingsystem;

import com.servicebookingsystem.config.DatabaseConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DatabaseConfigTest {

    @Autowired(required = false)
    private DataSource dataSource;

    @Autowired
    private Environment environment;

    @Test
    void testDatabaseConfigWithTestProfile() {
        // Test that the application context loads successfully with test profile
        // This uses H2 database instead of Railway PostgreSQL
        assertNotNull(dataSource, "DataSource should be created for test profile");
    }

    @Test
    void testContextLoadsWithDatabaseConfig() {
        // This test ensures the Spring context loads successfully with our DatabaseConfig
        // When using test profile, it should use H2 database configuration
        assertTrue(true, "Spring context loaded successfully with test profile");
    }
}
