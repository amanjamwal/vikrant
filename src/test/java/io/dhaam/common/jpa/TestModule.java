package io.dhaam.common.jpa;

import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import javax.inject.Inject;

import io.dhaam.common.jpa.internal.DataSourceFactoryContainer;
import io.dhaam.common.jpa.internal.ManagedPersistenceService;
import io.dhaam.vikrant.dao.CacheDAO;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

import static org.mockito.Mockito.mock;

@Slf4j
public class TestModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new TestJpaModule(Lists.newArrayList(
        "io.dhaam.vikrant",
        "io.dhaam.common"
    )));

    bind(DataSourceFactoryContainer.class).toInstance(getDatabaseConfiguration());
    bind(Initializer.class).asEagerSingleton();

    bind(CacheDAO.class).in(Singleton.class);
  }

  private DataSourceFactoryContainer getDatabaseConfiguration() {
    return () -> {
      DataSourceFactory databaseConfiguration = new DataSourceFactory();
      databaseConfiguration.setDriverClass("org.hsqldb.jdbc.JDBCDriver");
      databaseConfiguration.setUrl("jdbc:hsqldb:mem:.");
      databaseConfiguration.setUser("SA");
      databaseConfiguration.setPassword("");
      databaseConfiguration.setValidationQuery("select 1 from INFORMATION_SCHEMA.SYSTEM_USERS");
      return databaseConfiguration;
    };
  }

  @Provides
  private Environment providesEnvironment() {
    return mock(Environment.class);
  }


  static class Initializer {

    private final ManagedPersistenceService persistenceService;

    @Inject
    public Initializer(ManagedPersistenceService persistenceService) throws Exception {
      this.persistenceService = persistenceService;
      persistenceService.start();
    }
  }
}
