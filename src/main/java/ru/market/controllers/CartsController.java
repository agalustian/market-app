package ru.market.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.market.dto.CartAction;
import ru.market.dto.CartItemsDTO;
import ru.market.dto.ItemDTO;
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

  @GetMapping
  public String getCartItems(final Model model) {
    model.addAttribute("items", getCartItemsDTO(CART_ID));

    return "cart";
  }

  @PostMapping("/items")
  public String addRemoveToCart(@RequestParam("id") Integer id, @RequestParam("action") CartAction action,
                                Model model) {
    cartsService.addRemoveToCart(CART_ID, id, action);

    model.addAttribute("items", getCartItemsDTO(CART_ID));

    return "cart";
  }

  private CartItemsDTO getCartItemsDTO(final Integer cartId) {
    var cart = cartsService.getCart(cartId);
    List<ItemDTO> cartItems = cart.getCartItems().stream()
        .map(cartItem -> ItemDTO.from(cartItem.getItem(), cartItem.getCount(), "img"))
        .toList();

    Integer totalPrice =
        cartItems.stream().map(cartItem -> cartItem.price() * cartItem.count()).reduce(Integer::sum).get();

    return new CartItemsDTO(cartItems, totalPrice);
  }

}
