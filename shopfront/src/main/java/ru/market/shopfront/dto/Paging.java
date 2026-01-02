package ru.market.shopfront.dto;

public class Paging {
  private final Integer pageNumber;
  private final Integer pageSize;
  private final Integer totalCount;

  public Paging(Integer pageNumber, Integer pageSize, Integer totalCount) {
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalCount = totalCount;
  }

  public boolean hasNext() {
    return pageNumber * pageSize < totalCount;
  }

  public boolean hasPrevious() {
    return pageNumber > 1;
  }

  public Integer pageNumber() {
    return pageNumber;
  }

  public Integer pageSize() {
    return pageSize;
  }
}