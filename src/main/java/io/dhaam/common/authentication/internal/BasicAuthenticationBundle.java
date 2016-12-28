package io.dhaam.common.authentication.internal;

import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * @author ajamwal
 * @since 12/28/16
 */
public class BasicAuthenticationBundle implements Bundle {
  @Override
  public void initialize(Bootstrap<?> bootstrap) {

  }

  @Override
  public void run(Environment environment) {
    environment.jersey().register(BasicAuthenticationFilter.class);
  }
}
