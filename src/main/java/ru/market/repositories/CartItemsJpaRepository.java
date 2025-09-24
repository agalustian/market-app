package ru.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.market.models.CartItem;

@Repository
public interface CartItemsJpaRepository extends JpaRepository<CartItem, Integer> {

  void deleteCartItemByCartIdAndItem_Id(Integer cartId, Integer itemId);

  @Query("""
      update CartItem
      set count = count + 1
      where cartId = :cartId and Item.id = :itemId
      """)
  void incrementCount(Integer cartId, Integer itemId);

  @Query("""
      update CartItem
      set count = count - 1
      where cartId = :cartId and Item.id = :itemId and count > 0
      """)
  void decrementCount(Integer cartId, Integer itemId);

}
