package io.dhaam.common;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * @author ajamwal
 * @since 12/13/16
 */

public abstract class AbstractDAO<T, ID extends Serializable> implements DAO<T, ID> {

  private final Provider<EntityManager> entityManagerProvider;
  private final Class<T> entityClass;

  @Inject
  public AbstractDAO(Provider<EntityManager> entityManagerProvider) {
    this.entityManagerProvider = entityManagerProvider;
    this.entityClass = getEntityClass();
  }

  @Override
  public void persist(T t) {
    getEntityManager().persist(t);
  }

  @Override
  public void persist(Iterable<? extends T> entities) {
    entities.forEach(this::persist);
  }

  @Override
  public <S extends T> S merge(S entity) {
    return getEntityManager().merge(entity);
  }

  @Override
  public <S extends T> Iterable<S> merge(Iterable<S> entities) {
    List<S> result = Lists.newArrayList();
    entities.forEach(entity -> result.add(merge(entity)));
    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public List<T> findAll() {
    return getEntityManager()
        .createQuery("select x from " + entityClass.getSimpleName() + " x")
        .getResultList();
  }

  @Override
  public Optional<T> findOne(ID id) {
    return Optional.ofNullable(getEntityManager().find(entityClass, id));
  }

  @Override
  public void delete(T t) {
    getEntityManager().remove(t);
  }

  @Override
  public void delete(Iterable<? extends T> entities) {
    entities.forEach(this::delete);
  }

  @Override
  @SuppressWarnings("unchecked")
  public Optional<T> findOneByQuery(String queryName, Map<String, Object> parameters) {
    Query query = getEntityManager().createNamedQuery(queryName);

    for (Map.Entry<String, Object> entry : parameters.entrySet()) {
      query.setParameter(entry.getKey(), entry.getValue());
    }

    try {
      return Optional.of((T) query.getSingleResult());
    } catch (NoResultException e) {
      return Optional.empty();
    }
  }

  protected EntityManager getEntityManager() {
    return entityManagerProvider.get();
  }

  @SuppressWarnings("unchecked")
  private Class<T> getEntityClass() {
    ParameterizedType genericSuperclass = (ParameterizedType) getGenericSuperClass();
    return (Class<T>) genericSuperclass.getActualTypeArguments()[0];
  }

  @SuppressWarnings("unchecked")
  private Type getGenericSuperClass() {
    Class clazz = getClass();
    while (clazz != null
        && clazz.getSuperclass() != null
        && !clazz.getSuperclass().isAssignableFrom(AbstractDAO.class)) {

      clazz = clazz.getSuperclass();
    }
    checkNotNull(clazz);
    return clazz.getGenericSuperclass();
  }

}
