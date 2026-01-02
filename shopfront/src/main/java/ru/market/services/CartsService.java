package ru.market.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.market.dto.CartAction;
import ru.market.dto.ItemDTO;
import ru.market.dto.OrderDTO;
import ru.market.models.CartItem;
import ru.market.models.Order;
import ru.market.models.OrderItem;
import ru.market.repositories.CartItemsRepository;
import ru.market.repositories.OrderItemsRepository;
import ru.market.repositories.OrdersRepository;

@Service
public class CartsService {

  private final CartItemsRepository cartItemsRepository;

  private final OrdersRepository orderRepository;

  private final OrderItemsRepository orderItemsRepository;

  @Autowired
  public CartsService(CartItemsRepository cartItemsRepository, OrdersRepository orderRepository,
                      OrderItemsRepository orderItemsRepository) {
    this.cartItemsRepository = cartItemsRepository;
    this.orderRepository = orderRepository;
    this.orderItemsRepository = orderItemsRepository;
  }

  public Flux<ItemDTO> getCart(final Integer cartId) {
    return cartItemsRepository.findCartItems(cartId).map(ItemDTO::from);
  }

  public Mono<Void> addRemoveToCart(final Integer cartId, Integer itemId, CartAction cartAction) {
    return getOrCreateCartItem(cartId, itemId).flatMap(cartItem -> switch (cartAction) {
      case PLUS -> cartItemsRepository.incrementCountById(cartItem.getId());
      case MINUS -> cartItemsRepository.decrementCountById(cartItem.getId());
      case DELETE -> cartItemsRepository.deleteById(cartItem.getId());
    });
  }

  private Mono<CartItem> getOrCreateCartItem(Integer cartId, Integer itemId) {
    return cartItemsRepository.findByCartIdAndItemId(cartId, itemId)
        .switchIfEmpty(cartItemsRepository.save(new CartItem(cartId, itemId, 0)));
  }

  @Transactional
  public Mono<OrderDTO> buy(final Integer cartId) {
    return cartItemsRepository.findCartItems(cartId).collectList()
        .flatMap(cartItems ->
            orderRepository.save(Order.from(cartItems))
                .flatMap(order -> {
                      var orderItems = cartItems.stream().map(
                          cartItem -> OrderItem.from(order.getId(), cartItem, cartItem.getCount())).toList();

                      return orderItemsRepository.saveAll(orderItems)
                          .then(cartItemsRepository.deleteAllByCartId(cartId))
                          .then(Mono.just(OrderDTO.from(order, orderItems)));
                    }
                )
        );
  }

}
