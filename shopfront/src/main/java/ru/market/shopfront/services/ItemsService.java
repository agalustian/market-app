package ru.market.shopfront.services;

import java.util.NoSuchElementException;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.market.shopfront.dto.ItemDTO;
import ru.market.shopfront.dto.ItemsSort;
import ru.market.shopfront.models.CartItem;
import ru.market.shopfront.models.Image;
import ru.market.shopfront.models.Item;
import ru.market.shopfront.repositories.CartItemsRepository;
import ru.market.shopfront.repositories.ImagesRepository;
import ru.market.shopfront.repositories.ItemsRepository;

@Service
public class ItemsService {

  private static final Integer MAX_BYTES = 5 * 1024 * 1024;

  private final ItemsRepository itemsRepository;

  private final CartItemsRepository cartItemsRepository;

  private final ImagesRepository imagesRepository;

  public ItemsService(ItemsRepository itemsRepository,
                      CartItemsRepository cartItemsRepository,
                      ImagesRepository imagesRepository) {
    this.itemsRepository = itemsRepository;
    this.cartItemsRepository = cartItemsRepository;
    this.imagesRepository = imagesRepository;
  }

  public Mono<ItemDTO> getItemById(final Integer itemId, final String userId) {
    return itemsRepository.findById(itemId)
        .flatMap(item -> cartItemsRepository.findByUserIdAndItemId(userId, itemId)
            .map(CartItem::getCount)
            .defaultIfEmpty(0)
            .map(count -> {
              item.setCount(count);

              return item;
            })
        ).map(ItemDTO::from);
  }

  public Mono<ItemDTO> getItemById(final Integer itemId) {
    return itemsRepository.findById(itemId).map(ItemDTO::from);
  }

  public Flux<ItemDTO> search(final String search, ItemsSort sort, PageRequest pageRequest, String userId) {
    return searchFromRepository(search, sort, pageRequest)
        .map(item ->
            cartItemsRepository.findByUserIdAndItemId(userId, item.getId())
                .map(CartItem::getCount)
                .defaultIfEmpty(0)
                .map(count -> {
                  item.setCount(count);

                  return item;
                })
        ).flatMap(el -> el.map(ItemDTO::from));
  }

  public Flux<ItemDTO> search(String search, ItemsSort sort, PageRequest pageRequest) {
    return searchFromRepository(search, sort, pageRequest).map(ItemDTO::from);
  }

  private Flux<Item> searchFromRepository(String search, ItemsSort sort, PageRequest pageRequest) {
    return itemsRepository.findItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search,
        pageRequest.withSort(Sort.by(getSortField(sort)).ascending()));
  }

  public Mono<Integer> searchCount(final String search) {
    return itemsRepository.countItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search);
  }

  @Transactional
  public Mono<ItemDTO> saveItemImage(final Integer itemId, FilePart image) {
    Assert.notNull(itemId, "Item id is required for getting image");


    MediaType contentType = image.headers().getContentType();
    if (contentType == null || !"image".equalsIgnoreCase(contentType.getType())) {
      return Mono.error(new IllegalArgumentException("Нужен файл изображения"));
    }

    return DataBufferUtils.join(image.content())
        .flatMap(dataBuffer -> {
          try {
            int readable = dataBuffer.readableByteCount();
            if (readable <= 0) {
              return Mono.error(new IllegalStateException("Пустой файл"));
            }
            if (readable > MAX_BYTES) {
              return Mono.error(new IllegalArgumentException("Слишком большой файл"));
            }

            byte[] imageBytes = new byte[readable];

            dataBuffer.read(imageBytes);

            return itemsRepository.findById(itemId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Item with such id not exists")))
                .flatMap(item -> imagesRepository.findById(itemId)
                    .flatMap(existing -> {
                      existing.setNewAggregate(false);
                      return imagesRepository.save(existing);
                    })
                    .switchIfEmpty(Mono.defer(() -> {
                      Image newImage = new Image(itemId, imageBytes);
                      newImage.setNewAggregate(true);
                      return imagesRepository.save(newImage);
                    }))
                    .flatMap(upsertedImage -> {
                      item.setImgPath("/image/" + upsertedImage.getId());

                      return itemsRepository.save(item).map(ItemDTO::from);
                    })
                );
          } finally {
            DataBufferUtils.release(dataBuffer);
          }
        });
  }

  public Mono<byte[]> getItemImage(final Integer itemId) {
    Assert.notNull(itemId, "Item id is required for getting image");

    return imagesRepository.findById(itemId).map(Image::getContent);
  }

  private String getSortField(ItemsSort sort) {
    return switch (sort) {
      case NO -> "id";
      case ALPHA -> "title";
      case PRICE -> "price";
    };
  }

}
