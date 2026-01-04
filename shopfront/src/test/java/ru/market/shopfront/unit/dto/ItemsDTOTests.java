package ru.market.shopfront.unit.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.market.shopfront.dto.ItemDTO;
import ru.market.shopfront.dto.ItemsDTO;
import ru.market.shopfront.models.Item;

class ItemsDTOTests {

  @ParameterizedTest
  @ValueSource(ints = {2, 3})
  void shouldSplitItemsToSubListsByChunkSize(Integer chunkSize) {
    List<ItemDTO> items = List.of(
        ItemDTO.from(new Item(1, "Test", 100, "description", "test-path")),
        ItemDTO.from(new Item(2, "Test", 100, "description", "test-path")),
        ItemDTO.from(new Item(3, "Test", 100, "description", "test-path")),
        ItemDTO.from(new Item(4, "Test", 100, "description", "test-path")),
        ItemDTO.from(new Item(5, "Test", 100, "description", "test-path")),
        ItemDTO.from(new Item(6, "Test", 100, "description", "test-path"))
    );

    var dto = ItemsDTO.from(items, chunkSize);

    assertEquals(items.size() / chunkSize, dto.items().size());
  }

  @Test
  void shouldFillNotFullSubLists() {
    List<ItemDTO> items = List.of(
        ItemDTO.from(new Item(1, "Test", 100, "description", "test-path"))
    );

    var dto = ItemsDTO.from(items, 5);

    assertEquals(1, dto.items().size());
    assertEquals(5, dto.items().getFirst().size());
  }

}
