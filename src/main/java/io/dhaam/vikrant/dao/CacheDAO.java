package io.dhaam.vikrant.dao;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import io.dhaam.common.GenericDAO;
import io.dhaam.vikrant.entity.Tuple;

/**
 * @author ajamwal
 * @since 12/13/16
 */

public class CacheDAO extends GenericDAO<Tuple, String> {

  @Inject
  public CacheDAO(Provider<EntityManager> entityManagerProvider) {
    super(entityManagerProvider);
  }
}
