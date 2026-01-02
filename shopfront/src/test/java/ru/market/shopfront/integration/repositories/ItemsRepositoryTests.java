package ru.market.shopfront.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.market.shopfront.integration.PostgreSQLTestContainer;
import ru.market.shopfront.integration.RedisTestContainer;
import ru.market.shopfront.models.Item;
import ru.market.shopfront.repositories.ItemsRepository;

@SpringBootTest
@Testcontainers
@Profile("test")
@ImportTestcontainers({PostgreSQLTestContainer.class, RedisTestContainer.class})
class ItemsRepositoryTests {

  @Autowired
  CacheManager cacheManager;

  @Autowired
  private ItemsRepository itemsRepository;

  @Test
  void shouldFindById() {
    var generatedItem = new Item("Test", 100, "description", "test-path");
    Item item = itemsRepository.save(generatedItem).block();

    var foundItem = itemsRepository.findById(item.getId()).block();

    assertEquals(generatedItem.getTitle(), foundItem.getTitle());
  }

  @Test
  void shouldFindByIdInCache() {
    var generatedItem = new Item("Test", 100, "description", "test-path");
    Item item = itemsRepository.save(generatedItem).block();

    itemsRepository.findById(item.getId()).block();

    var cachedItem = cacheManager.getCache("item").get(item.getId(), Item.class);

    assertNotNull(cachedItem);
    assertEquals(cachedItem.getId(), item.getId());
    assertEquals(cachedItem.getTitle(), item.getTitle());
    assertEquals(cachedItem.getDescription(), item.getDescription());
    assertEquals(cachedItem.getPrice(), item.getPrice());
    assertEquals(cachedItem.getImgPath(), item.getImgPath());
  }

  @ParameterizedTest
  @ValueSource(strings = {"description", "dEscription", "Test", "tEst"})
  void shouldSearchByDescription(String searchBy) {
    var generatedItem = new Item("Test", 100, "description", "test-path");
    itemsRepository.save(generatedItem).block();

    var foundItem =
        itemsRepository.findItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchBy, searchBy,
            PageRequest.of(0, 1).withSort(Sort.Direction.ASC, "id")).blockFirst();

    cacheManager.getCache("items").invalidate();

    assertNotNull(foundItem);
    assertEquals(foundItem.getTitle(), generatedItem.getTitle());
  }

}
