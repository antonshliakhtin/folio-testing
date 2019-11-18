package org.folio.repository.book;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.folio.db.CqlQuery;
import org.folio.rest.jaxrs.model.Book;
import org.folio.rest.jaxrs.model.BooksCollection;
import org.folio.rest.persist.PostgresClient;
import org.folio.rest.persist.interfaces.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class BookRepositoryImpl implements BookRepository {

  private static final String BOOK_TABLE = "book_data";
  private final Logger log = LoggerFactory.getLogger(BookRepositoryImpl.class);
  private Vertx vertx;

  @Autowired
  public BookRepositoryImpl(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public Future<Book> save(Book book, String tenantId) {
    log.debug("Book -> " + book);

    Future<String> future = Future.future();

    if (StringUtils.isBlank(book.getId())) {
      book.setId(UUID.randomUUID().toString());
    }

    PostgresClient.getInstance(vertx, tenantId)
      .save(BOOK_TABLE, book.getId(), book, future);

    return future.map(bookId -> {
      book.setId(bookId);
      return book;
    });
  }

  @Override
  public Future<Book> findOne(String id, String tenantId) {
    log.debug("id -> " + id);

    Future<Book> future = Future.future();

    PostgresClient.getInstance(vertx, tenantId)
      .getById(BOOK_TABLE, id, Book.class, future);

    return future.map(book -> {
      if (Objects.isNull(book)) {
        log.debug("Book " + id + " not found");
        throw new NotFoundException("Book " + id + " not found");
      }
      return book;
    });
  }

  @Override
  public Future<BooksCollection> getAll(String cqlQuery, int offset, int limit, String tenantId) {
    log.debug("Getting books. new query:" + cqlQuery);

    CqlQuery<Book> q = new CqlQuery<>(PostgresClient.getInstance(vertx, tenantId), BOOK_TABLE, Book.class);

    return q.get(cqlQuery, offset, limit).map(this::mapBookResults);
  }

  private BooksCollection mapBookResults(Results<Book> results) {
    List<Book> books = new ArrayList<>(results.getResults());

    BooksCollection booksCollection = new BooksCollection();
    booksCollection.setBooks(books);
    Integer totalRecords = results.getResultInfo().getTotalRecords();
    booksCollection.setTotalRecords(totalRecords);
    return booksCollection;
  }
}
