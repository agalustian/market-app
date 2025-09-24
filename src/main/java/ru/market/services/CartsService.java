package ru.market.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.market.dto.CartAction;
import ru.market.models.Cart;
import ru.market.models.Order;
import ru.market.repositories.CartItemsJpaRepository;
import ru.market.repositories.CartsJpaRepository;
import ru.market.repositories.OrdersJpaRepository;

@Service
public class CartsService {

  private final CartsJpaRepository cartsRepository;
  private final CartItemsJpaRepository cartItemsRepository;
  private final OrdersJpaRepository orderRepository;

  public CartsService(CartsJpaRepository cartsRepository, CartItemsJpaRepository cartItemsRepository,
                      OrdersJpaRepository orderRepository) {
    this.cartsRepository = cartsRepository;
    this.cartItemsRepository = cartItemsRepository;
    this.orderRepository = orderRepository;
  }

  public Cart getCart(final Integer cartId) {
    // TODO fix hardcode, its for test, replace it when users will be added
    return cartsRepository.getCartById(cartId);
  }

  public Cart addRemoveToCart(final Integer cartId, Integer itemId, CartAction cartAction) {
    return switch (cartAction) {
      case PLUS -> cartItemsRepository.incrementCount(cartId, itemId);
      case MINUS -> cartItemsRepository.decrementCount(cartId, itemId);
      case DELETE -> cartItemsRepository.deleteCartItemByCartIdAndItem_Id(cartId, itemId);
    };
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
