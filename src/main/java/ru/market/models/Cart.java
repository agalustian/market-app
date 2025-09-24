package ru.market.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "carts")
public class Cart {
  // TODO i think this should be userId as cartId or additional table for link cart with items
  @Id
  private Integer id;

  @Column(name = "item_id", nullable = false, updatable = false)
  @OneToMany
  @JoinColumn(name = "id")
  private List<Item> items = new ArrayList<>();

  @Column(name = "count", nullable = false, updatable = true)
  private Integer count;

  @Column(name = "created_at", updatable = false)
  @CreatedDate
  private String createdAt;

}
