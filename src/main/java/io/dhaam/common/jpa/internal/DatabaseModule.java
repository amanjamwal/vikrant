package io.dhaam.common.jpa.internal;

import com.google.inject.AbstractModule;

import javax.inject.Singleton;
import javax.sql.DataSource;

/**
 * @author ajamwal
 * @since 2/16/17
 */
public class DatabaseModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(DataSource.class).toProvider(DataSourceProvider.class).in(Singleton.class);
  }
}
