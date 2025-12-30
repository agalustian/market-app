package ru.market.dto;

import java.util.List;
import ru.market.models.Order;

public record OrderDTO(Integer id, List<OrderItemDTO> items, Integer totalSum) {

  public static OrderDTO from(Order order) {
    List<OrderItemDTO> orderItemsDTO = order.getOrderItems().stream().map(OrderItemDTO::from).toList();

    return new OrderDTO(order.getId(), orderItemsDTO, order.getTotalSum());
  }

}
