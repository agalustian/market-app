package ru.market.integration.controllers;

import java.util.Objects;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.market.dto.CartAction;
import ru.market.dto.ItemDTO;
import ru.market.dto.OrderDTO;
import ru.market.models.CartItem;
import ru.market.models.Item;
import ru.market.models.Order;
import ru.market.models.OrderItem;

public class BaseControllerTests {

  Flux<Item> generateItems() {
    var item = new Item(1, "Test", 100, "description", "test-path");
    var item2 = new Item(2, "another-test", 100, "description", "test-path");

    return Flux.just(item, item2);
  }

  Flux<CartItem> generateCartItems() {
    return generateItems().map(item ->
        new CartItem(999 + item.getId(), 999, item.getId(), 10, item.getTitle(), item.getPrice(), "t", "d")
    );
  }

  Flux<ItemDTO> generateItemDTO() {
    return generateCartItems().map(ItemDTO::from);
  }

  Flux<OrderItem> generateOrderItems() {
    return generateCartItems().map(cartItem ->
        OrderItem.from(1, cartItem, 1111)
    );
  }

  Mono<OrderDTO> generateOrderDTO() {
    return Mono.just(
        OrderDTO.from(new Order(1111), Objects.requireNonNull(generateOrderItems().collectList().block())));
  }

  MultiValueMap generateAddRemoveToCartBody(CartAction action) {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("itemId", "1");
    formData.add("action", action.toString());

    return formData;
  }

}
