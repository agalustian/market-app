package ru.market.shopfront.dto;

import ru.market.shopfront.models.OrderItem;

public record OrderItemDTO(Integer id, String title, Integer price, Integer count) {

  public static OrderItemDTO from(final OrderItem orderItem) {
    return new OrderItemDTO(orderItem.getId(), orderItem.getTitle(), orderItem.getPrice(), orderItem.getCount());
  }

}
