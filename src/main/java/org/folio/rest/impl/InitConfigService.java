package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import org.folio.rest.resource.interfaces.PostDeployVerticle;

public class InitConfigService implements PostDeployVerticle {

  @Override
  public void init(Vertx vertx, Context context, Handler<AsyncResult<Boolean>> handler) {
//
//    System.out.println("Getting secret key to decode DB password.");
//    /** hard code the secret key for now - in production env - change this to read from a secure place */
//    String secretKey = "b2%2BS%2BX4F/NFys/0jMaEG1A";
//    int port = context.config().getInteger("http.port");
//    AdminClient ac = new AdminClient("http://localhost:" + port, null, null);
//    try {
//      ac.postAdminSetAESKey(secretKey, reply -> {
//        if(reply.statusCode() == 204){
//          handler.handle(io.vertx.core.Future.succeededFuture(true));
//        }
//        else{
//          handler.handle(io.vertx.core.Future.failedFuture(reply.statusCode() + ", " + reply.statusMessage()));
//        }
//      });
//    } catch (UnsupportedEncodingException e) {
//      e.printStackTrace();
//    }
  }
}
