package ru.market.integration.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.market.dto.OrderDTO;
import ru.market.integration.IntegrationTestConfig;
import ru.market.models.Order;
import ru.market.models.OrderItem;
import ru.market.repositories.OrdersJpaRepository;

@TestPropertySource(locations = "classpath:application.yaml")
class OrdersControllerTests extends IntegrationTestConfig {

  private final MockMvc mockMvc;

  private final OrdersJpaRepository ordersJpaRepository;

  private Integer orderId;

  @Autowired
  OrdersControllerTests(final MockMvc mockMvc, OrdersJpaRepository ordersJpaRepository) {
    this.mockMvc = mockMvc;
    this.ordersJpaRepository = ordersJpaRepository;
  }

  @BeforeEach
  void prepare() {
    ordersJpaRepository.deleteAll();

    OrderItem orderItem = new OrderItem(111, 1, "Test", 100, 8);
    Order order = new Order(List.of(orderItem), 800);

    this.orderId = ordersJpaRepository.save(order).getId();
  }

  @Test
  void shouldGetOrderById() throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/orders/"+orderId))
        .andExpect(status().isOk())
        .andExpect(view().name("order"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(model().attributeExists("order"))
        .andReturn();

    var model = mvcResult.getModelAndView().getModel();

    OrderDTO orderDTO = (OrderDTO) model.get("order");
    var orderItems = orderDTO.items();

    assertEquals(orderId, orderDTO.id());
    assertEquals(800, orderDTO.totalSum());
    assertEquals(1, orderItems.size());
  }

  @Test
  void shouldGetOrders() throws Exception {
    mockMvc.perform(get("/orders"))
        .andExpect(status().isOk())
        .andExpect(view().name("orders"))
        .andExpect(content().contentType("text/html;charset=UTF-8"))
        .andExpect(model().attributeExists("orders"))
        .andReturn();
  }

}
