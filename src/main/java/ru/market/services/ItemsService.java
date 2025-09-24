package ru.market.services;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.market.dto.ItemsSort;
import ru.market.models.Item;
import ru.market.repositories.CartItemsJpaRepository;
import ru.market.repositories.ItemsJpaRepository;

@Service
public class ItemsService {
  private static Integer CART_ID = 3;

  private final ItemsJpaRepository itemsRepository;

  private final CartItemsJpaRepository cartItemsRepository;

  public ItemsService(ItemsJpaRepository itemsRepository, CartItemsJpaRepository cartItemsRepository) {
    this.itemsRepository = itemsRepository;
    this.cartItemsRepository = cartItemsRepository;
  }

  public Item getItemById(final Integer itemId) {
    Item item = itemsRepository.getItemById(itemId);
    var cartItemsCount = cartItemsRepository.countCartItems(CART_ID, List.of(itemId));
    item.setCount(cartItemsCount.get(itemId));

    return item;
  }

  public List<Item> search(final String search, ItemsSort sort, PageRequest pageRequest) {
    var searchValue = search.toLowerCase();

    List<Item> items = itemsRepository.searchItemsByTitleContainingOrDescriptionContaining(searchValue, searchValue,
        Sort.by(getSortField(sort)).ascending(), pageRequest);

    if (items.isEmpty()) {
      return items;
    }

    var cartItemsCount = cartItemsRepository.countCartItems(CART_ID, items.stream().map(Item::getId).toList());

    for (Item item: items) {
      item.setCount(cartItemsCount.get(item.getId()));
    }

    return items;
  }

  public Integer searchCount(final String search) {
    return itemsRepository.countItemsByTitleContainingOrDescriptionContaining(search, search);
  }

  private String getSortField(ItemsSort sort) {
    return switch (sort) {
      case NO -> "id";
      case ALPHA -> "title";
      case PRICE -> "price";
    };
  }

}
