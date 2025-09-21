package com.servicebookingsystem.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile("!test")
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private final Environment environment;

    public DatabaseConfig(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = environment.getProperty("DATABASE_URL");
        
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            logger.info("Using DATABASE_URL for database configuration");
            return createDataSourceFromUrl(databaseUrl);
        }
        
        // Fallback to individual environment variables (Railway alternative)
        String host = environment.getProperty("PGHOST");
        String port = environment.getProperty("PGPORT", "5432");
        String database = environment.getProperty("PGDATABASE");
        String username = environment.getProperty("PGUSER");
        String password = environment.getProperty("PGPASSWORD");
        
        if (host != null && database != null && username != null && password != null) {
            logger.info("Using individual PostgreSQL environment variables for database configuration");
            String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
            
            return DataSourceBuilder.create()
                    .url(jdbcUrl)
                    .username(username)
                    .password(password)
                    .driverClassName("org.postgresql.Driver")
                    .build();
        }
        
        logger.error("No database configuration found. Expected DATABASE_URL or individual PG* environment variables");
        throw new RuntimeException("Database configuration not found. Please set DATABASE_URL or individual PostgreSQL environment variables (PGHOST, PGDATABASE, PGUSER, PGPASSWORD)");
    }
    
    private DataSource createDataSourceFromUrl(String databaseUrl) {
        if (databaseUrl.startsWith("postgresql://")) {
            try {
                URI dbUri = new URI(databaseUrl);
                String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath();
                
                String[] userInfo = dbUri.getUserInfo().split(":");
                String username = userInfo[0];
                String password = userInfo.length > 1 ? userInfo[1] : "";

                logger.info("Connecting to PostgreSQL database at: {}:{}{}", dbUri.getHost(), dbUri.getPort(), dbUri.getPath());

                return DataSourceBuilder.create()
                        .url(jdbcUrl)
                        .username(username)
                        .password(password)
                        .driverClassName("org.postgresql.Driver")
                        .build();
            } catch (URISyntaxException e) {
                logger.error("Invalid DATABASE_URL format: {}", databaseUrl, e);
                throw new RuntimeException("Invalid DATABASE_URL format: " + databaseUrl, e);
            } catch (Exception e) {
                logger.error("Failed to parse DATABASE_URL: {}", databaseUrl, e);
                throw new RuntimeException("Failed to parse DATABASE_URL", e);
            }
        }
        
        logger.error("DATABASE_URL is not in expected PostgreSQL format: {}", databaseUrl);
        throw new RuntimeException("DATABASE_URL is not in expected PostgreSQL format: " + databaseUrl);
    }
}
