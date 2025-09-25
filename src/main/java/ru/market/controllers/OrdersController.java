package ru.market.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.market.dto.OrderDTO;
import ru.market.models.Order;
import ru.market.services.OrdersService;

@Controller
@RequestMapping("/orders")
public class OrdersController {

  private static String ORDERS_VIEW = "orders";

  private final OrdersService ordersService;

  public OrdersController(final OrdersService ordersService) {
    this.ordersService = ordersService;
  }

  @GetMapping
  public String getOrders(final Model model) {
    List<OrderDTO> orders = ordersService.getOrders().stream().map(OrderDTO::from).toList();

    model.addAttribute("orders", orders);

    return ORDERS_VIEW;
  }

  @GetMapping("/{id}")
  public String getOrder(@PathVariable("id") Integer orderId,
                         @RequestParam(value = "newOrder", required = false) Boolean newOrder,
                         Model model) {
    Order order = ordersService.getOrder(orderId);

    if (order == null) {
      return "redirect:/" + ORDERS_VIEW;
    }

    model.addAttribute("order", OrderDTO.from(order));
    model.addAttribute("newOrder", newOrder);

    return "order";
  }

}
