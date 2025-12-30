package ru.market.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ru.market.models.Cart;

@Repository
public interface CartsJpaRepository extends JpaRepository<Cart, Integer> {
  Cart findCartById(Integer id);

  // TODO name is not good enough
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Cart getCartById(Integer id);
}
