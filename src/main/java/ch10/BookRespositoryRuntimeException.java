package ch10;

import java.sql.SQLException;

public class BookRespositoryRuntimeException extends RuntimeException {
  public BookRespositoryRuntimeException(SQLException e) {
    super(e);
  }
}
