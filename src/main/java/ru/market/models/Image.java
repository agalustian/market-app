package ru.market.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "images")
public class Image {

  @Id
  private Integer id;

  @Column(name = "item_id", nullable = false, updatable = false)
  private String itemId;

  @Column(name = "img_path", nullable = false, updatable = true)
  private String imgPath;

  @Column(name = "created_at", updatable = false)
  @CreatedDate
  private String createdAt;

  @Column(name = "updated_at", updatable = true)
  @LastModifiedDate
  private String updatedAt;

}
