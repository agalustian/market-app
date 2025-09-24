package ru.market.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

  @Id
  private final Integer id;

  @Column(name = "title")
  private final String title;

  @Column(name = "price")
  private final Integer price;

  @Column(name = "count")
  private final Integer count;

  private OrderItem(Integer id, String title, Integer price, Integer count) {
    this.id = id;
    this.title = title;
    this.price = price;
    this.count = count;
  }

  public static OrderItem from(final CartItem cartItem, Integer totalCount) {
    return new OrderItem(cartItem.getId(), cartItem.getItem().getTitle(), cartItem.getItem().getPrice(), totalCount);
  }

  public Integer getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public Integer getPrice() {
    return price;
  }

  public Integer getCount() {
    return count;
  }

}
