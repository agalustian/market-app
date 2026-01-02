package ru.market.shopfront.controllers;

import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;
import ru.market.shopfront.dto.AddRemoveToCartRequest;
import ru.market.shopfront.services.CartsService;
import ru.market.shopfront.services.PaymentService;

@Controller
@RequestMapping("/cart")
public class CartsController {

  // TODO for test
  private static final Integer CART_ID = 999;

  private final CartsService cartsService;

  private final PaymentService paymentService;

  CartsController(final CartsService cartsService, final PaymentService paymentService) {
    this.cartsService = cartsService;
    this.paymentService = paymentService;
  }

  @GetMapping("/items")
  public Mono<Rendering> getCartItems() {
    return getCartItemsView(CART_ID);
  }

  @PostMapping("/buy")
  public Mono<Rendering> buy() {
    return cartsService.buy(CART_ID)
        .map(
            orderDTO ->
                Rendering.redirectTo("/orders/" + orderDTO.id() + "?newOrder=true").build()
        )
        .switchIfEmpty(Mono.just(Rendering.redirectTo("/orders").build()));
  }

  @PostMapping(value = "/items", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public Mono<Rendering> addRemoveToCart(@ModelAttribute AddRemoveToCartRequest request) {
    return cartsService.addRemoveToCart(CART_ID, request.itemId(), request.action())
        .then(getCartItemsView(CART_ID));
  }

  private Mono<Rendering> getCartItemsView(final Integer cartId) {
    return cartsService.getCart(cartId)
        .collectList()
        .map(cartItems ->
            paymentService.getBalance(UUID.randomUUID())
                .map(balance -> Rendering.view("cart")
                    .modelAttribute("items", cartItems)
                    .modelAttribute("total", cartItems.stream().map(cartItem -> cartItem.price() * cartItem.count())
                        .reduce(0, Integer::sum))
                    .modelAttribute("balance", balance)
                    .build()));
  }

}
