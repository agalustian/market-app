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
  private Integer price;

  @Column(name = "description", nullable = false)
  private String description;

  @Column(name = "img_path", nullable = false)
  private String imgPath;

  @Column(name = "created_at", updatable = false)
  @CreatedDate
  private String createdAt;

  @Column(name = "updated_at", updatable = true)
  @LastModifiedDate
  private String updatedAt;

  private Integer count;

  public Item(Integer id, String title, Integer price, String description, String imgPath) {
    this.id = id;
    this.title = title;
    this.price = price;
    this.description = description;
    this.imgPath = imgPath;
  }

  protected Item() {
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

  public String getDescription() {
    return description;
  }

  public String getImgPath() {
    return imgPath;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setCount(final Integer count) {
    this.count = count == null ? 0 : count;
  }

  public Integer getCount() {
    return this.count;
  }

}
