package ru.market.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {

  @Id
  private Integer id;

  @Column(name = "order_id")
  private Integer orderId;

  @Column(name = "title")
  private String title;

  @Column(name = "price")
  private Integer price;

  @Column(name = "count")
  private Integer count;

  protected OrderItem() {
  }

  public OrderItem(Integer id, Integer orderId, String title, Integer price, Integer count) {
    this.id = id;
    this.orderId = orderId;
    this.title = title;
    this.price = price;
    this.count = count;
  }

  public static OrderItem from(final CartItem cartItem, Integer totalCount) {
    return new OrderItem(cartItem.getId(), null, cartItem.getItem().getTitle(), cartItem.getItem().getPrice(), totalCount);
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
