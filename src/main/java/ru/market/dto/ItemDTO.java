package ru.market.dto;

import ru.market.models.Item;

public record ItemDTO(Integer id, Long price, String title, String description, Integer count, String imgPath) {

  public static ItemDTO from(Item item, Integer count, String imgPath) {
    return new ItemDTO(item.getId(), item.getPrice(), item.getTitle(), item.getDescription(), count, imgPath);
  }
}
