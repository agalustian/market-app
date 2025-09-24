package ru.market.services;

import org.springframework.stereotype.Service;
import ru.market.dto.CartAction;
import ru.market.models.Cart;
import ru.market.repositories.CartItemsJpaRepository;
import ru.market.repositories.CartsJpaRepository;

@Service
public class CartsService {

  private final CartsJpaRepository cartsRepository;
  private final CartItemsJpaRepository cartItemsRepository;

  public CartsService(CartsJpaRepository cartsRepository, CartItemsJpaRepository cartItemsRepository) {
    this.cartsRepository = cartsRepository;
    this.cartItemsRepository = cartItemsRepository;
  }

  public Cart getCart(final Integer cartId) {
    // TODO fix hardcode, its for test, replace it when users will be added
    return cartsRepository.getCartById(cartId);
  }

  public boolean addRemoveToCart(final Integer cartId, Integer itemId, CartAction cartAction) {
    return switch (cartAction) {
      case PLUS -> cartItemsRepository.incrementCount(cartId, itemId);
      case MINUS -> cartItemsRepository.decrementCount(cartId, itemId);
      case DELETE -> cartItemsRepository.deleteCartItemByCartIdAndItem_Id(cartId, itemId);
    };
  }

}
