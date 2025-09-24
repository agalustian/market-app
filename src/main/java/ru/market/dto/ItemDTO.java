package ru.market.dto;

import ru.market.models.CartItem;
import ru.market.models.Item;

public record ItemDTO(Integer id, Integer price, String title, String description, Integer count, String imgPath) {

  public static ItemDTO from(Item item, Integer count, String imgPath) {
    return new ItemDTO(item.getId(), item.getPrice(), item.getTitle(), item.getDescription(), count, imgPath);
  }

  public static ItemDTO from(CartItem cartItem) {
    return ItemDTO.from(cartItem.getItem(), cartItem.getCount(), "img");
  }

}
