package com.example.security;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

@Profile("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class IntegrationTestBase {

    @Autowired
    protected ObjectMapper mapper;

    @BeforeAll
    static void setUp() {
        var postgresContainer = new PostgreSQLContainer<>("postgres:15")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpassword")
                .withExposedPorts(5432);

        postgresContainer.start();
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
    }
}
