package ru.market.shopfront.dto;

import ru.market.shopfront.models.CartItem;
import ru.market.shopfront.models.Item;

public record ItemDTO(Integer id, Integer price, String title, String description, String imgPath, Integer count) {

  public static ItemDTO from(Item item) {
    return new ItemDTO(item.getId(), item.getPrice(), item.getTitle(), item.getDescription(), item.getImgPath(),
        item.getCount());
  }

  public static ItemDTO from(CartItem cartItem) {
    return new ItemDTO(cartItem.getItemId(), cartItem.getPrice(), cartItem.getTitle(), cartItem.getDescription(),
        cartItem.getImgPath(), cartItem.getCount());
  }

  public static ItemDTO empty() {
    return new ItemDTO(-1, null, "", "", "", 0);
  }

}
