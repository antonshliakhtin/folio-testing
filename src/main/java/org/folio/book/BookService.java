package org.folio.book;

import io.vertx.core.Future;
import org.folio.common.OkapiParams;
import org.folio.rest.jaxrs.model.Book;
import org.folio.rest.jaxrs.model.BooksCollection;

public interface BookService {
  Future<BooksCollection> getBooks(String query, int offset, int limit, String tenantId);

  Future<Book> addBook(Book book, OkapiParams okapiParams);

  Future<Book> getOneBook(String id, String tenantId);

  Future<Void> deleteBook(String id, String tenantId);

  Future<Void> updateBook(String id, Book book, OkapiParams okapiParams);
}
