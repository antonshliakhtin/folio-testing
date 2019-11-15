package org.folio.book;

import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.folio.common.OkapiParams;
import org.folio.rest.jaxrs.model.Book;
import org.folio.rest.jaxrs.model.BooksCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookServiceImpl implements BookService {

  private final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);

  @Autowired
  private BookRepository repository;

  @Override
  public Future<BooksCollection> getBooks(String query, int offset, int limit, String tenantId) {
    return null;
  }

  @Override
  public Future<Book> addBook(Book book, OkapiParams okapiParams) {
    log.debug("Removing unsafe tags");

    return null;
  }

  @Override
  public Future<Book> getOneBook(String id, String tenantId) {
    return repository.findOne(id, tenantId);
  }

  @Override
  public Future<Void> deleteBook(String id, String tenantId) {
    return null;
  }

  @Override
  public Future<Void> updateBook(String id, Book book, OkapiParams okapiParams) {
    return null;
  }
}
