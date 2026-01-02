package ru.market.shopfront.integration.services;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.market.shopfront.integration.PostgreSQLTestContainer;

@SpringBootTest
@Testcontainers
@ImportTestcontainers(PostgreSQLTestContainer.class)
@Profile("test")
public class OrdersService {

  @Container
  @ServiceConnection
  static final PostgreSQLContainer<?> pgContainer = new PostgreSQLContainer<>("postgres:9.6.12");
}
