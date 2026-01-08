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
import ru.market.shopfront.exceptions.PaymentException;
import ru.market.shopfront.payment.domain.PaymentResult;
import ru.market.shopfront.services.CartsService;
import ru.market.shopfront.services.PaymentService;
import ru.market.shopfront.utils.SecurityUtils;

@Controller
@RequestMapping("/cart")
public class CartsController extends BaseController {

  private static final String CART_VIEW = "cart";

  private final CartsService cartsService;

  private final PaymentService paymentService;

  CartsController(final CartsService cartsService, final PaymentService paymentService) {
    this.cartsService = cartsService;
    this.paymentService = paymentService;
  }

  @GetMapping("/items")
  public Mono<Rendering> getCartItems() {
    return executeWithUsername(this::getCartItemsView);
  }

  @PostMapping("/buy")
  public Mono<Rendering> buy() {
    return executeWithUsername(username ->
        cartsService.buy(username)
            .flatMap(
                orderDTO -> {
                  // This is not optimal decision, not transaction fail safe!
                  return paymentService.chargePayment(UUID.randomUUID(), UUID.randomUUID(), orderDTO.totalSum())
                      .flatMap(paymentResult -> {
                            if (paymentResult.getStatus() != PaymentResult.StatusEnum.SUCCESS) {
                              // TODO use multilanguage
                              return Mono.error(
                                  new PaymentException("Ошибка оплаты, статус: " + paymentResult.getStatus()));
                            }

                            return Mono.just(Rendering.redirectTo("/orders/" + orderDTO.id() + "?newOrder=true").build());
                          }
                      )
                      .switchIfEmpty(Mono.just(Rendering.redirectTo("/orders").build()))
                      .onErrorResume(err -> getCartItemsView(username, err.getMessage()));
                }));
  }

  @PostMapping(value = "/items", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public Mono<Rendering> addRemoveToCart(@ModelAttribute AddRemoveToCartRequest request) {
    return executeWithUsername(username ->
        cartsService.addRemoveToCart(username, request.itemId(), request.action())
            .then(getCartItemsView(username))
    );
  }

  private Mono<Rendering> getCartItemsView(final String userId) {
    return getCartItemsView(userId, "");
  }

  private Mono<Rendering> getCartItemsView(final String userId, String errorMessage) {
    return cartsService.getCart(userId)
        .collectList()
        .flatMap(cartItems ->
            paymentService.getBalance(UUID.randomUUID())
                .map(balance -> Rendering.view(CART_VIEW)
                    .modelAttribute("items", cartItems)
                    .modelAttribute("total", cartItems.stream().map(cartItem -> cartItem.price() * cartItem.count())
                        .reduce(0, Integer::sum))
                    .modelAttribute("balance", balance)
                    .modelAttribute("errorMessage", errorMessage)
                    .build()));
  }

}
