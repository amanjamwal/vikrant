package io.dhaam.vikrant;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import lombok.Data;

/**
 * @author ajamwal
 * @since 12/13/16
 */

@Data
public class VikrantConfiguration extends Configuration {
  @Valid
  @NotNull
  private DataSourceFactory database = new DataSourceFactory();
}
