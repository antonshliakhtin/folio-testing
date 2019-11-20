package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.common.OkapiParams;
import org.folio.rest.annotations.Validate;
import org.folio.rest.jaxrs.model.Book;
import org.folio.rest.jaxrs.resource.Books;
import org.folio.service.BookService;
import org.folio.spring.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Map;
import java.util.function.Function;

import static io.vertx.core.Future.succeededFuture;

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
  }

  @Override
  @Validate
  public void postBooks(Book book, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("postBooks");

    Future<Book> future = succeededFuture();
    future.compose(o -> bookService.saveBook(book, new OkapiParams(okapiHeaders)))
      .map(PostBooksResponse::respond200WithApplicationJson)
      .otherwise(exception -> {
        if (exception instanceof NotFoundException || exception instanceof NotAuthorizedException ||
          exception instanceof IllegalArgumentException || exception instanceof IllegalStateException ||
          exception instanceof BadRequestException) {
          asyncResultHandler.handle(succeededFuture(PostBooksResponse.respond400WithTextPlain(exception.getMessage())));
        } else {
          asyncResultHandler.handle(succeededFuture(PostBooksResponse.respond500WithTextPlain(exception.getMessage())));
        }
        return null;
      });
  }

  private Function<Book, Response> handleSuccessfulPost() {
    return book ->
      PostBooksResponse.respond201WithApplicationJson(book,
        PostBooksResponse.headersFor201().withLocation(LOCATION_PREFIX + book.getId()));
  }
}
