package ru.market.services;

import java.util.List;
import org.springframework.stereotype.Service;
import ru.market.models.Order;
import ru.market.repositories.OrdersJpaRepository;

@Service
public class OrdersService {

  private final OrdersJpaRepository ordersRepository;

  public OrdersService(final OrdersJpaRepository ordersRepository) {
    this.ordersRepository = ordersRepository;
  }

  public List<Order> getOrders() {
    return ordersRepository.findAll();
  }

}
