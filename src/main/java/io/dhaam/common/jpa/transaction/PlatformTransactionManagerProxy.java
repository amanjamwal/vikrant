package io.dhaam.common.jpa.transaction;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author ajamwal
 * @since 12/13/16
 */

public class PlatformTransactionManagerProxy implements PlatformTransactionManager {

  private Provider<PlatformTransactionManager> target;

  @Inject
  public void setTarget(Provider<PlatformTransactionManager> target) {
    this.target = target;
  }

  @Override
  public TransactionStatus getTransaction(TransactionDefinition definition)
      throws TransactionException {
    return target.get().getTransaction(definition);
  }

  @Override
  public void commit(TransactionStatus status) throws TransactionException {
    target.get().commit(status);
  }

  @Override
  public void rollback(TransactionStatus status) throws TransactionException {
    target.get().rollback(status);
  }
}