package ru.market.shopfront.unit.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.market.shopfront.models.Order;
import ru.market.shopfront.models.CartItem;

class OrderTests {

  @Test
  void shouldCalculateTotalSum() {
    var cartItem = new CartItem(999 + 1, "test-user", 1, 10, "title", 101, "t", "");
    var cartItem2 = new CartItem(999 + 2, "test-user", 2, 5, "title2", 202, "t", "a");

    assertEquals(2020, Order.from("tet-user", List.of(cartItem, cartItem2)).getTotalSum());
  }

}
