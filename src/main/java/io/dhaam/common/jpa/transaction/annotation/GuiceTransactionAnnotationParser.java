package io.dhaam.common.jpa.transaction.annotation;

import com.google.inject.persist.Transactional;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.TransactionAnnotationParser;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;

import lombok.NoArgsConstructor;

/**
 * @author ajamwal
 * @since 12/13/16
 */

@NoArgsConstructor
public class GuiceTransactionAnnotationParser implements TransactionAnnotationParser {

  @Override
  public TransactionAttribute parseTransactionAnnotation(AnnotatedElement ae) {
    AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(
        ae,
        Transactional.class
    );
    return attributes != null ? parseTransactionAnnotation(attributes) : null;
  }

  public TransactionAttribute parseTransactionAnnotation(Transactional ann) {
    return parseTransactionAnnotation(AnnotationUtils.getAnnotationAttributes(ann, false, false));
  }

  protected TransactionAttribute parseTransactionAnnotation(AnnotationAttributes attributes) {
    RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
    rbta.setPropagationBehaviorName(RuleBasedTransactionAttribute.PREFIX_PROPAGATION + "REQUIRED");
    ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList<>();
    Class<?>[] rollbackOns = attributes.getClassArray("rollbackOn");
    for (Class<?> rbRule : rollbackOns) {
      RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
      rollBackRules.add(rule);
    }
    Class<?>[] nrbf = attributes.getClassArray("ignore");
    for (Class<?> rbRule : nrbf) {
      NoRollbackRuleAttribute rule = new NoRollbackRuleAttribute(rbRule);
      rollBackRules.add(rule);
    }
    rbta.getRollbackRules().addAll(rollBackRules);
    return rbta;
  }

  @Override
  public boolean equals(Object other) {
    return (this == other || other instanceof GuiceTransactionAnnotationParser);
  }

  @Override
  public int hashCode() {
    return GuiceTransactionAnnotationParser.class.hashCode();
  }

}
