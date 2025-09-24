package ru.market.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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

  @Column(name = "created_at", updatable = false)
  @CreatedDate
  private String createdAt;

  @Column(name = "updated_at", updatable = true)
  @LastModifiedDate
  private String updatedAt;

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

  public String getCreatedAt() {
    return createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

}
