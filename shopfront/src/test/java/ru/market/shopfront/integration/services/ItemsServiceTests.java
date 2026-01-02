package ru.market.shopfront.integration.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Profile;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.market.shopfront.integration.FixturesGenerator;
import ru.market.shopfront.integration.PostgreSQLTestContainer;
import ru.market.shopfront.integration.RedisTestContainer;
import ru.market.shopfront.models.CartItem;
import ru.market.shopfront.repositories.CartItemsRepository;
import ru.market.shopfront.repositories.ImagesRepository;
import ru.market.shopfront.repositories.ItemsRepository;
import ru.market.shopfront.services.ItemsService;

@SpringBootTest
@Testcontainers
@Profile("test")
@ImportTestcontainers({PostgreSQLTestContainer.class, RedisTestContainer.class})
public class ItemsServiceTests {

  @Autowired
  private ItemsService itemsService;

  @Autowired
  private CartItemsRepository cartItemsRepository;

  @Autowired
  private ItemsRepository itemsRepository;

  @Autowired
  private ImagesRepository imagesRepository;

  @BeforeEach
  void prepare() {
    var items = itemsRepository.saveAll(FixturesGenerator.generateItems()).collectList().block();
    cartItemsRepository.save(new CartItem(1001, items.getFirst().getId(), 10)).block();
    cartItemsRepository.save(new CartItem(1001, items.getLast().getId(), 1)).block();
  }

}
