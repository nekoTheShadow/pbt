package ch10;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;

public record BookInfo(String isbn, String title, String author) {
  public static Arbitrary<BookInfo> getBookInfo() {
    return Combinators.combine(getIsbn(), getTitle(), getAuthor()).as(BookInfo::new);
  }

  public static Arbitrary<String> getAuthor() {
    return Arbitraries.strings().ofMinLength(1).ofMaxLength(256);
  }

  public static Arbitrary<String> getTitle() {
    return Arbitraries.strings().ofMinLength(1).ofMaxLength(256);
  }

  public static Arbitrary<String> getIsbn() {
    return Combinators.combine(Arbitraries.of("978", "979"),
        Arbitraries.integers().between(0, 9999), Arbitraries.integers().between(0, 9999),
        Arbitraries.integers().between(0, 999), Arbitraries.chars().with("0123456789X"))
        .as((n, a, b, c, checkDigit) -> {
          return String.join("-", n, String.valueOf(a), String.valueOf(b), String.valueOf(c),
              String.valueOf(checkDigit));
        });
  }
}
