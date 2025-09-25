package ru.market.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.market.dto.ItemsSort;
import ru.market.models.CartItem;
import ru.market.models.Image;
import ru.market.models.Item;
import ru.market.repositories.CartItemsJpaRepository;
import ru.market.repositories.ImagesJpaRepository;
import ru.market.repositories.ItemsJpaRepository;

@Service
public class ItemsService {
  private static final Integer CART_ID = 999;

  private final ItemsJpaRepository itemsRepository;

  private final CartItemsJpaRepository cartItemsRepository;

  private final ImagesJpaRepository imagesRepository;

  public ItemsService(ItemsJpaRepository itemsRepository, CartItemsJpaRepository cartItemsRepository,
                      ImagesJpaRepository imagesRepository) {
    this.itemsRepository = itemsRepository;
    this.cartItemsRepository = cartItemsRepository;
    this.imagesRepository = imagesRepository;
  }

  public Item getItemById(final Integer itemId) {
    CartItem cartItem = cartItemsRepository.getCartItemByCartIdAndItem_Id(CART_ID, itemId);

    cartItem.getItem().setCount(cartItem.getCount());

    return cartItem.getItem();
  }

  public List<Item> search(final String search, ItemsSort sort, PageRequest pageRequest) {
    var searchValue = search.toLowerCase();

    List<Item> items = itemsRepository.findItemsByTitleContainingIgnoreCase(searchValue,
        pageRequest.withSort(Sort.by(getSortField(sort)).ascending()));

    if (items.isEmpty()) {
      return items;
    }

    var cartItemsCount =
        cartItemsRepository.getCartItemsByItemIdInAndCartId(items.stream().map(Item::getId).toList(), CART_ID).stream()
            .collect(
                Collectors.toMap((cartItem) -> cartItem.getItem().getId(), (cartItem) -> cartItem.getCount())
            );

    for (Item item : items) {
      item.setCount(cartItemsCount.get(item.getId()));
    }

    return items;
  }

  public Integer searchCount(final String search) {
    return itemsRepository.countItemsByTitleContainingOrDescriptionContaining(search, search);
  }

  @Transactional
  public Item saveItemImage(final Integer itemId, byte[] image) {
    Assert.notNull(itemId, "Item id is required for getting image");
    Item item = itemsRepository.getItemById(itemId);

    if (item == null) {
      throw new NoSuchElementException("Item with such id not exists");
    }

    imagesRepository.save(new Image(itemId, image)).getContent();

    item.setImgPath("/image" + itemId);

    itemsRepository.save(item);

    return item;
  }

  public byte[] getItemImage(final Integer itemId) {
    Assert.notNull(itemId, "Item id is required for getting image");

    return imagesRepository.getImageById(itemId).getContent();
  }

  private String getSortField(ItemsSort sort) {
    return switch (sort) {
      case NO -> "id";
      case ALPHA -> "title";
      case PRICE -> "price";
    };
  }

}
