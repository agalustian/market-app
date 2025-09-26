package ru.market.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.market.dto.CartAction;
import ru.market.models.Cart;
import ru.market.models.CartItem;
import ru.market.models.Item;
import ru.market.models.Order;
import ru.market.repositories.CartsJpaRepository;
import ru.market.repositories.ItemsJpaRepository;
import ru.market.repositories.OrdersJpaRepository;
import ru.market.services.CartsService;

class CartsServiceTests {

  private static final Integer CART_ID = 111;

  private static final Integer ITEM_ID = 222;

  private CartsJpaRepository cartsJpaRepository = Mockito.mock(CartsJpaRepository.class);

  private OrdersJpaRepository ordersJpaRepository = Mockito.mock(OrdersJpaRepository.class);

  private ItemsJpaRepository itemsJpaRepository = Mockito.mock(ItemsJpaRepository.class);

  private final CartsService cartsService =
      new CartsService(cartsJpaRepository, ordersJpaRepository, itemsJpaRepository);

  @Test
  void shouldAddItemToCart() {
    Mockito.when(cartsJpaRepository.getCartById(CART_ID)).thenReturn(new Cart(CART_ID, new ArrayList<>()));

    Cart cart = cartsService.addRemoveToCart(CART_ID, ITEM_ID, CartAction.PLUS);

    assertEquals(CART_ID, cart.getId());
    assertEquals(CART_ID, cart.getCartItems().getFirst().getCartId());
    assertEquals(1, cart.getCartItems().getFirst().getCount());
  }

  @Test
  void shouldDecrementItemCountToCart() {
    var item = new Item(ITEM_ID, "title", 100, "descr", "img");

    var count = 5;
    Mockito.when(cartsJpaRepository.getCartById(CART_ID))
        .thenReturn(new Cart(CART_ID, List.of(new CartItem(CART_ID, item, count))));

    Cart cart = cartsService.addRemoveToCart(CART_ID, ITEM_ID, CartAction.MINUS);

    assertEquals(4, cart.getCartItems().getFirst().getCount());
  }

  @Test
  void shouldRemoveItemToCart() {
    var item = new Item(ITEM_ID, "title", 100, "descr", "img");

    var count = 5;
    Mockito.when(cartsJpaRepository.getCartById(CART_ID))
        .thenReturn(new Cart(CART_ID, new ArrayList<>(List.of(new CartItem(CART_ID, item, count)))));

    Cart cart = cartsService.addRemoveToCart(CART_ID, ITEM_ID, CartAction.DELETE);

    assertTrue(cart.getCartItems().isEmpty());
  }

  @Test
  void shouldBuyCart() {
    var item = new Item(ITEM_ID, "title", 100, "descr", "img");
    var cart = new Cart(CART_ID, new ArrayList<>(List.of(new CartItem(CART_ID, item, 5))));

    Mockito.when(cartsJpaRepository.getCartById(CART_ID)).thenReturn(cart);
    Mockito.when(ordersJpaRepository.save(Order.from(cart))).thenReturn(Order.from(cart));

    Order order = cartsService.buy(CART_ID);

    var removedCart = cartsJpaRepository.findCartById(CART_ID);

    assertEquals(500, order.getTotalSum());
    assertNull(removedCart);
  }
}
