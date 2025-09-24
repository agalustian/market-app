package ru.market.models;

public class OrderItem {

  private Integer id;

  private String title;

  private String description;

  private Integer price;

  private Integer count;

  public Integer getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public Integer getPrice() {
    return price;
  }

  public Integer getCount() {
    return count;
  }
}
