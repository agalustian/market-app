package ru.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.market.models.Item;

public interface ItemsJpaRepository extends JpaRepository<Item, Integer> {
}
