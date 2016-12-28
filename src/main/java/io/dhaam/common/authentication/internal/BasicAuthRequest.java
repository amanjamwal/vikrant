package io.dhaam.common.authentication.internal;

import io.dhaam.common.authentication.AuthRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ajamwal
 * @since 12/21/16
 */

@Getter
@AllArgsConstructor
public class BasicAuthRequest implements AuthRequest {
  private final String apiKey;
}
