package ru.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.models.Item;

@Repository
public interface ItemsJpaRepository extends JpaRepository<Item, Integer> {
  Item getItemById(Integer id);
}
