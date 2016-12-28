package io.dhaam.common.jpa.transaction;

import com.google.inject.persist.Transactional;

import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.SpringTransactionAnnotationParser;
import org.springframework.transaction.annotation.TransactionAnnotationParser;
import org.springframework.util.ClassUtils;

import java.util.HashSet;
import java.util.Set;

import io.dhaam.common.jpa.transaction.annotation.GuiceTransactionAnnotationParser;

/**
 * @author ajamwal
 * @since 12/13/16
 */

public class CustomAnnotationTransactionAttributeSource
    extends AnnotationTransactionAttributeSource {

  private static final boolean guicePersistPresent = ClassUtils.isPresent(
      Transactional.class.getName(),
      AnnotationTransactionAttributeSource.class.getClassLoader()
  );

  private final boolean allowPublicMethodsOnly;

  public CustomAnnotationTransactionAttributeSource(boolean allowPublicMethodsOnly) {
    super(getAnnotationParsers());
    this.allowPublicMethodsOnly = allowPublicMethodsOnly;
  }

  @Override
  protected boolean allowPublicMethodsOnly() {
    return allowPublicMethodsOnly;
  }

  private static Set<TransactionAnnotationParser> getAnnotationParsers() {
    Set<TransactionAnnotationParser> annotationParsers = new HashSet<>();
    annotationParsers.add(new SpringTransactionAnnotationParser());
    if (guicePersistPresent) {
      annotationParsers.add(new GuiceTransactionAnnotationParser());
    }
    return annotationParsers;
  }
}
