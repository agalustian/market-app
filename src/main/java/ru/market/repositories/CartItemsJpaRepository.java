package ru.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.models.CartItem;

@Repository
public interface CartItemsJpaRepository extends CartItemsRepositoryCustom, JpaRepository<CartItem, Integer> {
}
