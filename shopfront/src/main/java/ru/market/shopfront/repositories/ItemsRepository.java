package ru.market.shopfront.repositories;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.market.shopfront.models.Item;

@Repository
public interface ItemsRepository extends ReactiveCrudRepository<Item, Integer> {
  @Cacheable("items")
  Flux<Item> findItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, PageRequest pageRequest);

  @Override
  @Cacheable("item")
  Mono<Item> findById(Integer itemId);

  Mono<Integer> countItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
