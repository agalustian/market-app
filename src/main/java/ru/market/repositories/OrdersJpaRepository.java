package ru.market.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.market.models.Order;

@Repository
public interface OrdersJpaRepository extends JpaRepository<Order, Integer> {
}
