package ru.market.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "orders")
public class Order {

  @Id
  private Integer id;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "order_items", columnDefinition = "jsonb")
  private List<OrderItem> orderItems = new ArrayList<>();

  @Column(name = "total_sum")
  private Integer totalSum;

  @Column(name = "created_at", updatable = false)
  @CreatedDate
  private String createdAt;

  protected Order() {
  }

  private Order(Integer id, List<OrderItem> orderItems, Integer totalSum, String createdAt) {
    this.id = id;
    this.orderItems = orderItems;
    this.totalSum = totalSum;
    this.createdAt = createdAt;
  }

  public static Order from(Cart cart) {
    List<OrderItem> orderItems = cart.getCartItems().stream().map(
        cartItem -> OrderItem.from(cartItem, cartItem.getCount())).toList();

    return new Order(null, orderItems, cart.getTotalSum(), null);
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

  public String getCreatedAt() {
    return createdAt;
  }

}

