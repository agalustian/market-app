package ru.market.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import ru.market.models.OrderItem;

@Repository
public interface OrderItemsRepository extends ReactiveCrudRepository<OrderItem, Integer> {
  Flux<OrderItem> findAllByOrderId(Integer orderId);
}
