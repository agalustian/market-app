package ru.market.shopfront.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("items")
public class Item {

  @Id
  private Integer id;

  @Column("title")
  private String title;

  @Column("price")
  private Integer price;

  @Column("description")
  private String description;

  @Column("img_path")
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

  public Item(String title, Integer price, String description, String imgPath) {
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
