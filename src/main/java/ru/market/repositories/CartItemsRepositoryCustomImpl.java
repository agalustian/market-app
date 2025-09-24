package ru.market.repositories;

import jakarta.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemsRepositoryCustomImpl implements CartItemsRepositoryCustom {
  private final EntityManager entityManager;

  public CartItemsRepositoryCustomImpl(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public Map<Integer, Integer> countCartItems(Integer cartId, List<Integer> itemIds) {
    var query = entityManager.createQuery("""
            select item.id, count(*) as comments_count
            from CartItem ci
            where cartId = :cartId and item.id IN (:itemIds)
            group by item
        """);

    query.setParameter("cartId", cartId);
    query.setParameter("itemIds", itemIds);

    var queryResultList = (List<Object[]>) query.getResultList();
    var cartItemsCounts = new HashMap<Integer, Integer>();

    for (Object[] row : queryResultList) {
      cartItemsCounts.put((Integer) row[0], ((Number) row[1]).intValue());
    }

    return cartItemsCounts;
  }

}
