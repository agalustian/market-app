package ru.market.shopfront.repositories;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.market.shopfront.models.Item;

@Repository
public interface ItemsRepository extends ReactiveCrudRepository<Item, Integer> {
  Flux<Item> findItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, PageRequest pageRequest);

  Mono<Integer> countItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
