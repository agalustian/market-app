package ru.market.dto;

import java.util.List;
import ru.market.models.Order;
import ru.market.models.OrderItem;

public record OrderDTO(Integer id, List<OrderItemDTO> items, Integer totalSum) {

  public static OrderDTO from(Order order, List<OrderItem> orderItems) {
    List<OrderItemDTO> orderItemsDTO = orderItems.stream().map(OrderItemDTO::from).toList();

    return new OrderDTO(order.getId(), orderItemsDTO, order.getTotalSum());
  }

}
