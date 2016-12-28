package io.dhaam.common.authentication;

import java.io.Serializable;

import io.dhaam.common.DAO;

/**
 * @author ajamwal
 * @since 12/21/16
 */

public interface AuthenticationDAO<T, ID extends Serializable> extends DAO<T, ID> {
  boolean authenticate(AuthRequest authRequest);
}
