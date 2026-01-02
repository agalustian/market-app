package ru.market.shopfront.integration.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static reactor.core.publisher.Mono.when;

import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import ru.market.shopfront.dto.CartAction;
import ru.market.shopfront.integration.PostgreSQLTestContainer;
import ru.market.shopfront.models.CartItem;
import ru.market.shopfront.models.Item;
import ru.market.shopfront.payment.domain.PaymentResult;
import ru.market.shopfront.repositories.CartItemsRepository;
import ru.market.shopfront.repositories.ItemsRepository;
import ru.market.shopfront.services.CartsService;

@SpringBootTest
@Testcontainers
@Profile("test")
@ImportTestcontainers(PostgreSQLTestContainer.class)
class CartsServiceTests {

  @Autowired
  private CartsService cartsService;

  @Autowired
  private CartItemsRepository cartItemsRepository;

  @Autowired
  private ItemsRepository itemsRepository;

  @BeforeEach
  void prepare() {
    var item = itemsRepository.save(new Item("title", 100, "description", "/2002")).block();
    var anotherItem = itemsRepository.save(new Item("title", 100, "description", "/2")).block();
    cartItemsRepository.save(new CartItem(1001, item.getId(), 10)).block();
    cartItemsRepository.save(new CartItem(1001, anotherItem.getId(), 1)).block();
  }

  @AfterEach
  void clean() {
    cartItemsRepository.deleteAll().block();
    itemsRepository.deleteAll().block();
  }

  @Test
  void shouldCreateOrderOnBuying() {
    cartsService.buy(1001)
        .doOnNext(orderDTO -> {
          assertEquals(1, orderDTO.id());
          assertEquals(2, orderDTO.items().size());
          assertEquals(1100, orderDTO.items().stream().map(cartItem -> cartItem.price() * cartItem.count())
              .reduce(0, Integer::sum));

        }).block();
  }

  @Test
  void shouldGetCart() {
    cartsService.getCart(1001).collectList()
        .doOnNext(carItems -> {
          assertEquals(2, carItems.size());

          var carItem = carItems.getFirst();

          assertEquals(10, carItem.count());
          assertEquals("/2002", carItem.imgPath());
          assertEquals("title", carItem.title());
          assertEquals(100, carItem.price());
          assertEquals("description", carItem.description());
        }).block();
  }

  @Test
  void shouldAddToCart() {
    cartItemsRepository.deleteAll().block();

    var item = new Item("title", 1001, "description", "/3");
    itemsRepository.save(item).block();

    cartsService.addRemoveToCart(1002, item.getId(), CartAction.PLUS)
        .doOnNext(result -> {
          var cartItems = cartsService.getCart(1002).collectList().block();

          assertEquals(1, cartItems.size());
          assertEquals(cartItems.getFirst().id(), item.getId());
        }).block();
  }

}
