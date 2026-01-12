package ru.market.shopfront.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "cart_items")
public class CartItem {

  @Id
  private Integer id;

  @Column("user_id")
  private String userId;

  @Column("item_id")
  private Integer itemId;

  @Column("count")
  private Integer count;

  @Column("price")
  private Integer price;

  @Column("title")
  private String title;

  @Column("img_path")
  private String imgPath;

  @Column("description")
  private String description;

  protected CartItem() {
  }

  public CartItem(Integer id, String userId, Integer itemId, Integer count, String title, Integer price, String imgPath, String description) {
    this.id = id;
    this.userId = userId;
    this.itemId = itemId;
    this.price = price;
    this.count = count;
    this.title = title;
    this.imgPath = imgPath;
    this.description = description;
  }

  public CartItem(String userId, Integer itemId, Integer count) {
    this.userId = userId;
    this.itemId = itemId;
    this.count = count;
  }

  public Integer getId() {
    return id;
  }
  public Integer getItemId() {
    return itemId;
  }

  public String getUserId() {
    return userId;
  }

  public Integer getCount() {
    return count;
  }

  public Integer getPrice() {
    return price;
  }

  public String getTitle() {
    return title;
  }

  public String getImgPath() {
    return imgPath;
  }

  public String getDescription() {
    return description;
  }

}
