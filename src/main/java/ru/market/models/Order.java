package ru.market.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  private Integer id;

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private List<OrderItem> orderItems = new ArrayList<>();

  @Column(name = "total_sum")
  private Integer totalSum;

  protected Order() {
  }

  public Order(Integer id, List<OrderItem> orderItems, Integer totalSum) {
    this.id = id;
    this.orderItems = orderItems;
    this.totalSum = totalSum;
  }

  public static Order from(Cart cart) {
    List<OrderItem> orderItems = cart.getCartItems().stream().map(
        cartItem -> OrderItem.from(cartItem, cartItem.getCount())).toList();

    return new Order(null, orderItems, cart.getTotalSum());
  }

  public Integer getId() {
    return id;
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }

  public Integer getTotalSum() {
    return totalSum;
  }

  public void setOrderItems(List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }
}

