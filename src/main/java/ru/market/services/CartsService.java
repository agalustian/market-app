package ru.market.services;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.dto.CartAction;
import ru.market.models.Cart;
import ru.market.models.CartItem;
import ru.market.models.Item;
import ru.market.models.Order;
import ru.market.repositories.CartsJpaRepository;
import ru.market.repositories.ItemsJpaRepository;
import ru.market.repositories.OrdersJpaRepository;

@Service
public class CartsService {

  private final CartsJpaRepository cartsRepository;

  private final OrdersJpaRepository orderRepository;

  private final ItemsJpaRepository itemsJpaRepository;

  @Autowired
  public CartsService(CartsJpaRepository cartsRepository, OrdersJpaRepository orderRepository,
                      ItemsJpaRepository itemsJpaRepository) {
    this.cartsRepository = cartsRepository;
    this.orderRepository = orderRepository;
    this.itemsJpaRepository = itemsJpaRepository;
  }

  public Cart getCart(final Integer cartId) {
    return cartsRepository.findCartById(cartId);
  }

  @Transactional
  public Cart addRemoveToCart(final Integer cartId, Integer itemId, CartAction cartAction) {
    Cart cart = cartsRepository.getCartById(cartId);
    CartItem cartItem = getOrCreateCartItem(cart, itemId);

    switch (cartAction) {
      case PLUS -> cartItem.incrementCount();
      case MINUS -> cartItem.decrementCount();
      case DELETE -> cart.getCartItems().remove(cartItem);
    }

    cartsRepository.save(cart);

    return cart;
  }

  private CartItem getOrCreateCartItem(Cart cart, Integer itemId) {
    var cartItem = cart.getCartItems().stream()
        .filter(item -> Objects.equals(item.getItem().getId(), itemId))
        .toList();

    if (cartItem.isEmpty()) {
      Item item = itemsJpaRepository.getItemById(itemId);
      var newCartItem = new CartItem(cart.getId(), item, 0);

      cart.getCartItems().add(newCartItem);

      return newCartItem;
    }

    return cartItem.getFirst();
  }

  @Transactional
  public Order buy(final Integer cartId) {
    Cart cart = cartsRepository.getCartById(cartId);

    if (cart == null) {
      return null;
    }

    Order order = orderRepository.save(Order.from(cart));

    cart.getCartItems().removeAll(cart.getCartItems());

    cartsRepository.save(cart);

    return order;
  }

}
