package ru.springtest.service;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
public abstract class AbstractIntegrationTest {

    @ServiceConnection
    protected static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14");

    @ServiceConnection(name = "redis")
    protected static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:8.2.3"))
            .withExposedPorts(6379);
}