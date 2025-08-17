package ch10;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.h2.jdbcx.JdbcDataSource;

public class BookRepository {
  private ResultSetHandler<List<Book>> newListHandler() {
    return (rs) -> {
      List<Book> books = new ArrayList<>();
      while (rs.next()) {
        books.add(toBook(rs));
      }
      return books;
    };
  }
  
  private ResultSetHandler<Book> newHandler() {
    return (rs) -> rs.next() ? toBook(rs) : null;
  }
  
  private Book toBook(ResultSet rs) throws SQLException {
    String isbn = rs.getString("isbn");
    String title = rs.getString("title");
    String author = rs.getString("author");
    int owned = rs.getInt("owned");
    int available = rs.getInt("available");
    return new Book(isbn, title, author, owned, available);
  }


  private QueryRunner queryRunner;

  public BookRepository() {
    JdbcDataSource dataSource = new JdbcDataSource();
    dataSource.setUrl("jdbc:h2:mem:testdb;;DB_CLOSE_DELAY=-1;");
    this.queryRunner = new QueryRunner(dataSource);
  }

  public void initialize() {
    try {
      queryRunner.update("DROP TABLE IF EXISTS books;");
      queryRunner.update("""
          CREATE TABLE books (
             isbn varchar(20) PRIMARY KEY,
             title varchar(256) NOT NULL,
             author varchar(256) NOT NULL,
             owned smallint DEFAULT 0,
             available smallint DEFAULT 0
          );""");

    } catch (SQLException e) {
      throw new BookRespositoryRuntimeException(e);
    }
  }

  public BookRepositoryStatus addBook(String isbn, String title, String author) {
    try {
      if (findByIsbn(isbn) != null) {
        return BookRepositoryStatus.Existing;
      }
      queryRunner.update(
          "INSERT INTO books (isbn, title, author, owned, available) VALUES (?, ?, ?, 1, 1)", isbn,
          title, author);
      return BookRepositoryStatus.Success;
    } catch (SQLException e) {
      throw new BookRespositoryRuntimeException(e);
    }
  }
  
  public BookRepositoryStatus addCopy(String isbn) {
    try {
      // 本自体が存在しない
      if (findByIsbn(isbn) == null) {
        return BookRepositoryStatus.NotFound;
      }
      queryRunner.update(
          "UPDATE books SET owned = owned + 1, available = available + 1 WHERE isbn = ?", isbn);
      return BookRepositoryStatus.Success;
    } catch (SQLException e) {
      throw new BookRespositoryRuntimeException(e);
    }
  }

  public BookRepositoryStatus borrowCopy(String isbn) {
    try {
      Book book = findByIsbn(isbn);
      
      // 本自体が存在しない
      if (book == null) {
        return BookRepositoryStatus.NotFound;
      }
      
      // 本は存在するが、すべて貸し出し中
      if (book.available() == 0) {
        return BookRepositoryStatus.Unavailable;
      }
      
      queryRunner.update(
          "UPDATE books SET available = available - 1WHERE isbn = ?", isbn);
      return BookRepositoryStatus.Success;
    } catch (SQLException e) {
      throw new BookRespositoryRuntimeException(e);
    }
  }

  public BookRepositoryStatus returnCopy(String isbn) {
    try {
      Book book = findByIsbn(isbn);
      
      // 本自体が存在しない
      if (book == null) {
        return BookRepositoryStatus.NotFound;
      }
      
      // 本は存在するが、貸し出し中のものがない
      if (book.available() == book.owned()) {
        return BookRepositoryStatus.Unreturnable;
      }
      
      queryRunner.update("UPDATE books SET available = available + 1 WHERE isbn = ?", isbn);
      return BookRepositoryStatus.Success;
    } catch (SQLException e) {
      throw new BookRespositoryRuntimeException(e);
    }
  }

  public List<Book> findByAuthor(String author) {
    try {
      return queryRunner.query("SELECT * FROM books WHERE author LIKE ?", newListHandler(), author);
    } catch (SQLException e) {
      throw new BookRespositoryRuntimeException(e);
    }
  }
  
  public Book findByIsbn(String isbn) {
    try {
      return queryRunner.query("SELECT * FROM books WHERE isbn = ?", newHandler(), isbn);
    } catch (SQLException e) {
      throw new BookRespositoryRuntimeException(e);
    }
  }

  public List<Book> findByTitle(String title) {
    try {
      return queryRunner.query("SELECT * FROM books WHERE title LIKE ?", newListHandler(), title);
    } catch (SQLException e) {
      throw new BookRespositoryRuntimeException(e);
    }
  }
  

}
