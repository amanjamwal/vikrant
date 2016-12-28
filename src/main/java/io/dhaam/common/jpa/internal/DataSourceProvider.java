package io.dhaam.common.jpa.internal;

import com.google.inject.Inject;

import com.codahale.metrics.MetricRegistry;

import javax.inject.Provider;
import javax.sql.DataSource;

import io.dropwizard.db.ManagedDataSource;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;

/**
 * @author ajamwal
 * @since 12/13/16
 */

public class DataSourceProvider implements Provider<DataSource>, Managed {

  private static final String DATA_SOURCE_NAME = "vikrant";

  private final Provider<DataSourceFactoryContainer> factoryContainerProvider;
  private MetricRegistry metricRegistry;
  private ManagedDataSource dataSource;

  @Inject
  public DataSourceProvider(Provider<DataSourceFactoryContainer> factoryContainerProvider,
                            Environment environment) {

    this.factoryContainerProvider = factoryContainerProvider;
    this.metricRegistry = environment.metrics();
  }

  @Override
  public ManagedDataSource get() {
    return this.dataSource == null ?
        initializeDataSource(factoryContainerProvider) : this.dataSource;
  }

  @Override
  public void start() throws Exception {
    get().start();
  }

  @Override
  public void stop() throws Exception {
    get().stop();
  }

  private ManagedDataSource initializeDataSource(
      Provider<DataSourceFactoryContainer> factoryContainerProvider) {

    DataSourceFactoryContainer configuration = factoryContainerProvider.get();
    return configuration.getDatabase().build(metricRegistry, DATA_SOURCE_NAME);
  }
}
