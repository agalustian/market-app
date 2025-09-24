package ru.market.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.market.dto.OrderDTO;
import ru.market.services.OrdersService;

@Controller
@RequestMapping("orders")
public class OrdersController {

  private final OrdersService ordersService;

  public OrdersController(final OrdersService ordersService) {
    this.ordersService = ordersService;
  }

  @GetMapping
  public String getOrders(final Model model) {
    List<OrderDTO> orders = ordersService.getOrders().stream().map(OrderDTO::from).toList();

    model.addAttribute("orders", orders);

    return "orders";
  }

}
