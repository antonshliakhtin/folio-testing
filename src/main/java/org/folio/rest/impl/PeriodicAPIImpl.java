package org.folio.rest.impl;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import org.folio.rest.resource.interfaces.PeriodicAPI;

public class PeriodicAPIImpl implements PeriodicAPI {

  @Override
  public long runEvery() {
    return 45000;
  }

  @Override
  public void run(Vertx vertx, Context context) {
  }
}
