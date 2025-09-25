package ru.market.repositories;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.models.Item;

@Repository
public interface ItemsJpaRepository extends JpaRepository<Item, Integer> {
  Item getItemById(Integer id);

  List<Item> findItemsByTitleContainingIgnoreCase(String title, PageRequest pageRequest);

  Integer countItemsByTitleContainingOrDescriptionContaining(String title, String description);
}
