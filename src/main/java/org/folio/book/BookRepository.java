package org.folio.book;

import io.vertx.core.Future;
import org.folio.rest.jaxrs.model.Book;
import org.folio.rest.jaxrs.model.BooksCollection;

public interface BookRepository {

  Future<Book> save(Book book, String tenantId);

  Future<Book> findOne(String id, String tenantId);

  Future<BooksCollection> getAll(String cqlQuery, int offset, int limit, String tenantId);
}
