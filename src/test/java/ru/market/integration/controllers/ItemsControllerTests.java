package ru.market.integration.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.market.models.Cart;
import ru.market.models.CartItem;
import ru.market.models.Item;
import ru.market.repositories.CartsJpaRepository;
import ru.market.repositories.ItemsJpaRepository;
import ru.market.services.CartsService;
import ru.market.services.ItemsService;

@TestPropertySource(locations = "classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
public class ItemsControllerTests {

  private final MockMvc mockMvc;

  private CartsService cartsService;

  private ItemsService itemsService;

  private CartsJpaRepository cartsJpaRepository;

  private ItemsJpaRepository itemsJpaRepository;

  @Autowired
  ItemsControllerTests(final MockMvc mockMvc, CartsService cartsService, ItemsService itemsService,
                       CartsJpaRepository cartsJpaRepository, ItemsJpaRepository itemsJpaRepository) {
    this.mockMvc = mockMvc;
    this.cartsService = cartsService;
    this.itemsService = itemsService;
    this.cartsJpaRepository = cartsJpaRepository;
    this.itemsJpaRepository = itemsJpaRepository;
  }

  @BeforeEach
  void prepare() {
    cartsJpaRepository.deleteAll();
    itemsJpaRepository.deleteAll();

    var item = new Item(1, "Test", 100, "description", "test-path");
    var item2 = new Item(2, "another-test", 100, "description", "test-path");
    var item3 = new Item(3, "other-test", 100, "description", "test-path");
    var cartItem = new CartItem(999 + 1, 999, item, 10);
    var cartItem2 = new CartItem(999 + 2, 999, item, 5);
    var cartItem3 = new CartItem(999 + 3, 999, item, 2);

    itemsJpaRepository.saveAll(List.of(item, item2, item3));
    cartsJpaRepository.save(new Cart(999, List.of(cartItem, cartItem2, cartItem3)));
  }

  @Test
  void shouldSearchItems() throws Exception {
    mockMvc.perform(get("/items?search=test&sort=NO&pageNumber=1&pageSize=1"))
        .andExpect(status().isOk())
        .andExpect(view().name("items"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(model().attributeExists("items"))
        .andExpect(model().attributeExists("search"))
        .andExpect(model().attributeExists("sort"))
        .andExpect(model().attributeExists("paging"));
  }

}
