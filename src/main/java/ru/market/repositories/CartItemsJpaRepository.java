package ru.market.repositories;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.models.CartItem;

@Repository
public interface CartItemsJpaRepository extends JpaRepository<CartItem, Integer> {

  CartItem getCartItemByCartIdAndItem_Id(Integer cartId, Integer itemId);

  List<CartItem> getCartItemsByItemIdInAndCartId(Collection<Integer> itemIds, Integer cartId);

}
