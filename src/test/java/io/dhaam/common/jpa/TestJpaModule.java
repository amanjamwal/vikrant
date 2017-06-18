package io.dhaam.common.jpa;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;

import org.hibernate.cfg.ImprovedNamingStrategy;

import java.util.List;
import java.util.Properties;

import javax.inject.Provider;

import io.dhaam.common.jpa.internal.DataSourceProxy;
import io.dhaam.common.jpa.internal.DatabaseModule;
import io.dhaam.common.jpa.internal.PersistenceServiceLifeCycle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestJpaModule extends AbstractModule {

  private final String persistenceUnitName = "dhaam_test";
  private final List<String> packagesToScan;

  public TestJpaModule(List<String> packagesToScan) {
    this.packagesToScan = packagesToScan;
  }

  @Override
  protected void configure() {
    install(new DatabaseModule());

    DataSourceProxy dataSourceProxy = new DataSourceProxy();
    bind(DataSourceProxy.class).toInstance(dataSourceProxy);

    Properties jpaProperties = new Properties();
    jpaProperties.put("hibernate.connection.datasource", dataSourceProxy);
    jpaProperties.put("hibernate.ejb.naming_strategy", ImprovedNamingStrategy.INSTANCE);
    jpaProperties.put("hibernate.hbm2ddl.auto", "create-drop");
    jpaProperties.put("dynamicPersistenceProvider.packagesToScan", packagesToScan);

    install(new JpaPersistModule(persistenceUnitName).properties(jpaProperties));
  }

  @Provides
  @Singleton
  private PersistenceServiceLifeCycle providesPersistenceServiceLifeCycle(
      final Provider<PersistService> persistService) {

    return new PersistenceServiceLifeCycle() {
      @Override
      public void start() throws Exception {
        persistService.get().start();
      }

      @Override
      public void stop() throws Exception {
        persistService.get().stop();
      }
    };
  }
}