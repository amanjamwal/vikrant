package io.dhaam.common;

import lombok.Builder;
import lombok.Data;

/**
 * @author ajamwal
 * @since 12/21/16
 */

@Data
@Builder
public class ErrorResponse {
  private int code;
  private String description;
}
