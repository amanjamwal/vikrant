package io.dhaam.vikrant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ajamwal
 * @since 12/13/16
 */

@Entity
@Table(name = "cache")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tuple {
  @Id
  private String id;

  @Column(name = "item", nullable = false)
  private String item;
}
