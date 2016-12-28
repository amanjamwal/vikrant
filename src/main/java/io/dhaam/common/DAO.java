package io.dhaam.common;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author ajamwal
 * @since 12/13/16
 */

public interface DAO<T, ID extends Serializable> {
  void persist(T entity);

  void persist(Iterable<? extends T> entities);

  <S extends T> S merge(S entity);

  <S extends T> Iterable<S> merge(Iterable<S> entities);

  void delete(T t);

  void delete(Iterable<? extends T> entities);

  List<T> findAll();

  Optional<T> findOne(ID id);

  Optional<T> findOneByQuery(String query, Map<String, Object> parameters);
}
