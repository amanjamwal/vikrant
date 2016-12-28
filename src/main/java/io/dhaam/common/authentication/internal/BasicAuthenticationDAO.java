package io.dhaam.common.authentication.internal;

import com.google.inject.persist.Transactional;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import io.dhaam.common.AbstractDAO;
import io.dhaam.common.authentication.AuthRequest;
import io.dhaam.common.authentication.AuthenticationDAO;

/**
 * @author ajamwal
 * @since 12/21/16
 */
@Transactional
public class BasicAuthenticationDAO extends AbstractDAO<BasicAuthentication, String>
    implements AuthenticationDAO<BasicAuthentication, String> {

  @Inject
  public BasicAuthenticationDAO(Provider<EntityManager> entityManagerProvider) {
    super(entityManagerProvider);
  }

  @Override
  public boolean authenticate(AuthRequest authRequest) {
    return authenticate((BasicAuthRequest) authRequest);
  }

  private boolean authenticate(BasicAuthRequest authRequest) {
    Optional<BasicAuthentication> oBasicAuthentication = findOne(authRequest.getApiKey());
    return oBasicAuthentication.isPresent() && oBasicAuthentication.get().getIsActive();
  }
}
