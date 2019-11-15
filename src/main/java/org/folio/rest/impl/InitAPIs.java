package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.folio.rest.resource.interfaces.InitAPI;
import org.folio.spring.SpringContextUtil;
import org.folio.spring.config.ApplicationConfig;

public class InitAPIs implements InitAPI {

  @Override
  public void init(Vertx vertx, Context context, Handler<AsyncResult<Boolean>> handler) {
    System.out.println("Book Shop");

    vertx.executeBlocking(
      future -> {
        SpringContextUtil.init(vertx, context, ApplicationConfig.class);
        future.complete();
      },
      result -> {
        if (result.succeeded()) {
          handler.handle(Future.succeededFuture(true));
        } else {
          handler.handle(Future.failedFuture(result.cause()));
        }
      });

//    try {
//      System.out.println("Book Shop");
//      handler.handle(io.vertx.core.Future.succeededFuture(true));
//    } catch (Exception e) {
//      e.printStackTrace();
//      handler.handle(io.vertx.core.Future.failedFuture(e.getMessage()));
//    }
  }
}
