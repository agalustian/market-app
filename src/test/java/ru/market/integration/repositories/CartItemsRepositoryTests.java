package ru.market.integration.repositories;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import ru.market.models.Cart;
import ru.market.models.CartItem;
import ru.market.models.Item;
import ru.market.repositories.CartItemsJpaRepository;
import ru.market.repositories.CartsJpaRepository;
import ru.market.repositories.ItemsJpaRepository;

@ActiveProfiles("test")
@DataJpaTest
class CartItemsRepositoryTests {

  @Autowired
  private CartItemsJpaRepository cartItemsJpaRepository;

  @Autowired
  private ItemsJpaRepository itemsJpaRepository;

  @Autowired
  private CartsJpaRepository cartsJpaRepository;

  @BeforeEach
  void prepare() {
    cartItemsJpaRepository.deleteAll();
    itemsJpaRepository.deleteAll();
    cartsJpaRepository.deleteAll();
  }

  @Test
  void shouldCountCartItems() {
    generateCartWithItems(999);

    var cartItemsCount = cartItemsJpaRepository.countCartItems(999, List.of(1, 2));

    Assertions.assertEquals(2, cartItemsCount.get(1));
  }

  private void generateCartWithItems(Integer cartId) {
    var item = new Item(1, "test", 100, "description", "test-path");
    var item2 = new Item(2, "test", 100, "description", "test-path");
    var cartItem = new CartItem(cartId + 1, cartId, item, 10);
    var cartItem2 = new CartItem(cartId + 2, cartId, item, 5);

    itemsJpaRepository.saveAll(List.of(item, item2));
    cartsJpaRepository.save(new Cart(cartId, List.of(cartItem, cartItem2)));
  }

}
