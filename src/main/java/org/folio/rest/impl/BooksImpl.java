package org.folio.rest.impl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Context;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.common.OkapiParams;
import org.folio.rest.annotations.Validate;
import org.folio.rest.jaxrs.model.Book;
import org.folio.rest.jaxrs.resource.Books;
import org.folio.rest.tools.utils.TenantTool;
import org.folio.service.BookService;
import org.folio.spring.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.Map;

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
    succeededFuture()
      .compose(o -> bookService.getBooks(query, offset, limits, TenantTool.tenantId(okapiHeaders)))
      .map(books -> {
        asyncResultHandler.handle(succeededFuture(GetBooksResponse.respond200WithApplicationJson(books)));
        return null;
      })
      .otherwise(exception -> {
        if (exception instanceof NotFoundException || exception instanceof NotAuthorizedException ||
          exception instanceof BadRequestException) {
          asyncResultHandler.handle(succeededFuture(PostBooksResponse.respond400WithTextPlain(exception.getMessage())));
        } else {
          asyncResultHandler.handle(succeededFuture(PostBooksResponse.respond500WithTextPlain(exception.getMessage())));
        }
        return null;
      });
  }

  @Override
  @Validate
  public void postBooks(Book book, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    succeededFuture()
      .compose(o -> bookService.saveBook(book, new OkapiParams(okapiHeaders)))
      .map(newBook -> {
        asyncResultHandler.handle(
          succeededFuture(PostBooksResponse.respond201WithApplicationJson(newBook, PostBooksResponse.headersFor201())));
        return null;
      })
      .otherwise(exception -> {
        if (exception instanceof NotFoundException || exception instanceof NotAuthorizedException ||
          exception instanceof BadRequestException) {
          asyncResultHandler.handle(succeededFuture(PostBooksResponse.respond400WithTextPlain(exception.getMessage())));
        } else {
          asyncResultHandler.handle(succeededFuture(PostBooksResponse.respond500WithTextPlain(exception.getMessage())));
        }
        return null;
      });
  }

  @Override
  public void getBooksById(String id, Map<String, String> okapiHeaders, Handler<AsyncResult<Response>> asyncResultHandler, Context vertxContext) {
    succeededFuture()
      .compose(o -> bookService.getOneBook(id, TenantTool.tenantId(okapiHeaders)))
      .map(oneBook -> {
        asyncResultHandler.handle(
          succeededFuture(GetBooksByIdResponse.respond200WithApplicationJson(oneBook)));
        return null;
      })
      .otherwise(exception -> {
        if (exception instanceof BadRequestException) {
          asyncResultHandler.handle(succeededFuture(GetBooksByIdResponse.respond400WithApplicationJson(exception.getMessage())));
        } else if (exception instanceof NotAuthorizedException) {
          asyncResultHandler.handle(succeededFuture(GetBooksByIdResponse.respond401WithApplicationJson(exception.getMessage())));
        } else if (exception instanceof NotFoundException) {
          asyncResultHandler.handle(succeededFuture(GetBooksByIdResponse.respond404WithApplicationJson(exception.getMessage())));
        } else {
          asyncResultHandler.handle(succeededFuture(GetBooksByIdResponse.respond500WithApplicationJson(exception.getMessage())));
        }
        return null;
      });
  }
}
