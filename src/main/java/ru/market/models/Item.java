package ru.market.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "items")
public class Item {

  @Id
  private Integer id;

  @Column(name = "title", nullable = false, updatable = true)
  private String title;

  @Column(name = "price", nullable = false, updatable = true)
  private Long price;

  @Column(name = "description", nullable = false)
  private String description;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id")
  private List<Image> images = new ArrayList<>();

  @Column(name = "created_at", updatable = false)
  @CreatedDate
  private String createdAt;

  @Column(name = "updated_at", updatable = true)
  @LastModifiedDate
  private String updatedAt;

  public Item(Integer id, String title, Long price, String description) {
    this.id = id;
    this.title = title;
    this.price = price;
    this.description = description;
  }

  protected Item() {
  }

}
