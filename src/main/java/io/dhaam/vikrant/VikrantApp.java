package io.dhaam.vikrant;

import com.google.common.collect.Sets;
import com.google.inject.Stage;

import com.hubspot.dropwizard.guice.GuiceBundle;

import io.dhaam.common.authentication.internal.BasicAuthenticationBundle;
import io.dhaam.common.jpa.JpaPersistenceModule;
import io.dhaam.vikrant.entity.Tuple;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ajamwal
 * @since 12/13/16
 */

@Slf4j
public class VikrantApp extends Application<VikrantConfiguration> {

  public static void main(String[] args) throws Exception {
    new VikrantApp().run(args);
  }

  @Override
  public void initialize(final Bootstrap<VikrantConfiguration> bootstrap) {

    bootstrap.addBundle(new MigrationsBundle<VikrantConfiguration>() {
      @Override
      public DataSourceFactory getDataSourceFactory(VikrantConfiguration vikrantConfiguration) {
        return vikrantConfiguration.getDatabase();
      }
    });

    bootstrap.addBundle(createHibernateBundle());

    bootstrap.addBundle(new BasicAuthenticationBundle());

    bootstrap.addBundle(GuiceBundle.<VikrantConfiguration>newBuilder()
        .setConfigClass(VikrantConfiguration.class)
        .addModule(new VikrantModule())
        .addModule(new JpaPersistenceModule(
            Sets.newHashSet(
                "io.dhaam.vikrant",
                "io.dhaam.common"
            )
        ))
        .enableAutoConfig("io.dhaam.vikrant")
        .build(Stage.DEVELOPMENT));
  }

  @Override
  public void run(VikrantConfiguration vikrantConfiguration, Environment environment)
      throws Exception {
    log.info("Vikrant warming up ...");
  }

  public static HibernateBundle<VikrantConfiguration> createHibernateBundle() {
    return new HibernateBundle<VikrantConfiguration>(Tuple.class) {

      @Override
      public DataSourceFactory getDataSourceFactory(VikrantConfiguration configuration) {
        return configuration.getDatabase();
      }
    };
  }
}
