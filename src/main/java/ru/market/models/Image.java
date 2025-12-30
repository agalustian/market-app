package ru.market.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "images")
public class Image {

  @Id
  private Integer id;

  @Column(name = "content")
  private byte[] content;

  public Image(Integer id, byte[] content) {
    this.id = id;
    this.content = content;
  }

  protected Image() {}

  public Integer getId() {
    return id;
  }

  public byte[] getContent() {
    return content;
  }

}
