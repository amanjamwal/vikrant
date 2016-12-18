package io.dhaam.common.jpa.internal;

import io.dropwizard.db.DataSourceFactory;

/**
 * @author ajamwal
 * @since 12/13/16
 */

public interface DataSourceFactoryContainer {
  DataSourceFactory getDatabase();
}
