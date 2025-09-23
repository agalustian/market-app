package ru.market.services;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.market.dto.ItemsSort;
import ru.market.models.Item;
import ru.market.repositories.ItemsJpaRepository;

@Service
public class ItemsService {
  private final ItemsJpaRepository itemsRepository;

  public ItemsService(ItemsJpaRepository itemsRepository) {
    this.itemsRepository = itemsRepository;
  }

  public Item getItemById(final Integer itemId) {
    return itemsRepository.getItemById(itemId);
  }

  public List<Item> search(final String search, ItemsSort sort, PageRequest pageRequest) {
    var searchValue = search.toLowerCase();

    return itemsRepository.searchItemsByTitleContainingOrDescriptionContaining(searchValue, searchValue,
        Sort.by(getSortField(sort)).ascending(), pageRequest);
  }

  public Integer searchCount(final String search) {
    return itemsRepository.countItemsByTitleContainingOrDescriptionContaining(search, search);
  }

  private String prepareSearchValue(final String search) {
    return search.toLowerCase();
  }

  private String getSortField(ItemsSort sort) {
    return switch (sort) {
      case NO -> "id";
      case ALPHA -> "title";
      case PRICE -> "price";
    };
  }

}
