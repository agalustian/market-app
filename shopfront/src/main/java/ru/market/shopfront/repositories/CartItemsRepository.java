package ru.market.shopfront.repositories;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.market.shopfront.models.CartItem;

@Repository
public interface CartItemsRepository extends ReactiveCrudRepository<CartItem, Integer> {

  @Query("""
      SELECT ci.id, ci.user_id, ci.item_id, ci.count, i.title as "title", i.price as "price", i.img_path as "img_path", i.description as "description"
      FROM cart_items ci INNER JOIN items i ON ci.item_id = i.id
      WHERE ci.user_id = :userId and i.id = :itemId
      """)
  Mono<CartItem> findByUserIdAndItemId(String userId, Integer itemId);

  @Modifying
  @Query("UPDATE cart_items SET count = count + 1 WHERE id = :id")
  Mono<Void> incrementCountById(Integer id);

  @Modifying
  @Query("UPDATE cart_items SET count = count - 1 WHERE id = :id and count > 1")
  Mono<Void> decrementCountById(Integer id);

  @Query("""
      SELECT ci.id, ci.user_id, ci.item_id, ci.count, i.title as "title", i.price as "price", i.img_path as "img_path", i.description as "description"
      FROM cart_items ci LEFT JOIN items i ON ci.item_id = i.id
      WHERE ci.user_id = :userId
      """)
  Flux<CartItem> findCartItems(String userId);

  Mono<Void> deleteAllByUserId(String userId);

}
