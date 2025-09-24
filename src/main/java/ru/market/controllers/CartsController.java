package ru.market.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.market.dto.CartAction;
import ru.market.dto.ItemDTO;
import ru.market.models.Cart;
import ru.market.models.Order;
import ru.market.services.CartsService;

@Controller
@RequestMapping("/cart")
public class CartsController {

  // TODO for test
  private static final Integer CART_ID = 1;

  private final CartsService cartsService;

  CartsController(final CartsService cartsService) {
    this.cartsService = cartsService;
  }

  @GetMapping("/items")
  public String getCartItems(final Model model) {
    Cart cart = cartsService.getCart(CART_ID);

    return getCartItemsView(model, cart);
  }

  @PostMapping("/buy")
  public String buy() {
    Order order = cartsService.buy(CART_ID);

    if (order == null) {
      return "redirect:/orders";
    }

    return "redirect:/orders/" + order.getId() + "?newOrder=true";
  }

  @PostMapping("/items")
  public String addRemoveToCart(@RequestParam("id") Integer id, @RequestParam("action") CartAction action,
                                Model model) {
    Cart cart = cartsService.addRemoveToCart(CART_ID, id, action);

    return getCartItemsView(model, cart);
  }

  private String getCartItemsView(final Model model, Cart cart) {
    model.addAttribute("items", cart.getCartItems().stream().map(ItemDTO::from).toList());
    model.addAttribute("total", cart.getTotalSum());

    return "cart";
  }

}
