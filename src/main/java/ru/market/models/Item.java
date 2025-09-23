package ru.market.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
  private Double userId;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "created_at", updatable = false)
  @CreatedDate
  private String createdAt;

  @Column(name = "updated_at", updatable = true)
  @LastModifiedDate
  private String updatedAt;

  public Item(Integer id, String title, Double userId, String description) {
    this.id = id;
    this.title = title;
    this.userId = userId;
    this.description = description;
  }

  protected Item() {
  }

}
