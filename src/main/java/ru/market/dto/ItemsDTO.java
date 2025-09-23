package ru.market.dto;

import java.util.ArrayList;
import java.util.List;
import ru.market.models.Item;

public record ItemsDTO(List<List<Item>> items, String search, ItemsSort sort, Paging paging) {

  public static ItemsDTO from(List<Item> items, Integer chunkSize, String search, ItemsSort sort, Paging paging) {
    List<List<Item>> lists = new ArrayList<>();

    for (var i = 0; i < items.size(); i += chunkSize) {
      lists.add(items.subList(i, i + chunkSize));
    }

    return new ItemsDTO(lists, search, sort, paging);
  }

}
