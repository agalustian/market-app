package ru.market.shopfront.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.market.shopfront.dto.CartAction;
import ru.market.shopfront.dto.ItemDTO;
import ru.market.shopfront.dto.OrderDTO;
import ru.market.shopfront.models.CartItem;
import ru.market.shopfront.models.Order;
import ru.market.shopfront.models.OrderItem;
import ru.market.shopfront.repositories.CartItemsRepository;
import ru.market.shopfront.repositories.OrderItemsRepository;
import ru.market.shopfront.repositories.OrdersRepository;

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
        .flatMap(cartItems -> {
          var order = Order.from(cartItems);

          if (cartItems.isEmpty()) {
            return Mono.empty();
          }

          return orderRepository.save(order)
              .flatMap(savedOrder -> {
                var orderItems = cartItems.stream().map(
                    cartItem -> OrderItem.from(savedOrder.getId(), cartItem, cartItem.getCount())).toList();

                return orderItemsRepository.saveAll(orderItems)
                    .then(cartItemsRepository.deleteAllByCartId(cartId))
                    .then(Mono.just(OrderDTO.from(savedOrder, orderItems)));
              });
        });
  }

}
