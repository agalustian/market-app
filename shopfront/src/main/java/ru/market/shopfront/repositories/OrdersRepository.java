package ru.market.shopfront.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.market.shopfront.models.Order;

@Repository
public interface OrdersRepository extends ReactiveCrudRepository<Order, Integer> {

  Mono<Order> findByUserIdAndId(String userId, Integer id);

  Flux<Order> findAllByUserId(String userId);

}
