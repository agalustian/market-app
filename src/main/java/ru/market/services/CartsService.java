package ru.market.services;

import org.springframework.stereotype.Service;
import ru.market.models.Cart;
import ru.market.repositories.CartsJpaRepository;

@Service
public class CartsService {

  private final CartsJpaRepository cartsRepository;

  public CartsService(CartsJpaRepository cartsRepository) {
    this.cartsRepository = cartsRepository;
  }

  public Cart getCart() {
    // TODO fix hardcode, its for test, replace it when users will be added
    return cartsRepository.getCartById(1);
  }

}
