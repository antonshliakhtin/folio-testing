package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.common.OkapiParams;
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

import static io.vertx.core.Future.succeededFuture;
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
    asyncResultHandler.handle(Future.succeededFuture(GetBooksResponse.status(Response.Status.NOT_IMPLEMENTED).build()));
  }

  @Override
  @Validate
  public void postBooks(Book book, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("postBooks");

    succeededFuture()
      .compose(object -> bookService.addBook(book, new OkapiParams(okapiHeaders)))
      .map(handleSuccessfulPost())
      .setHandler(asyncResultHandler);
    asyncResultHandler.handle(Future.succeededFuture(PostBooksResponse.status(Response.Status.NOT_IMPLEMENTED).build()));
  }

  @Override
  @Validate
  public void putBooks(String accessToken, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("putBooks");

  }

  @Override
  @Validate
  public void deleteBooks(Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("deleteBooks");

  }

  @Override
  @Validate
  public void getBooksById(String id, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("getBooksById");

    ResponseHelper.respond(bookService.getOneBook(id, tenantId(okapiHeaders)),
      GetBooksByIdResponse::respond200WithApplicationJson, asyncResultHandler, null);
  }

  @Override
  @Validate
  public void putBooksById(String id, String accessToken, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("getBooksById");

  }

  @Override
  @Validate
  public void deleteBooksById(String id, String accessToken, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("deleteBooksById");

  }

  @Override
  @Validate
  public void getBooksAuthorById(String id, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("getBooksAuthorById");

  }

  @Override
  @Validate
  public void getBooksTitleById(String id, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("getBooksTitleById");

  }

  @Override
  @Validate
  public void getBooksByBookTitle(String bookTitle, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("getBooksByBookTitle");

  }

  @Override
  @Validate
  public void putBooksByBookTitle(String bookTitle, String accessToken, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("putBooksByBookTitle");

  }

  @Override
  @Validate
  public void getBooksByAuthorName(String authorName, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("getBooksByAuthorName");

  }

  @Override
  @Validate
  public void putBooksByAuthorName(String authorName, String accessToken, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    log.debug("putBooksByAuthorName");

  }

  private Function<Book, Response> handleSuccessfulPost() {
    return book ->
      PostBooksResponse.respond201WithApplicationJson(book,
        PostBooksResponse.headersFor201().withLocation(LOCATION_PREFIX + book.getId()));
  }
}
