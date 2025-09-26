package ru.market.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id")
  private List<OrderItem> orderItems = new ArrayList<>();

  @Column(name = "total_sum")
  private Integer totalSum;

  protected Order() {
  }

  public Order(List<OrderItem> orderItems, Integer totalSum) {
    this.orderItems = orderItems;
    this.totalSum = totalSum;
  }

  public static Order from(Cart cart) {
    List<OrderItem> orderItems = cart.getCartItems().stream().map(
        cartItem -> OrderItem.from(cartItem, cartItem.getCount())).toList();

    return new Order(orderItems, cart.getTotalSum());
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

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(id, order.id) && Objects.equals(totalSum, order.totalSum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, totalSum);
  }

}

