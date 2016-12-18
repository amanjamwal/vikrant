package io.dhaam.vikrant.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import io.dhaam.common.GenericDAO;
import io.dhaam.vikrant.entity.Tuple;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ajamwal
 * @since 12/13/16
 */

@Slf4j
public class CacheDAO extends GenericDAO<Tuple, String> {

  @Inject
  public CacheDAO(Provider<EntityManager> entityManagerProvider) {
    super(entityManagerProvider);
  }

  public Optional<Tuple> findKey(String item) {

    Map<String, Object> params = new HashMap<>();
    params.put("item", item);

    return findOneByQuery("findTupleByItem", params);
  }
}
