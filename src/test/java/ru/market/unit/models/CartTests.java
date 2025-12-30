package ru.market.unit.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.market.models.Cart;
import ru.market.models.CartItem;
import ru.market.models.Item;

class CartTests {

  @Test
  void shouldCalculateTotalSum() {
    var item = new Item(1, "Test", 100, "description", "test-path");
    var item2 = new Item(2, "another-test", 100, "description", "test-path");
    var cartItem = new CartItem(999 + 1, 999, item, 10);
    var cartItem2 = new CartItem(999 + 2, 999, item2, 5);

    Cart cart = new Cart(999, List.of(cartItem, cartItem2));

    assertEquals(1500, cart.getTotalSum());
  }

}
