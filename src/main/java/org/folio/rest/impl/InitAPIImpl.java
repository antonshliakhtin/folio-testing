package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.serviceproxy.ServiceBinder;
import org.folio.rest.resource.interfaces.InitAPI;
import org.folio.service.BookService;
import org.folio.spring.SpringContextUtil;
import org.folio.spring.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;

public class InitAPIImpl implements InitAPI {

  @Autowired
  private BookService bookService;

  @Override
  public void init(Vertx vertx, Context context, Handler<AsyncResult<Boolean>> handler) {
    System.out.println("Book Shop");

    vertx.executeBlocking(
      future -> {
        SpringContextUtil.init(vertx, context, ApplicationConfig.class);
        SpringContextUtil.autowireDependencies(this, context);
        new ServiceBinder(vertx)
          .setAddress("book-service.queue")
          .register(BookService.class, bookService);
        future.complete();
      },
      result -> {
        if (result.succeeded()) {
          handler.handle(Future.succeededFuture(true));
        } else {
          handler.handle(Future.failedFuture(result.cause()));
        }
      });
  }
}
