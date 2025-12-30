package ru.market.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
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
  public Mono<Rendering> getOrders() {
    return Mono.just(
        Rendering.view(ORDERS_VIEW)
            .modelAttribute("orders", ordersService.getOrders())
            .build()
    );
  }

  @GetMapping("/{id}")
  public Mono<Rendering> getOrder(@PathVariable("id") Integer orderId,
                                  @RequestParam(value = "newOrder", required = false) Boolean newOrder) {
    return ordersService.getOrder(orderId)
        .map(order ->
            Rendering.view("order")
                .modelAttribute("order", order)
                .modelAttribute("newOrder", newOrder)
                .build()

        )
        .switchIfEmpty(Mono.just(Rendering.redirectTo(ORDERS_VIEW).build()));
  }

}
