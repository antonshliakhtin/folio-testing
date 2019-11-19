package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.rest.ResponseHelper;
import org.folio.rest.annotations.Validate;
import org.folio.rest.jaxrs.model.Book;
import org.folio.rest.jaxrs.resource.Books;
import org.folio.service.BookService;
import org.folio.spring.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static org.folio.rest.tools.utils.TenantTool.tenantId;

public class BooksImpl implements Books {

  private static final String LOCATION_PREFIX = "/books/";

  private final Logger log = LoggerFactory.getLogger("mod-testing-books");

  @Autowired
  private BookService bookService;

  public BooksImpl(Vertx vertx, String tenantId) {
    SpringContextUtil.autowireDependencies(this, vertx.getOrCreateContext());
  }

  @Override
  @Validate
  public void getBooks(String query, int offset, int limits, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("getBooks");
    ResponseHelper.respond(bookService.getBooks(query, offset, limits, tenantId(okapiHeaders)),
      GetBooksResponse::respond200WithApplicationJson, asyncResultHandler, null);

  }

  @Override
  @Validate
  public void postBooks(Book book, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("postBooks");

//    succeededFuture()
//      .compose(object -> bookService.addBook(book, new OkapiParams(okapiHeaders)))
//      .map(handleSuccessfulPost())
//      .setHandler(asyncResultHandler);
    asyncResultHandler.handle(Future.succeededFuture(PostBooksResponse.status(Response.Status.NOT_IMPLEMENTED).build()));
  }

  private Function<Book, Response> handleSuccessfulPost() {
    return book ->
      PostBooksResponse.respond201WithApplicationJson(book,
        PostBooksResponse.headersFor201().withLocation(LOCATION_PREFIX + book.getId()));
  }
}
