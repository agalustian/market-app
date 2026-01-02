package ru.market.services;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.market.dto.OrderDTO;
import ru.market.repositories.OrderItemsRepository;
import ru.market.repositories.OrdersRepository;

@Service
public class OrdersService {

  private final OrdersRepository ordersRepository;

  private final OrderItemsRepository orderItemsRepository;

  public OrdersService(final OrdersRepository ordersRepository, OrderItemsRepository orderItemsRepository) {
    this.ordersRepository = ordersRepository;
    this.orderItemsRepository = orderItemsRepository;
  }

  public Flux<OrderDTO> getOrders() {
    return ordersRepository.findAll().flatMap(order ->
        orderItemsRepository
            .findAllByOrderId(order.getId())
            .collectList()
            .map(orderItems -> OrderDTO.from(order, orderItems))
    );
  }

  public Mono<OrderDTO> getOrder(final Integer id) {
    return ordersRepository.findById(id).flatMap(order ->
        orderItemsRepository
            .findAllByOrderId(order.getId())
            .collectList()
            .map(orderItems -> OrderDTO.from(order, orderItems))
    );
  }

}
