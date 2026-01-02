package ru.market.shopfront.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import ru.market.shopfront.models.Order;

@Repository
public interface OrdersRepository extends ReactiveCrudRepository<Order, Integer> {
}
