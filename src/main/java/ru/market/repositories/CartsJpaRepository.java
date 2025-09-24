package ru.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.models.Cart;

@Repository
public interface CartsJpaRepository extends JpaRepository<Cart, Integer> {
  Cart getCartById(Integer id);
}
