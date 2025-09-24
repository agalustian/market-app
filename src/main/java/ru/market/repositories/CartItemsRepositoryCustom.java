package ru.market.repositories;

import java.util.List;
import java.util.Map;

public interface CartItemsRepositoryCustom {

  Map<Integer, Integer> countCartItems(Integer cartId, List<Integer> itemIds);

}
