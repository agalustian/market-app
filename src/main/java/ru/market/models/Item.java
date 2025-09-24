package ru.market.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

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

  @Column(name = "img_path", nullable = true)
  private String imgPath;

  @Transient
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

  public Integer getCount() {
    return this.count;
  }

  public void setCount(final Integer count) {
    this.count = count == null ? 0 : count;
  }

  public void setImgPath(final String imgPath) {
    this.imgPath = imgPath;
  }

}
