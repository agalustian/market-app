package ru.market.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

// TODO entity for link cart to userId
@Entity
@Table(name = "carts")
public class Cart {

  @Id
  private Integer id;

  @OneToMany
  @JoinColumn(name = "cart_id")
  private List<CartItem> cartItems = new ArrayList<>();

  public Integer getId() {
    return id;
  }

  public List<CartItem> getCartItems() {
    return cartItems;
  }

}
