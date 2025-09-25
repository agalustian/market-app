package ru.market.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_items")
public class CartItem {

  @Id
  private Integer id;

  @Column(name = "cart_id", nullable = false, updatable = false)
  private Integer cartId;

  @ManyToOne()
  @JoinColumn(name = "item_id", referencedColumnName = "id")
  private Item item;

  @Column(name = "count", nullable = false, updatable = true)
  private Integer count;

  protected CartItem() {
  }

  public CartItem(Integer id, Integer cartId, Item item, Integer count) {
    this.id = id;
    this.cartId = cartId;
    this.item = item;
    this.count = count;
  }

  public CartItem(Integer cartId, Item item, Integer count) {
    this.cartId = cartId;
    this.item = item;
    this.count = count;
  }

  public Integer getId() {
    return id;
  }

  public Integer getCartId() {
    return cartId;
  }

  public Item getItem() {
    return item;
  }

  public Integer getCount() {
    return count;
  }

  public void incrementCount() {
    this.count = count + 1;
  }

  public void decrementCount() {
    if (count > 0) {
      this.count = count - 1;
    }
  }

}
