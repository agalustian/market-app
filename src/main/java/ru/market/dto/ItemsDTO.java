package ru.market.dto;

import java.util.List;
import ru.market.models.Item;

public record ItemsDTO(List<List<Item>> items, String search, ItemsSort sort, Paging paging) {

  public static ItemsDTO from(List<Item> items, String search, ItemsSort sort, Paging paging) {
    return new ItemsDTO(items, search, sort, paging);
  }

}
