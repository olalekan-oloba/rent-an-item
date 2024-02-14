package com.omegalambdang.rentanitem.common.audit;


import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@Embeddable
@Data
@SuppressWarnings("NullAway.Init")
public class AuditSection implements Serializable {
  @CreatedDate
  @Column
  private Instant dateCreated;

  @LastModifiedDate
  @Nullable
  @Column
  private Instant dateModified;

  @Column(name = "modified_by")
  @Nullable
  private String modifiedBy;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name="deleted", columnDefinition = "varchar(1) default (0)")
  private String delF="0";

  @Override
  public String toString() {
    return "AuditSection{dateCreated=%s, dateModified=%s, delF='%s'}"
            .formatted(dateCreated, dateModified, delF);
  }
}
