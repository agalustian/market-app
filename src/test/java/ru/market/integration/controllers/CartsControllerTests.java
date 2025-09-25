package ru.market.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import ru.market.integration.IntegrationTestConfig;
import ru.market.models.Cart;
import ru.market.models.CartItem;
import ru.market.models.Item;
import ru.market.repositories.CartsJpaRepository;
import ru.market.repositories.ItemsJpaRepository;

@TestPropertySource(locations = "classpath:application.yaml")
class CartsControllerTests extends IntegrationTestConfig {

  private final CartsJpaRepository cartsJpaRepository;

  private final ItemsJpaRepository itemsJpaRepository;

  @Autowired
  CartsControllerTests(CartsJpaRepository cartsJpaRepository,
                       ItemsJpaRepository itemsJpaRepository) {
    this.cartsJpaRepository = cartsJpaRepository;
    this.itemsJpaRepository = itemsJpaRepository;
  }

  @BeforeEach
  void prepare() {
    cartsJpaRepository.deleteAll();
    itemsJpaRepository.deleteAll();

    var item = new Item(1, "Test", 100, "description", "test-path");
    var item2 = new Item(2, "another-test", 100, "description", "test-path");
    var cartItem = new CartItem(999 + 1, 999, item, 10);
    var cartItem2 = new CartItem(999 + 2, 999, item2, 5);

    itemsJpaRepository.saveAll(List.of(item, item2));
    cartsJpaRepository.save(new Cart(999, List.of(cartItem, cartItem2)));
  }

  @Test
  void shouldGetCartItems() throws Exception {
    mockMvc.perform(get("/cart/items"))
        .andExpect(status().isOk())
        .andExpect(view().name("cart"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(model().attributeExists("items"))
        .andExpect(model().attributeExists("total"))
        .andReturn();
  }

  @Test
  void shouldBuyCartItems() throws Exception {
    mockMvc.perform(post("/cart/buy"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/orders/1?newOrder=true"));
  }

  @Nested
  @Import(IntegrationTestConfig.class)
  class AddRemoveToCart extends IntegrationTestConfig {

    @Test
    void shouldAddItemToCart() throws Exception {
      mockMvc.perform(post("/cart/items?id=1&action=PLUS"))
          .andExpect(status().isOk())
          .andExpect(view().name("cart"))
          .andExpect(content().contentType("text/html;charset=UTF-8"))
          .andExpect(model().attributeExists("items"))
          .andExpect(model().attributeExists("total"));
    }

    @Test
    void shouldDecrementItemInCart() throws Exception {
      mockMvc.perform(post("/cart/items?id=1&action=MINUS")).andExpect(status().isOk())
          .andExpect(view().name("cart"))
          .andExpect(content().contentType("text/html;charset=UTF-8"))
          .andExpect(model().attributeExists("items"))
          .andExpect(model().attributeExists("total"));
    }

    @Test
    void shouldDeleteItemInCart() throws Exception {
      mockMvc.perform(post("/cart/items?id=1&action=DELETE"))
          .andExpect(view().name("cart"))
          .andExpect(content().contentType("text/html;charset=UTF-8"))
          .andExpect(model().attributeExists("items"))
          .andExpect(model().attributeExists("total"));
    }

  }

}
