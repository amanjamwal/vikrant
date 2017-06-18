package io.dhaam.common.jpa.internal;

import com.google.common.collect.Lists;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SimplePersistenceUnitInfo implements PersistenceUnitInfo {

  protected String persistenceUnitName;
  protected String persistenceProviderClassName;
  protected PersistenceUnitTransactionType transactionType;
  protected DataSource nonJtaDataSource;
  protected List<String> mappingFileNames = Lists.newArrayList();
  protected List<String> managedClassNames = Lists.newArrayList();

  @Override
  public DataSource getJtaDataSource() {
    return null;
  }

  @Override
  public List<URL> getJarFileUrls() {
    return Collections.emptyList();
  }

  @Override
  public URL getPersistenceUnitRootUrl() {
    return null;
  }

  @Override
  public boolean excludeUnlistedClasses() {
    return false;
  }

  @Override
  public SharedCacheMode getSharedCacheMode() {
    return null;
  }

  @Override
  public ValidationMode getValidationMode() {
    return null;
  }

  @Override
  public Properties getProperties() {
    return null;
  }

  @Override
  public String getPersistenceXMLSchemaVersion() {
    return "2.0";
  }

  @Override
  public ClassLoader getClassLoader() {
    return this.getClass().getClassLoader();
  }

  @Override
  public void addTransformer(ClassTransformer transformer) {
  }

  @Override
  public ClassLoader getNewTempClassLoader() {
    return this.getClass().getClassLoader();
  }
}
