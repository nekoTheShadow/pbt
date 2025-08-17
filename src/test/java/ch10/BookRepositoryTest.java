package ch10;

import static org.assertj.core.api.Assertions.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.lifecycle.BeforeProperty;
import net.jqwik.api.state.Action;
import net.jqwik.api.state.ActionChain;
import net.jqwik.api.state.Transformer;

public class BookRepositoryTest {

  private BookRepository reposity;
  private Map<String, Book> books;

  @BeforeProperty
  void setUp() {
    this.reposity = new BookRepository();
    this.reposity.initialize();
    this.books = new HashMap<>();
  }

  @Property
  void propertyBasedTestForBookRepository(@ForAll("chain") ActionChain<Map<String, Book>> chain) {
    chain.run();
  }


  @Provide
  private Arbitrary<ActionChain<Map<String, Book>>> chain() {
    return ActionChain.startWith(() -> books).withAction(new AddBook()).withAction(new AddCopy())
        .withAction(new BorrowCopy()).withAction(new ReturnCopy()).withAction(new FindByAuthor())
        .withAction(new FindByTitle()).withAction(new FindByIsbn());
  }

  private class AddBook implements Action.Independent<Map<String, Book>> {
    @Override
    public Arbitrary<Transformer<Map<String, Book>>> transformer() {
      return BookInfo.getBookInfo()
          .map(bookInfo -> Transformer.mutate("AddBook " + bookInfo, books -> {
            BookRepositoryStatus status =
                reposity.addBook(bookInfo.isbn(), bookInfo.title(), bookInfo.author());
            if (books.containsKey(bookInfo.isbn())) {
              assertThat(status).isEqualTo(BookRepositoryStatus.Existing);
              return;
            }
            assertThat(status).isEqualTo(BookRepositoryStatus.Success);
            books.put(bookInfo.isbn(),
                new Book(bookInfo.isbn(), bookInfo.title(), bookInfo.author(), 1, 1));
          }));
    }
  }

  private class AddCopy implements Action.Independent<Map<String, Book>> {
    @Override
    public Arbitrary<Transformer<Map<String, Book>>> transformer() {
      return BookInfo.getIsbn().map(isbn -> Transformer.mutate("AddCopy " + isbn, books -> {
        BookRepositoryStatus status = reposity.addCopy(isbn);
        if (!books.containsKey(isbn)) {
          assertThat(status).isEqualTo(BookRepositoryStatus.NotFound);
          return;
        }

        assertThat(status).isEqualTo(BookRepositoryStatus.Success);
        Book book = books.get(isbn);
        books.put(isbn, new Book(book.isbn(), book.title(), book.author(), book.owned() + 1,
            book.available() + 1));
      }));
    }
  }

  private class BorrowCopy implements Action.Independent<Map<String, Book>> {
    @Override
    public Arbitrary<Transformer<Map<String, Book>>> transformer() {
      return BookInfo.getIsbn().map(isbn -> Transformer.mutate("BorrowCopy " + isbn, books -> {
        BookRepositoryStatus status = reposity.borrowCopy(isbn);
        if (!books.containsKey(isbn)) {
          assertThat(status).isEqualTo(BookRepositoryStatus.NotFound);
          return;
        }

        Book book = books.get(isbn);
        if (book.available() == 0) {
          assertThat(status).isEqualTo(BookRepositoryStatus.Unavailable);
          return;
        }

        assertThat(status).isEqualTo(BookRepositoryStatus.Success);
        books.put(isbn,
            new Book(book.isbn(), book.title(), book.author(), book.owned(), book.available() - 1));
      }));
    }
  }

  private class ReturnCopy implements Action.Independent<Map<String, Book>> {
    @Override
    public Arbitrary<Transformer<Map<String, Book>>> transformer() {
      return BookInfo.getIsbn().map(isbn -> Transformer.mutate("ReturnCopy " + isbn, books -> {
        BookRepositoryStatus status = reposity.returnCopy(isbn);

        if (!books.containsKey(isbn)) {
          assertThat(status).isEqualTo(BookRepositoryStatus.NotFound);
          return;
        }

        Book book = books.get(isbn);
        if (book.available() == book.owned()) {
          assertThat(status).isEqualTo(BookRepositoryStatus.Unreturnable);
          return;
        }

        assertThat(status).isEqualTo(BookRepositoryStatus.Success);
        books.put(isbn,
            new Book(book.isbn(), book.title(), book.author(), book.owned(), book.available() + 1));

      }));
    }
  }

  private class FindByAuthor implements Action.Independent<Map<String, Book>> {
    @Override
    public Arbitrary<Transformer<Map<String, Book>>> transformer() {
      return BookInfo.getAuthor()
          .map(author -> Transformer.mutate("FindByAuthor " + author, books -> {
            List<Book> actual = reposity.findByAuthor(author);
            List<Book> expected =
                books.values().stream().filter(book -> book.author().equals(author)).toList();
            assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
          }));
    }
  }

  private class FindByTitle implements Action.Independent<Map<String, Book>> {
    @Override
    public Arbitrary<Transformer<Map<String, Book>>> transformer() {
      return BookInfo.getTitle().map(title -> Transformer.mutate("FindByTitle " + title, books -> {
        List<Book> actual = reposity.findByTitle(title);
        List<Book> expected =
            books.values().stream().filter(book -> book.title().equals(title)).toList();
        assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
      }));
    }
  }

  private class FindByIsbn implements Action.Independent<Map<String, Book>> {
    @Override
    public Arbitrary<Transformer<Map<String, Book>>> transformer() {
      return BookInfo.getIsbn().map(isbn -> Transformer.mutate("FindByIsbn " + isbn, books -> {
        Book actual = reposity.findByIsbn(isbn);
        Book expected = books.get(isbn);
        assertThat(expected).isEqualTo(actual);
      }));
    }
  }
}
