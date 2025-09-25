package ru.market.integration.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.market.models.Item;
import ru.market.repositories.ItemsJpaRepository;

@DataJpaTest
class ItemsJpaRepositoryTests {

  @Autowired
  private ItemsJpaRepository itemsJpaRepository;

  @BeforeEach
  void prepare() {
    itemsJpaRepository.deleteAll();

    var item = new Item(1, "Test", 100, "description", "test-path");

    itemsJpaRepository.save(item);
  }

  @Test
  void shouldFindByTitleIgnoringCase() {
    var items = itemsJpaRepository.findItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("test", "1",
        PageRequest.of(0, 5));

    assertEquals(1, items.size());
  }

  @Test
  void shouldFindByDescriptionIgnoringCase() {
    var items =
        itemsJpaRepository.findItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("1", "descr",
            PageRequest.of(0, 5));

    assertEquals(1, items.size());
  }

  @Test
  void shouldCountByTitleIgnoringCase() {
    var count = itemsJpaRepository.countItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("test", "1");

    assertEquals(1, count);
  }

  @Test
  void shouldCountByDescriptionIgnoringCase() {
    var count = itemsJpaRepository.countItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("1", "descr");

    assertEquals(1, count);
  }

}
