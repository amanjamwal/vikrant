package io.dhaam.common.authentication.internal;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.dropwizard.jackson.JsonSnakeCase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ajamwal
 * @since 12/21/16
 */
@Entity
@Data
@JsonSnakeCase
@AllArgsConstructor
@NoArgsConstructor
public class BasicAuthentication {
  @Id
  private String apiKey;

  private Boolean isActive;
}
