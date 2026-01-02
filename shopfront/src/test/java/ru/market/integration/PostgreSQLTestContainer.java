package ru.market.integration;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public final class PostgreSQLTestContainer {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer<?> pgContainer = new PostgreSQLContainer<>("postgres:9.6.12");

}