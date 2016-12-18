package io.dhaam.common.jpa.transaction.annotation;

import com.google.inject.persist.Transactional;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.transaction.annotation.TransactionAnnotationParser;
import org.springframework.transaction.interceptor.NoRollbackRuleAttribute;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;

/**
 * @author ajamwal
 * @since 12/13/16
 */

public class GuiceTransactionAnnotationParser implements TransactionAnnotationParser {

    @Override
    public TransactionAttribute parseTransactionAnnotation(AnnotatedElement annotatedElement) {
        AnnotationAttributes annotationAttributes =
                AnnotatedElementUtils.getAnnotationAttributes(annotatedElement, Transactional.class.getName());
        return annotationAttributes != null ? parseTransactionAnnotation(annotationAttributes) : null;
    }

    protected TransactionAttribute parseTransactionAnnotation(AnnotationAttributes annotationAttributes) {
        RuleBasedTransactionAttribute rbta = new RuleBasedTransactionAttribute();
        rbta.setPropagationBehaviorName(RuleBasedTransactionAttribute.PREFIX_PROPAGATION + "REQUIRED");
        ArrayList<RollbackRuleAttribute> rollBackRules = new ArrayList<>();
        Class<?>[] rollbackOns = annotationAttributes.getClassArray("rollbackOn");
        for (Class<?> rbRule : rollbackOns) {
            RollbackRuleAttribute rule = new RollbackRuleAttribute(rbRule);
            rollBackRules.add(rule);
        }
        Class<?>[] nrbf = annotationAttributes.getClassArray("ignore");
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
