package io.dhaam.common.jpa;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

/**
 * @author ajamwal
 * @since 12/25/16
 */
public class ImprovedNamingStrategy implements PhysicalNamingStrategy {

  public static final PhysicalNamingStrategy INSTANCE = new ImprovedNamingStrategy();

  @Override
  public Identifier toPhysicalCatalogName(Identifier name,
                                          JdbcEnvironment jdbcEnvironment) {
    return apply(name, jdbcEnvironment);
  }

  @Override
  public Identifier toPhysicalSchemaName(Identifier name,
                                         JdbcEnvironment jdbcEnvironment) {
    return apply(name, jdbcEnvironment);
  }

  @Override
  public Identifier toPhysicalTableName(Identifier name,
                                        JdbcEnvironment jdbcEnvironment) {
    return apply(name, jdbcEnvironment);
  }

  @Override
  public Identifier toPhysicalSequenceName(Identifier name,
                                           JdbcEnvironment jdbcEnvironment) {
    return apply(name, jdbcEnvironment);
  }

  @Override
  public Identifier toPhysicalColumnName(Identifier name,
                                         JdbcEnvironment jdbcEnvironment) {
    return apply(name, jdbcEnvironment);
  }

  private Identifier apply(Identifier name, JdbcEnvironment jdbcEnvironment) {
    if (name == null) {
      return null;
    }
    StringBuilder builder = new StringBuilder(name.getText().replace('.', '_'));
    for (int i = 1; i < builder.length() - 1; i++) {
      if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i),
          builder.charAt(i + 1))) {
        builder.insert(i++, '_');
      }
    }
    return getIdentifier(builder.toString(), name.isQuoted(), jdbcEnvironment);
  }

  protected Identifier getIdentifier(String name, boolean quoted,
                                     JdbcEnvironment jdbcEnvironment) {
    if (isCaseInsensitive(jdbcEnvironment)) {
      name = name.toLowerCase(Locale.ROOT);
    }
    return new Identifier(name, quoted);
  }

  protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
    return true;
  }

  private boolean isUnderscoreRequired(char before, char current, char after) {
    return Character.isLowerCase(before) && Character.isUpperCase(current)
        && Character.isLowerCase(after);
  }

}