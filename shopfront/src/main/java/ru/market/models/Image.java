package ru.market.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("images")
public class Image implements Persistable<Integer> {

  @Id
  @Column("item_id")
  private Integer itemId;

  @Column("content")
  private byte[] content;

  @Transient
  private boolean newAggregate = false;

  public Image(Integer itemId, byte[] content) {
    this.itemId = itemId;
    this.content = content;
  }

  protected Image() {}

  @Override
  public Integer getId() {
    return itemId;
  }

  public byte[] getContent() {
    return content;
  }

  public void setNewAggregate(boolean newAggregate) {
    this.newAggregate = newAggregate;
  }

  @Override
  public boolean isNew() {
    return newAggregate;
  }

}
