package io.dhaam.common.jpa;

import com.google.inject.Provider;
import com.google.inject.persist.UnitOfWork;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AbstractDAOTest {

  @Inject
  protected Provider<EntityManager> entityManagerProvider;

  @Inject
  protected UnitOfWork unitOfWork;

  protected EntityTransaction transaction;

  @BeforeMethod
  public void setup() throws Exception {
    unitOfWork.begin();
    transaction = entityManagerProvider.get().getTransaction();
    transaction.begin();
    log.info("setup - transaction status {}.", transaction.isActive());
  }

  @AfterMethod
  public void tearDown() throws Exception {
    log.info("tearDown - transaction status {}.", transaction.isActive());
    transaction.rollback();
    unitOfWork.end();
  }

  protected EntityManager getEntityManager() {
    return entityManagerProvider.get();
  }

  protected <T> T persist(T object) {
    entityManagerProvider.get().persist(object);
    return object;
  }
}
