package ru.market.dto;

import ru.market.models.CartItem;
import ru.market.models.Item;

public record ItemDTO(Integer id, Integer price, String title, String description, String imgPath, Integer count) {

  public static ItemDTO from(Item item) {
    return new ItemDTO(item.getId(), item.getPrice(), item.getTitle(), item.getDescription(), item.getImgPath(), item.getCount() );
  }

  public static ItemDTO from(CartItem cartItem) {
    return ItemDTO.from(cartItem.getItem());
  }

  public static ItemDTO empty() {
    return new ItemDTO(-1, null, "", "", "", 0);
  }

}
