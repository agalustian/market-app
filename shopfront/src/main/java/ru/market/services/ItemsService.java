package ru.market.services;

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
import ru.market.dto.ItemDTO;
import ru.market.dto.ItemsSort;
import ru.market.models.CartItem;
import ru.market.models.Image;
import ru.market.repositories.CartItemsRepository;
import ru.market.repositories.ImagesRepository;
import ru.market.repositories.ItemsRepository;

@Service
public class ItemsService {
  private static final Integer CART_ID = 999;

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

  public Mono<ItemDTO> getItemById(final Integer itemId) {
    return itemsRepository.findById(itemId)
        .flatMap(item -> cartItemsRepository.findByCartIdAndItemId(CART_ID, itemId)
            .map(CartItem::getCount)
            .defaultIfEmpty(0)
            .map(count -> {
              item.setCount(count);

              return item;
            })
        ).map(ItemDTO::from);
  }

  public Flux<ItemDTO> search(final String search, ItemsSort sort, PageRequest pageRequest) {
    return itemsRepository.findItemsByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(search, search,
            pageRequest.withSort(Sort.by(getSortField(sort)).ascending()))
        .map(item ->
            cartItemsRepository.findByCartIdAndItemId(CART_ID, item.getId())
                .map(CartItem::getCount)
                .defaultIfEmpty(0)
                .map(count -> {
                  item.setCount(count);

                  return item;
                })
        ).flatMap(el -> el.map(ItemDTO::from));
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
