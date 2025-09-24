package ru.market.controllers;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.market.dto.CartItemsDTO;
import ru.market.dto.ItemDTO;
import ru.market.services.CartsService;

@Controller
@RequestMapping("/cart")
public class CartsController {

  private final CartsService cartsService;

  CartsController(final CartsService cartsService) {
    this.cartsService = cartsService;
  }

  @GetMapping
  public String getCartItems(final Model model) {
    var cart = cartsService.getCart();
    List<ItemDTO> cartItems = cart.getCartItems().stream()
        .map(cartItem -> ItemDTO.from(cartItem.getItem(), cartItem.getCount(), "img"))
        .toList();

    Integer totalPrice =
        cartItems.stream().map(cartItem -> cartItem.price() * cartItem.count()).reduce(Integer::sum).get();

    model.addAttribute("items", new CartItemsDTO(cartItems, totalPrice));

    return "cart";
  }

}
