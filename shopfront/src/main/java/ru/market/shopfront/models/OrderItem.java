package ru.market.shopfront.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("order_items")
public class OrderItem implements Persistable<Integer> {

  @Id
  private Integer id;

  @Column("order_id")
  private Integer orderId;

  @Column("title")
  private String title;

  @Column("price")
  private Integer price;

  @Column("count")
  private Integer count;

  @Transient
  private boolean newAggregate = false;

  protected OrderItem() {
  }

  public OrderItem(Integer id, Integer orderId, String title, Integer price, Integer count) {
    this.id = id;
    this.orderId = orderId;
    this.title = title;
    this.price = price;
    this.count = count;
  }

  public static OrderItem from(final Integer orderId, CartItem cartItem, Integer totalCount) {
    var orderItem = new OrderItem(cartItem.getId(), orderId, cartItem.getTitle(), cartItem.getPrice(), totalCount);
    orderItem.setNewAggregate(true);

    return orderItem;
  }

  @Override
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

  public void setNewAggregate(boolean newAggregate) {
    this.newAggregate = newAggregate;
  }

  @Override
  public boolean isNew() {
    return newAggregate;
  }

}
