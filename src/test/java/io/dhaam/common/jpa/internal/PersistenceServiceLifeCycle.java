package io.dhaam.common.jpa.internal;

public interface PersistenceServiceLifeCycle {
  void start() throws Exception;

  void stop() throws Exception;
}
