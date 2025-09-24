package ru.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.market.models.Cart;

public interface CartsJpaRepository extends JpaRepository<Cart, Integer> {
}
