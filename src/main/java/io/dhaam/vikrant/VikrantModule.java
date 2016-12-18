package io.dhaam.vikrant;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Provider;
import javax.inject.Singleton;

import io.dhaam.common.jpa.internal.DataSourceFactoryContainer;
import io.dhaam.vikrant.dao.CacheDAO;
import io.dhaam.vikrant.resources.PingResource;
import io.dropwizard.db.DataSourceFactory;

/**
 * @author ajamwal
 * @since 12/13/16
 */

public class VikrantModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(PingResource.class).in(Singleton.class);
    bind(CacheDAO.class).in(Singleton.class);
  }

  @Provides
  DataSourceFactoryContainer providesDatabaseConfiguration(
      final Provider<VikrantConfiguration> provider) {
    return () -> (DataSourceFactory) provider.get().getDatabase();
  }
}
