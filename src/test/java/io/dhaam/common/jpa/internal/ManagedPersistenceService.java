package io.dhaam.common.jpa.internal;

import com.google.inject.Inject;

import io.dropwizard.lifecycle.Managed;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManagedPersistenceService implements Managed {

  private final PersistenceServiceLifeCycle persistService;

  @Inject
  public ManagedPersistenceService(PersistenceServiceLifeCycle persistService) {
    this.persistService = persistService;
  }

  @Override
  public void start() throws Exception {
    ManagedPersistenceService.log.info("Initializing persistence service");
    persistService.start();
  }

  @Override
  public void stop() throws Exception {
    persistService.stop();
    ManagedPersistenceService.log.info("stopped persistence service");
  }
}
