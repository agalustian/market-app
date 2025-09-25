package ru.market.services;

import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.dto.CartAction;
import ru.market.models.Cart;
import ru.market.models.Order;
import ru.market.repositories.CartsJpaRepository;
import ru.market.repositories.OrdersJpaRepository;

@Service
public class CartsService {

  private final CartsJpaRepository cartsRepository;

  private final OrdersJpaRepository orderRepository;

  public CartsService(CartsJpaRepository cartsRepository, OrdersJpaRepository orderRepository) {
    this.cartsRepository = cartsRepository;
    this.orderRepository = orderRepository;
  }

  public Cart getCart(final Integer cartId) {
    return cartsRepository.findCartById(cartId);
  }

  @Transactional
  public Cart addRemoveToCart(final Integer cartId, Integer itemId, CartAction cartAction) {
    Cart cart = cartsRepository.getCartById(cartId);

    var cartItem = cart.getCartItems().stream()
        .filter(item -> Objects.equals(item.getItem().getId(), itemId))
        .toList()
        .getFirst();

    if (cartItem == null) {
      return null;
    }

    switch (cartAction) {
      case PLUS -> cartItem.incrementCount();
      case MINUS -> cartItem.decrementCount();
      case DELETE -> cart.getCartItems().remove(cartItem);
    }

    cartsRepository.save(cart);

    return cart;
  }

  @Transactional
  public Order buy(final Integer cartId) {
    Cart cart = cartsRepository.getCartById(cartId);

    if (cart == null) {
      return null;
    }

    return orderRepository.save(Order.from(cart));
  }

}
