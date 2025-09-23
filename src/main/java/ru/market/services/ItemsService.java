package ru.market.services;

import org.springframework.stereotype.Service;
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

}
