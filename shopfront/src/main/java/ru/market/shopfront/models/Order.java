package ru.market.shopfront.models;

import java.util.List;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("orders")
public class Order {

  @Id
  private Integer id;

  @Column("total_sum")
  private Integer totalSum;

  @Column("user_id")
  private String userId;

  protected Order() {
  }

  public Order(String userId, Integer totalSum) {
    this.totalSum = totalSum;
    this.userId = userId;
  }

  public static Order from(String userId, List<CartItem> cartItems) {
    Integer totalSum = cartItems.stream()
        .map(cartItem -> cartItem.getPrice() * cartItem.getCount())
        .reduce(0, Integer::sum);

    return new Order(userId, totalSum);
  }

  public Integer getId() {
    return id;
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

