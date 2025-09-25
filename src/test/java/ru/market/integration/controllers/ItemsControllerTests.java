package ru.market.integration.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.market.dto.ItemDTO;
import ru.market.models.Cart;
import ru.market.models.CartItem;
import ru.market.models.Item;
import ru.market.repositories.CartsJpaRepository;
import ru.market.repositories.ItemsJpaRepository;

@TestPropertySource(locations = "classpath:application.yaml")
@SpringBootTest
@AutoConfigureMockMvc
public class ItemsControllerTests {

  private final MockMvc mockMvc;

  private CartsJpaRepository cartsJpaRepository;

  private ItemsJpaRepository itemsJpaRepository;

  @Autowired
  ItemsControllerTests(final MockMvc mockMvc, CartsJpaRepository cartsJpaRepository,
                       ItemsJpaRepository itemsJpaRepository) {
    this.mockMvc = mockMvc;
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
    var cartItem2 = new CartItem(999 + 2, 999, item2, 5);
    var cartItem3 = new CartItem(999 + 3, 999, item3, 2);

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

  @Test
  void shouldGetItemById() throws Exception {
    mockMvc.perform(get("/items/1"))
        .andExpect(status().isOk())
        .andExpect(view().name("item"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(model().attributeExists("item"));
  }

  @Nested
  @SpringBootTest
  @AutoConfigureMockMvc
  class AddRemoveToCartFromItemsView {
    private static final String ACTUAL_URL_PARAMS = "search=test&sort=ALPHA&pageNumber=1&pageSize=5";

    @Test
    void shouldAddItemToCart() throws Exception {
      mockMvc.perform(post("/items?id=1&action=PLUS&" + ACTUAL_URL_PARAMS))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/items?" + ACTUAL_URL_PARAMS));
    }

    @Test
    void shouldDecrementItemInCart() throws Exception {
      mockMvc.perform(post("/items?id=1&action=MINUS&" + ACTUAL_URL_PARAMS))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/items?" + ACTUAL_URL_PARAMS));
    }

    @Test
    void shouldDeleteItemInCart() throws Exception {
      mockMvc.perform(post("/items?id=1&action=DELETE&" + ACTUAL_URL_PARAMS))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/items?" + ACTUAL_URL_PARAMS));
    }
  }

  @Test
  void shouldAddItemToCart() throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/items/1?action=PLUS"))
        .andExpect(status().isOk())
        .andExpect(view().name("item"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(model().attributeExists("item"))
        .andReturn();

    var model = mvcResult.getModelAndView().getModel();

    ItemDTO item = (ItemDTO) model.get("item");

    assertEquals(11, item.count());
  }

  @Test
  void shouldDecrementItemInCart() throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/items/1?action=MINUS")).andReturn();

    var model = mvcResult.getModelAndView().getModel();

    ItemDTO item = (ItemDTO) model.get("item");

    assertEquals(9, item.count());
  }

  @Test
  void shouldDeleteItemInCart() throws Exception {
    mockMvc.perform(post("/items/1?action=DELETE"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/items"));
  }

  @Test
  void shouldSaveImage() throws Exception {
    byte[] image = {108};
    mockMvc.perform(multipart("/items/image/1")
            .file(new MockMultipartFile("image", image))
            .contentType("multipart/form-data")
        )
        .andExpect(status().isOk())
        .andExpect(view().name("item"))
        .andExpect(model().attributeExists("item"));
  }

}
