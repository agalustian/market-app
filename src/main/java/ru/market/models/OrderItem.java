package ru.market.models;

public class OrderItem {

  private final Integer id;

  private final String title;

  private final Integer price;

  private final Integer count;

  private OrderItem(Integer id, String title, Integer price, Integer count) {
    this.id = id;
    this.title = title;
    this.price = price;
    this.count = count;
  }

  public static OrderItem from(final CartItem cartItem, Integer totalCount) {
    return new OrderItem(cartItem.getId(), cartItem.getItem().getTitle(), cartItem.getItem().getPrice(), totalCount);
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

  public Integer getCount() {
    return count;
  }
}
